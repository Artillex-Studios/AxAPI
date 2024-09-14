package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.StringReader;
import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

class InternalArgumentType implements ArgumentType<Object> {

    @Override
    public Object parse(StringReader var1) throws CommandSyntaxException {
        return null;
    }
}
