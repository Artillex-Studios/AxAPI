package com.artillexstudios.axapi.commands;

import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

public interface StringReader {

    String getString();

    void setCursor(int cursor);

    int getRemainingLength();

    int getTotalLength();

    int getCursor();

    String getRead();

    String getRemaining();

    boolean canRead(int length);

    boolean canRead();

    char peek();

    char peek(int offset);

    char read();

    void skip();

    void skipWhitespace();

    int readInt() throws CommandSyntaxException;

    long readLong() throws CommandSyntaxException;

    double readDouble() throws CommandSyntaxException;

    float readFloat() throws CommandSyntaxException;

    String readUnquotedString();

    String readQuotedString() throws CommandSyntaxException;

    String readStringUntil(char terminator) throws CommandSyntaxException;

    String readString() throws CommandSyntaxException;

    boolean readBoolean() throws CommandSyntaxException;
}
