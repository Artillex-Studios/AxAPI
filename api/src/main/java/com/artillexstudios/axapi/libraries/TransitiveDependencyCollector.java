package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TransitiveDependencyCollector {
    private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{(.+)}");
    private final Set<Library> seen = new HashSet<>();
    private final LibraryDownloader downloader;

    public TransitiveDependencyCollector(LibraryDownloader downloader) {
        this.downloader = downloader;
    }

    private List<Library> findTransitiveDependencies(Library library) {
        this.seen.add(library);
        List<Library> found = new ArrayList<>();
        for (Repository repository : this.downloader.getRepositories()) {
            URI pomURI = repository.getPomURI(library);
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Downloading from URI {}", pomURI);
            }

            try (InputStream stream = pomURI.toURL().openStream()) {
                List<Library> foundLibrary = this.findLibrary(library, stream);
                found.addAll(foundLibrary);
                break;
            } catch (FileNotFoundException exception) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Library pom not found at: {}", pomURI);
                }
                continue;
            } catch (IOException | ParserConfigurationException | SAXException exception) {
                LogUtils.error("An exception occurred while trying to find transitive libraries for library: {}!", library, exception);
                continue;
            }
        }

        List<Library> transitiveDependencies = new ArrayList<>();
        for (Library dependency : found) {
            if (!this.hasAlreadySeen(dependency)) {
                List<Library> transitiveDependency = this.findTransitiveDependencies(dependency);
                transitiveDependencies.add(new Library(dependency.group(), dependency.artifactId(), dependency.version(), dependency.classifier(), transitiveDependency));
            }
        }

        return transitiveDependencies;
    }

    public Library withTransitiveDependencies(Library library) {
        return new Library(library.group(), library.artifactId(), library.version(), library.classifier(), this.findTransitiveDependencies(library));
    }

    public void reset() {
        this.seen.clear();
    }

    public boolean hasAlreadySeen(Library library) {
        for (Library loaded : this.seen) {
            if (LibraryCache.checkWithoutVersion(library, loaded)) {
                return true;
            }
        }

        return false;
    }

    private String findProperty(Library library, Element documentElement, String property) throws IOException, ParserConfigurationException, SAXException {
        NodeList properties = documentElement.getElementsByTagName("properties");
        for (int i = 0; i < properties.getLength(); i++) {
            if (properties.item(i) instanceof Element element) {
                NodeList elementsByTagName = element.getElementsByTagName(property);
                if (elementsByTagName.getLength() > 0) {
                    return elementsByTagName.item(0).getTextContent();
                }
            }
        }

        NodeList elementsByTagName = documentElement.getElementsByTagName("parent");
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            if (elementsByTagName.item(i) instanceof Element element) {
                Library created = this.createLibrary(library, element, documentElement);
                for (Repository repository : this.downloader.getRepositories()) {
                    URI pomURI = repository.getPomURI(created);
                    try (InputStream stream = pomURI.toURL().openStream()) {
                        String foundProperty = this.findProperty(created, this.createDocumentElement(stream), property);
                        if (foundProperty != null) {
                            return foundProperty;
                        }
                    }
                }
            }
        }

        return null;
    }

    private List<Library> findLibrary(Library library, InputStream stream) throws SAXException, ParserConfigurationException, IOException {
        List<Library> libraries = new ArrayList<>();
        Element documentElement = this.createDocumentElement(stream);
        NodeList childNodes = documentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();

            if (nodeName.equals("dependencies")) {
                NodeList dependencies = item.getChildNodes();
                for (int j = 0; j < dependencies.getLength(); j++) {
                    Node dependency = dependencies.item(j);
                    if (!(dependency instanceof Element element)) {
                        continue;
                    }

                    Library created = this.createLibrary(library, element, documentElement);
                    if (created == null) {
                        continue;
                    }

                    libraries.add(created);
                }
            }
        }

        return libraries;
    }

    private Element createDocumentElement(InputStream stream) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document parse = documentBuilder.parse(stream);
        return parse.getDocumentElement();
    }

    private Library createLibrary(Library library, Element element, Element documentElement) throws IOException, ParserConfigurationException, SAXException {
        String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
        String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
        NodeList versionNode = element.getElementsByTagName("version");
        String version = versionNode.getLength() == 0 ? "" : versionNode.item(0).getTextContent();
        NodeList scopeNode = element.getElementsByTagName("scope");
        String scope = scopeNode.getLength() == 0 ? "compile" : scopeNode.item(0).getTextContent();
        NodeList classifierNode = element.getElementsByTagName("classifier");
        String classifier = classifierNode.getLength() == 0 ? "" : classifierNode.item(0).getTextContent();
        NodeList optionalNode = element.getElementsByTagName("optional");
        boolean optional = optionalNode.getLength() != 0 && Boolean.parseBoolean(optionalNode.item(0).getTextContent().trim());

        if (optional) {
            return null;
        }

        if (!scope.equals("compile")) {
            return null;
        }

        if (version.equals("${project.version}")) {
            version = library.version();
        }

        if (groupId.equals("${project.groupId}")) {
            groupId = library.group();
        }

        version = this.findReplacement(library, documentElement, version);
        groupId = this.findReplacement(library, documentElement, groupId);
        artifactId = this.findReplacement(library, documentElement, artifactId);
        classifier = this.findReplacement(library, documentElement, classifier);

        if (version.isBlank()) {
            return null;
        }

        return new Library(groupId.replace("${", "").replace("}", ""), artifactId.replace("${", "").replace("}", ""), version.replace("${", "").replace("}", ""), classifier.replace("${", "").replace("}", ""), List.of());
    }

    private String findReplacement(Library library, Element documentElement, String property) throws IOException, ParserConfigurationException, SAXException {
        Matcher matcher = PROPERTY_PATTERN.matcher(property);
        if (matcher.find()) {
            String group = matcher.group(1);
            String foundProperty = this.findProperty(library, documentElement, group);
            if (foundProperty != null) {
                return foundProperty;
            }
        }

        return property;
    }
}
