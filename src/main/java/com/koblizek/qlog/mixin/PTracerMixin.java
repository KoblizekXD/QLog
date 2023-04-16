package com.koblizek.qlog.mixin;

import com.koblizek.qlog.client.ptracer.PTracer;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ClientConnection.class)
public abstract class PTracerMixin {

    @Shadow public abstract void send(Packet<?> packet);

    private static final Logger LOGGER = LoggerFactory.getLogger("PTracer");

    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static void apply(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (PTracer.enabled) {
            if (Arrays.stream(PTracer.filter.getDeterminator()).noneMatch(s -> packet.getClass().getSimpleName().contains(s)))
                return;
            PTracer tracer = PTracer.initialize(packet);
            tracer.sendWithSettings(tracer.parse());
        }
    }
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V",
            at = @At("HEAD"))
    public void send(Packet<?> packet, CallbackInfo ci) {
        if (PTracer.enabled) {
            if (Arrays.stream(PTracer.filter.getDeterminator()).noneMatch(s -> packet.getClass().getSimpleName().contains(s)))
                return;
            PTracer tracer = PTracer.initialize(packet);
            tracer.sendWithSettings(tracer.parse());
        }
    }
}
