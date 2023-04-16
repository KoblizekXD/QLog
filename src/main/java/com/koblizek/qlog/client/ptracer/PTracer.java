package com.koblizek.qlog.client.ptracer;

import com.koblizek.qlog.util.ChatColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringJoiner;

public final class PTracer {
    private final Packet<?> packet;
    private final PacketType type;
    private final Settings settings;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Logger logger = LoggerFactory.getLogger("PTracer");
    public static boolean enabled = false;
    public static Text text = Text.literal("Enable PTracer");

    private PTracer(Packet<?> packet) {
        this.packet = packet;
        this.type = PacketType.determine(packet);
        this.settings = new Settings();
    }
    public static void switchStatus() {
        enabled = !enabled;
        if (text.contains(Text.literal("Enable PTracer")))
            text = Text.literal("Disable PTracer");
        else
            text = Text.literal("Enable PTracer");
    }

    public static PTracer initialize(Packet<?> packet) {
        return new PTracer(packet);
    }
    public void sendChatMessage(String text) {
        try {
            Method method = ChatHud.class.getDeclaredMethod("addMessage", Text.class, MessageSignatureData.class, int.class, MessageIndicator.class, boolean.class);
            method.setAccessible(true);

            method.invoke(mc.inGameHud.getChatHud(), Text.literal(text), null, mc.inGameHud.getTicks(), mc.isConnectedToLocalServer() ? MessageIndicator.singlePlayer() : MessageIndicator.system(), false);
        } catch (NoSuchMethodException e) {
            logger.error("Failed to send chat message!");
        } catch (InvocationTargetException e) {
            logger.error("Internal error occurred");
        } catch (IllegalAccessException e) {
            logger.warn("Failed to obtain access to targeting method");
        }
    }
    public void sendWithSettings(String text) {
        switch (settings.type) {
            case LOG_ONLY -> logger.info(text);
            case MSG_ONLY -> sendChatMessage(text);
            case BOTH -> {
                logger.info(text);
                sendChatMessage(text);
            }
        }
    }
    public String parse() {
        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.RED + "Packet Detected:" + ChatColor.PINK + " [")
                .append(ChatColor.GOLD + packet.getClass().getSimpleName())
                .append(ChatColor.PINK + "/")
                .append(type.toColoredString())
                .append(ChatColor.PINK + "] {");

        for (Field field : packet.getClass().getDeclaredFields()) {
            builder.append("\n" + ChatColor.WHITE);
            if (field.trySetAccessible()) {
                try {
                    builder.append("    " + ChatColor.GOLD + field.getName() + ChatColor.PINK + " = " + ChatColor.GOLD + field.get(packet) + ",");
                } catch (IllegalAccessException e) {
                    builder.append("    " + ChatColor.GOLD + field.getName() + ChatColor.RED + " is inaccessible,");
                }
            } else {
                builder.append("   " + ChatColor.RED + "field is inaccessible,");
            }
        }
        builder.deleteCharAt(builder.length()-1)
            .append(ChatColor.PINK + "\n}");

        return builder.toString();
    }
    public static class Settings {
        public LoggingType type = LoggingType.BOTH;

        public enum LoggingType {
            LOG_ONLY,
            MSG_ONLY,
            BOTH
        }
    }
}
