package com.artillexstudios.axapi.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class SimplePattern {
    private final String expression;
    private final boolean endsWith;
    private final boolean startsWith;

    public static SimplePattern of(String expression) {
        return new SimplePattern(expression.isBlank() ? "%%" : expression);
    }

    private SimplePattern(String expression) {
        String modifiableExpression = expression;
        if (expression.charAt(0) == '%') {
            this.endsWith = true;
            modifiableExpression = modifiableExpression.substring(1);
        } else {
            this.endsWith = false;
        }

        if (expression.charAt(expression.length() - 1) == '%') {
            this.startsWith = true;
            modifiableExpression = modifiableExpression.substring(0, modifiableExpression.length() - 1);
        } else {
            this.startsWith = false;
        }
        this.expression = modifiableExpression;
    }

    public boolean matches(String other) {
        if (this.endsWith && this.startsWith) {
            return other.contains(this.expression);
        }

        if (this.endsWith) {
            return other.endsWith(this.expression);
        }

        if (this.startsWith) {
            return other.startsWith(this.expression);
        }

        return other.equals(this.expression);
    }
}
