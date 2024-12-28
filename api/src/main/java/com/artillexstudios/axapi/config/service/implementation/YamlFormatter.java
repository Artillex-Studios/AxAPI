package com.artillexstudios.axapi.config.service.implementation;

import com.artillexstudios.axapi.config.service.Formatter;

public final class YamlFormatter implements Formatter {

    @Override
    public String format(String toFormat) {
        StringBuilder builder = new StringBuilder();
        String[] lines = toFormat.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.strip().startsWith("#")) {
                if (getLeadingWhiteSpace(line) == 0 && i >= 1) {
                    builder.append("\n");
                }
                builder.append(this.toPrettyComment(line));
                builder.append("\n");
                int j = i + 1;
                while (j < lines.length) {
                    String nextLine = lines[j];
                    if (!nextLine.strip().startsWith("#")) {
                        break;
                    }

                    builder.append(this.toPrettyComment(nextLine));
                    builder.append("\n");
                    j++;
                    i++;
                }
            } else if (i >= 1 && getLeadingWhiteSpace(line) < getLeadingWhiteSpace(lines[i - 1]) && getLeadingWhiteSpace(line) == 0) {
                builder.append(line);
                builder.append("\n");
            } else {
                builder.append(line);
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private int getLeadingWhiteSpace(String string) {
        int whiteSpace = 0;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (!Character.isWhitespace(ch) && ch != '-') {
                break;
            }

            whiteSpace++;
        }

        return whiteSpace;
    }

    private String toPrettyComment(String string) {
        int index = string.indexOf('#');
        if (index == -1 || index == string.length() - 1) {
            return string;
        }

        char ch = string.charAt(index + 1);
        // Already pretty
        if (Character.isWhitespace(ch)) {
            return string;
        }

        StringBuilder builder = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '#') {
                builder.append('#').append(' ');
            } else {
                builder.append(string.charAt(i));
            }
        }

        return builder.toString();
    }
}
