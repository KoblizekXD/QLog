package com.koblizek.qlog.client.ptracer;

import com.koblizek.qlog.util.ChatColor;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.packet.Packet;

public enum PacketType {
    INCOMING("S2C", ChatColor.DARK_GREEN),
    OUTGOING("C2S", ChatColor.AQUA),
    ANY("", ChatColor.WHITE);

    private final String determinator;
    private final ChatColor color;

    PacketType(String determinator, ChatColor color) {
        this.determinator = determinator;
        this.color = color;
    }

    public String getDeterminator() {
        return determinator;
    }

    public static PacketType determine(Packet<?> packet) {
        if (packet.getClass().getSimpleName().contains(INCOMING.getDeterminator()))
            return INCOMING;
        else
            return OUTGOING;
    }

    public String toColoredString() {
        return (color.toString() + this.toString()).trim();
    }
}
