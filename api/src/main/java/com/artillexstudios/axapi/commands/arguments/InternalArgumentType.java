package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

public class InternalArgumentType implements ArgumentType<Object, Object> {
    private final String id;

    protected InternalArgumentType(String id) {
        this.id = id;
    }

    @Override
    public Object parse(Object var1) throws CommandSyntaxException {
        return null;
    }

    @Override
    public Class<Object> type() {
        return Object.class;
    }

    @Override
    public ArgumentType<?, Object> internalType() {
        return null;
    }

    @Override
    public String toString() {
        return "InternalArgumentType{" +
                "id='" + id + '\'' +
                '}';
    }
}
