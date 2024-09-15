package com.artillexstudios.axapi.nms.v1_21_R1.command;

import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;
import com.artillexstudios.axapi.utils.ComponentSerializer;

public class StringReader implements com.artillexstudios.axapi.commands.StringReader {
    private final com.mojang.brigadier.StringReader parent;

    public StringReader(com.mojang.brigadier.StringReader parent) {
        this.parent = parent;
    }

    @Override
    public String getString() {
        return this.parent.getString();
    }

    @Override
    public int getRemainingLength() {
        return this.parent.getRemainingLength();
    }

    @Override
    public int getTotalLength() {
        return this.parent.getTotalLength();
    }

    @Override
    public int getCursor() {
        return this.parent.getCursor();
    }

    @Override
    public void setCursor(int cursor) {
        this.parent.setCursor(cursor);
    }

    @Override
    public String getRead() {
        return this.parent.getRead();
    }

    @Override
    public String getRemaining() {
        return this.parent.getRemaining();
    }

    @Override
    public boolean canRead(int length) {
        return this.parent.canRead(length);
    }

    @Override
    public boolean canRead() {
        return this.parent.canRead();
    }

    @Override
    public char peek() {
        return this.parent.peek();
    }

    @Override
    public char peek(int offset) {
        return this.parent.peek(offset);
    }

    @Override
    public char read() {
        return this.parent.read();
    }

    @Override
    public void skip() {
        this.parent.skip();
    }

    @Override
    public void skipWhitespace() {
        this.parent.skipWhitespace();
    }

    @Override
    public int readInt() throws CommandSyntaxException {
        try {
            return this.parent.readInt();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public long readLong() throws CommandSyntaxException {
        try {
            return this.parent.readInt();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public double readDouble() throws CommandSyntaxException {
        try {
            return this.parent.readInt();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public float readFloat() throws CommandSyntaxException {
        try {
            return this.parent.readInt();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public String readUnquotedString() {
        return this.parent.readUnquotedString();
    }

    @Override
    public String readQuotedString() throws CommandSyntaxException {
        try {
            return this.parent.readQuotedString();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public String readStringUntil(char terminator) throws CommandSyntaxException {
        try {
            return this.parent.readStringUntil(terminator);
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public String readString() throws CommandSyntaxException {
        try {
            return this.parent.readString();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }

    @Override
    public boolean readBoolean() throws CommandSyntaxException {
        try {
            return this.parent.readBoolean();
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            throw new CommandSyntaxException(ComponentSerializer.INSTANCE.fromVanilla(e.getRawMessage()), e.getInput(), e.getCursor());
        }
    }
}
