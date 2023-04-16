package com.koblizek.qlog.client.ptracer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class PTracerScreen extends Screen {
    public PTracerScreen() {
        super(Text.literal("PTracer Options"));
    }

    @Override
    protected void init() {

        this.addDrawableChild(ButtonWidget.builder(PTracer.text,
                        b -> {
                            PTracer.switchStatus();
                            b.setMessage(PTracer.text);
                        })
                .dimensions(this.width / 2 - 50, this.height / 2 - 100, 100, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        Screen.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private ButtonWidget createButton(Text message, Consumer<ButtonWidget> widgetConsumer) {
        return ButtonWidget.builder(message, widgetConsumer::accept).build();
    }
}
