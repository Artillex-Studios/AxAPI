package com.artillexstudios.axapi.utils;

import org.slf4j.LoggerFactory;

public final class LogUtils {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final FileLogger logger = new FileLogger("logs");

    public static void debug(String message) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message);
        logger.log(message);
    }

    public static void debug(String message, Object object) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object);

        String formatted = org.apache.commons.lang.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
        logger.log(formatted);
    }

    public static void debug(String message, Object object, Object object2) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2);

        String formatted = org.apache.commons.lang.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
        logger.log(formatted);
    }

    public static void debug(String message, Object object, Object object2, Object object3) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2, object3);

        String formatted = org.apache.commons.lang.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object3 == null ? "null" : object3.toString(), 1);
        logger.log(formatted);
    }

    public static void debug(String message, Object object, Object object2, Object object3, Object object4) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2, object3, object4);

        String formatted = org.apache.commons.lang.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object3 == null ? "null" : object3.toString(), 1);
        formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", object4 == null ? "null" : object4.toString(), 1);
        logger.log(formatted);
    }

    public static void debug(String message, Object... arguments) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, arguments);

        String formatted = message;
        for (Object argument : arguments) {
            formatted = org.apache.commons.lang.StringUtils.replace(formatted, "{}", argument == null ? "null" : argument.toString(), 1);
        }
        logger.log(formatted);
    }

    public static void warn(String message, Object... arguments) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).warn(message, arguments);
    }

    public static void error(String message, Object... arguments) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).error(message, arguments);
    }

    public static void info(String message, Object... arguments) {
        LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, arguments);
    }
}
