package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.StringReader;
import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

class InternalArgumentType implements ArgumentType<Object> {
    private final String id;

    protected InternalArgumentType(String id) {
        this.id = id;
    }

    @Override
    public Object parse(StringReader var1) throws CommandSyntaxException {
        return null;
    }

    @Override
    public Class<Object> type() {
        return Object.class;
    }

    @Override
    public String toString() {
        return "InternalArgumentType{" +
                "id='" + id + '\'' +
                '}';
    }
}
