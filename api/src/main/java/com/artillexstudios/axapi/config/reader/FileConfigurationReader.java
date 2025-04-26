package com.artillexstudios.axapi.config.reader;

import com.artillexstudios.axapi.config.YamlConstructor;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.config.annotation.Comment;
import it.unimi.dsi.fastutil.Pair;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FileConfigurationReader implements Handler {
    private final TypeAdapterHolder holder;
    private final YamlConstructor constructor;
    private final Yaml yaml;

    public FileConfigurationReader(Yaml yaml, YamlConstructor constructor, TypeAdapterHolder holder) {
        this.holder = holder;
        this.constructor = constructor;
        this.yaml = yaml;
    }

    @Override
    public Pair<Map<String, Object>, Map<String, Comment>> read(InputStream stream, Object instance) {
        LinkedHashMap<String, Object> contents = new LinkedHashMap<>();
        Map<String, Comment> comments = new HashMap<>();
        this.load("", (MappingNode) this.yaml.compose(new UnicodeReader(stream)), contents, comments);
        return Pair.of(contents, comments);
    }

    @Override
    public String write(Map<String, Object> contents, Map<String, Comment> comments) {
        StringWriter writer = new StringWriter();
        this.yaml.serialize(this.map(contents, comments, ""), writer);
        return writer.toString();
    }

    private MappingNode map(Map<String, Object> map, Map<String, Comment> comments, String path) {
        List<NodeTuple> nodes = new ArrayList<>();
        Node key;
        Node value;
        for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); nodes.add(new NodeTuple(key, value))) {
            Map.Entry<String, Object> entry = iterator.next();
            key = this.yaml.represent(entry.getKey());
            if (entry.getValue() instanceof Map<?, ?> m) {
                value = this.map((Map<String, Object>) m, comments, path.isEmpty() ? entry.getKey() : path + "." + entry.getKey());
            } else {
                value = this.yaml.represent(entry.getValue());
            }

            Comment comment = comments.get(path.isEmpty() ? entry.getKey() : path + "." + entry.getKey());
            if (comment != null) {
                List<CommentLine> lines = new ArrayList<>();
                String[] split = comment.value().split("\n");
                for (String string : split) {
                    lines.add(new CommentLine(null, null, string, comment.type() == Comment.CommentType.BLOCK ? CommentType.BLOCK : CommentType.IN_LINE));
                }
                if (comment.type() == Comment.CommentType.BLOCK) {
                    key.setBlockComments(lines);
                } else {
                    key.setInLineComments(lines);
                }
            }
        }

        return new MappingNode(Tag.MAP, nodes, DumperOptions.FlowStyle.BLOCK);
    }

    private void load(String path, MappingNode node, LinkedHashMap<String, Object> map, Map<String, Comment> comments) {
        if (node == null) {
            return;
        }

        this.constructor.flatten(node);

        for (NodeTuple nodeTuple : node.getValue()) {
            Node key = nodeTuple.getKeyNode();
            String keyString = String.valueOf(this.constructor.construct(key));
            Node value;

            for (value = nodeTuple.getValueNode(); value instanceof AnchorNode anchorNode; value = anchorNode.getRealNode()) {

            }

            String path1 = path.isEmpty() ? keyString : path + "." + keyString;
            if (value instanceof MappingNode mappingNode) {
                LinkedHashMap<String, Object> newSection = new LinkedHashMap<>();
                map.put(keyString, newSection);
                this.load(keyString, mappingNode, newSection, comments);
            } else {
                map.put(keyString, this.constructor.construct(value));
            }

            List<CommentLine> blockComments = key.getBlockComments();
            if (blockComments != null) {
                StringBuilder commentValue = new StringBuilder();
                for (int i = 0; i < blockComments.size(); i++) {
                    CommentLine blockComment = blockComments.get(i);
                    commentValue.append(blockComment.getValue());
                    if (i + 1 < blockComments.size()) {
                        commentValue.append('\n');
                    }
                }

                if (!commentValue.isEmpty()) {
                    comments.put(path1, new Comment() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return Comment.class;
                        }

                        @Override
                        public String value() {
                            return commentValue.toString();
                        }

                        @Override
                        public CommentType type() {
                            return CommentType.BLOCK;
                        }
                    });
                }
            }

            List<CommentLine> inlineComments = key.getInLineComments();
            if (inlineComments != null) {
                StringBuilder commentValue = new StringBuilder();
                for (int i = 0; i < inlineComments.size(); i++) {
                    CommentLine comment = inlineComments.get(i);
                    commentValue.append(comment.getValue());
                    if (i + 1 < inlineComments.size()) {
                        commentValue.append('\n');
                    }
                }

                if (!commentValue.isEmpty()) {
                    comments.put(path1, new Comment() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return Comment.class;
                        }

                        @Override
                        public String value() {
                            return commentValue.toString();
                        }

                        @Override
                        public CommentType type() {
                            return CommentType.INLINE;
                        }
                    });
                }
            }
        }

    }
}
