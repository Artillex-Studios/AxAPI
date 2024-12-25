package com.artillexstudios.axapi.command.annotation;

public @interface Range {

    double min() default Double.MIN_VALUE;

    double max() default Double.MAX_VALUE;
}
