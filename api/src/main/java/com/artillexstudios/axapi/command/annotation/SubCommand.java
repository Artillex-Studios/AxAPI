package com.artillexstudios.axapi.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a subcommand of a command, with aliases
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SubCommand {

    /**
     * The name and aliases of the subcommand
     * @return The subcommand name & aliases
     */
    String[] name();
}
