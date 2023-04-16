package com.koblizek.qlog.mixin;

import com.koblizek.qlog.client.ptracer.PTracerScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("HEAD"))
    private void initGui(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("PTracer"), button -> this.client.setScreen(new PTracerScreen())).dimensions(1, 1, 100, 20).build());
    }
}
