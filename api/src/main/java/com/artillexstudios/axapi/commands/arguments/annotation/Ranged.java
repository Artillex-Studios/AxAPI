package com.artillexstudios.axapi.commands.arguments.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
public @interface Ranged {

    double min() default Double.MIN_VALUE;

    double max() default Double.MAX_VALUE;
}
