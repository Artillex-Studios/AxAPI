package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.arguments.ArgumentType;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CommandArgument {
    private final ArgumentType<?> type;
    private final String name;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public CommandArgument(ArgumentType<?> type, String name, Annotation[] annotations) {
        this.type = type;
        this.name = name;

        this.annotations = new HashMap<>();
        for (int i = 0; i < annotations.length; i++) {
            this.annotations.put(annotations[i].annotationType(), annotations[i]);
        }
    }

    public Map<Class<? extends Annotation>, Annotation> annotations() {
        return annotations;
    }

    public <T extends Annotation> T annotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }

    public ArgumentType<?> type() {
        return type;
    }

    public String name() {
        return name;
    }
}
