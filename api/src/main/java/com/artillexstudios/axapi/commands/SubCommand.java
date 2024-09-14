package com.artillexstudios.axapi.commands;

import java.lang.reflect.Method;
import java.util.List;

public class SubCommand {
    private final String[] aliases;
    private final Object instance;
    private final Method method;
    private final List<CommandArgument> arguments;
    private final boolean noArgs;

    public SubCommand(String[] aliases, Object instance, Method method, List<CommandArgument> arguments, boolean noArgs) {
        this.aliases = aliases;
        this.instance = instance;
        this.method = method;
        this.arguments = arguments;
        this.noArgs = noArgs;
    }

    public Object instance() {
        return instance;
    }

    public String[] aliases() {
        return aliases;
    }

    public Method method() {
        return method;
    }

    public List<CommandArgument> arguments() {
        return arguments;
    }

    public boolean noArgs() {
        return noArgs;
    }
}
