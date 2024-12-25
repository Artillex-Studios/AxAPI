package com.artillexstudios.axapi.config.renamer;

public final class LowerKebabCaseRenamer implements KeyRenamer {

    @Override
    public String rename(String fieldName) {
        StringBuilder builder = new StringBuilder(fieldName.length());
        for (int i = 0; i < fieldName.length(); i++) {
            char ch = fieldName.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i != 0) {
                    builder.append('-');
                }
                builder.append(Character.toLowerCase(ch));
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }
}
