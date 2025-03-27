package com.artillexstudios.axapi.utils.logging;

import org.slf4j.LoggerFactory;

public final class LogUtils {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final FileLogger logger = new FileLogger("debug-logs");

    public static void debug(String message) {
        debug(message, DebugMode.ALL);
    }

    public static void debug(String message, DebugMode mode) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            logger.log(message);
        }
    }

    public static void debug(String message, Object object) {
        debug(message, object, DebugMode.ALL);
    }

    public static void debug(String message, Object object, DebugMode mode) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            String formatted = org.apache.commons.lang3.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
            logger.log(formatted);
        }
    }

    public static void debug(String message, Object object, Object object2) {
        debug(message, object, object2, DebugMode.ALL);
    }

    public static void debug(String message, Object object, Object object2, DebugMode mode) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            String formatted = org.apache.commons.lang3.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
            logger.log(formatted);
        }
    }

    public static void debug(String message, Object object, Object object2, Object object3) {
        debug(message, object, object2, object3, DebugMode.ALL);
    }

    public static void debug(String message, Object object, Object object2, Object object3, DebugMode mode) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2, object3);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            String formatted = org.apache.commons.lang3.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object3 == null ? "null" : object3.toString(), 1);
            logger.log(formatted);
        }
    }

    public static void debug(String message, Object object, Object object2, Object object3, Object objec4) {
        debug(message, object, object2, object3, objec4, DebugMode.ALL);
    }

    public static void debug(String message, Object object, Object object2, Object object3, Object object4, DebugMode mode) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, object, object2, object3, object4);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            String formatted = org.apache.commons.lang3.StringUtils.replace(message, "{}", object == null ? "null" : object.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object2 == null ? "null" : object2.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object3 == null ? "null" : object3.toString(), 1);
            formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", object4 == null ? "null" : object4.toString(), 1);
            logger.log(formatted);
        }
    }

    public static void debug(String message, Object... arguments) {
        debug(message, arguments, DebugMode.ALL);
    }

    public static void debug(String message, DebugMode mode, Object... arguments) {
        if (mode == DebugMode.CONSOLE || mode == DebugMode.ALL) {
            LoggerFactory.getLogger(STACK_WALKER.getCallerClass()).info(message, arguments);
        }

        if (mode == DebugMode.FILE || mode == DebugMode.ALL) {
            String formatted = message;
            for (Object argument : arguments) {
                formatted = org.apache.commons.lang3.StringUtils.replace(formatted, "{}", argument == null ? "null" : argument.toString(), 1);
            }
            logger.log(formatted);
        }
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
