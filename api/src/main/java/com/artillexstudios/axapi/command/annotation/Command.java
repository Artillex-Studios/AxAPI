package com.artillexstudios.axapi.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a command, with its aliases
 * If used on a class, all of subcommands in the class
 * will be children of this command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Command {

    /**
     * The name and aliases of the command
     * @return The command name & aliases
     */
    String[] name();
}
