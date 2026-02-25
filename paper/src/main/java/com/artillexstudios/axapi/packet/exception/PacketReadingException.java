package com.artillexstudios.axapi.packet.exception;

/**
 * Represents an exception that should be thrown when
 * a packet can't be read, and the execution of packetlisteners should be stopped.
 */
public class PacketReadingException extends RuntimeException {
    public static final PacketReadingException INSTANCE = new PacketReadingException();

    private PacketReadingException() {
        super();
    }
}
