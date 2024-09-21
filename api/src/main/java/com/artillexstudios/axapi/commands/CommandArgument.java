package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.arguments.ArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CommandArgument {
    private static final Logger log = LoggerFactory.getLogger(CommandArgument.class);
    private final ArgumentType<Object, Object> type;
    private final String name;
    private final Class<?> inputType;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public CommandArgument(ArgumentType<Object, Object> type, String name, Annotation[] annotations, Class<?> inputType) {
        this.type = type;
        this.name = name;
        this.inputType = inputType;

        this.annotations = new HashMap<>();
        for (int i = 0; i < annotations.length; i++) {
            this.annotations.put(annotations[i].annotationType(), annotations[i]);
        }
    }

    public Map<Class<? extends Annotation>, Annotation> annotations() {
        return annotations;
    }

    public <T extends Annotation> T annotation(Class<T> annotationClass) {
        log.info("Annotations: {}", annotations);
        return annotationClass.cast(annotations.get(annotationClass));
    }

    public ArgumentType<Object, Object> type() {
        return type;
    }

    public Class<?> inputType() {
        return inputType;
    }

    public String name() {
        return name;
    }
}
