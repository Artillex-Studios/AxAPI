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

public final class TransitiveDependencyCollector {
    private final Set<Library> seen = new HashSet<>();
    private final LibraryDownloader downloader;

    public TransitiveDependencyCollector(LibraryDownloader downloader) {
        this.downloader = downloader;
    }

    private List<Library> findTransitiveDependencies(Library library) {
        List<Library> found = new ArrayList<>();
        for (Repository repository : this.downloader.getRepositories()) {
            URI pomURI = repository.getPomURI(library);
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Downloading from URI {}", pomURI);
            }

            try (InputStream stream = pomURI.toURL().openStream()) {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document parse = documentBuilder.parse(stream);
                NodeList childNodes = parse.getDocumentElement().getChildNodes();
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

                            String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
                            String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
                            NodeList versionNode = element.getElementsByTagName("version");
                            String version = versionNode.getLength() == 0 ? "" : versionNode.item(0).getTextContent();
                            NodeList scopeNode = element.getElementsByTagName("scope");
                            String scope = scopeNode.getLength() == 0 ? "compile" : scopeNode.item(0).getTextContent();
                            NodeList classifierNode = element.getElementsByTagName("classifier");
                            String classifier = classifierNode.getLength() == 0 ? null : classifierNode.item(0).getTextContent();

                            if (!scope.equals("compile")) {
                                continue;
                            }

                            Library tempLibrary = new Library(groupId, artifactId, version, classifier, List.of());
                            found.add(tempLibrary);
                        }
                    }
                }
            } catch (FileNotFoundException exception) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Library pom not found at: {}", pomURI);
                }
                continue;
            } catch (IOException | ParserConfigurationException | SAXException exception) {
                LogUtils.error("An exception occurred while trying to find transitive libraries for library: {}!", library, exception);
            }
        }

        List<Library> transitiveDependencies = new ArrayList<>();
        for (Library dependency : found) {
            if (this.seen.add(dependency)) {
                List<Library> transitiveDependency = this.findTransitiveDependencies(dependency);
                transitiveDependencies.add(new Library(dependency.group(), dependency.artifactId(), dependency.version(), dependency.classifier(), transitiveDependency));
            }
        }

        return transitiveDependencies;
    }

    public Library withTransitiveDependencies(Library library) {
        return new Library(library.group(), library.artifactId(), library.version(), library.classifier(), this.findTransitiveDependencies(library));
    }
}
