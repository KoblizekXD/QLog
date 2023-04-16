package com.koblizek.qlog.client.ptracer;

public enum Filters {
    ALL(""),
    MOVEMENT("Move", "Teleport", "Velocity", "Position", "Ground"),
    WORLD("World", "Time", "Chunk"),
    PLAYER("Player"),
    ENTITY("Entity"),
    INCOMING(PacketType.INCOMING, ""),
    OUTGOING(PacketType.OUTGOING, "");

    private final PacketType type;
    private final String[] determinator;

    Filters(PacketType type, String... determinator) {
        this.type = type;
        this.determinator = determinator;
    }
    Filters(String... determinator) {
        this.type = PacketType.ANY;
        this.determinator = determinator;
    }

    public PacketType getType() {
        return type;
    }

    public String[] getDeterminator() {
        return determinator;
    }
}
