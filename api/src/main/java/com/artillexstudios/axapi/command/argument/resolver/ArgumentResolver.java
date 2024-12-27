package com.artillexstudios.axapi.command.argument.resolver;

import com.artillexstudios.axapi.command.builder.CommandTree;

import java.lang.reflect.Parameter;

public interface ArgumentResolver {

    void append(CommandTree tree, Parameter parameter);
}
