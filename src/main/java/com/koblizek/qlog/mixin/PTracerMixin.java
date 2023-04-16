package com.koblizek.qlog.mixin;

import com.koblizek.qlog.client.ptracer.PTracer;
import com.koblizek.qlog.client.ptracer.PacketType;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(ClientConnection.class)
public class PTracerMixin {

    private static final Logger LOGGER = LoggerFactory.getLogger("PTracer");

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static void apply(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (PTracer.enabled) {
            PTracer tracer = PTracer.initialize(packet);
            tracer.sendWithSettings(tracer.parse());
        }
    }
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V",
            at = @At("HEAD"))
    public void send(Packet<?> packet, CallbackInfo ci) {
        if (PTracer.enabled) {
            PTracer tracer = PTracer.initialize(packet);
            tracer.sendWithSettings(tracer.parse());
        }
    }
}
