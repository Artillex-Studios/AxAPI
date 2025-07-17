package com.artillexstudios.axapi.utils;

public class SimplePattern {
    private final String expression;
    private final boolean beginMatch;
    private final boolean endMatch;

    public static SimplePattern of(String expression) {
        return new SimplePattern(expression);
    }

    public SimplePattern(String expression) {
        String modifiableExpression = expression;
        if (expression.charAt(0) == '%') {
            this.beginMatch = true;
            modifiableExpression = modifiableExpression.substring(1);
        } else {
            this.beginMatch = false;
        }

        if (expression.charAt(expression.length() - 1) == '%') {
            this.endMatch = true;
            modifiableExpression = modifiableExpression.substring(0, modifiableExpression.length() - 1);
        } else {
            this.endMatch = false;
        }
        this.expression = modifiableExpression;
    }

    public boolean matches(String other) {
        if (this.beginMatch && this.endMatch) {
            return other.contains(this.expression);
        }

        if (this.beginMatch) {
            return other.endsWith(this.expression);
        }

        if (this.endMatch) {
            return other.startsWith(this.expression);
        }

        return other.equals(this.expression);
    }
}
