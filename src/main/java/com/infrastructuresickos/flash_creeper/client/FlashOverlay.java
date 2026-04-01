package com.infrastructuresickos.flash_creeper.client;

import com.infrastructuresickos.flash_creeper.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * Client-side white screen overlay.
 * Alpha is proportional to remaining Flash duration — bright at onset, fades as it wears off.
 */
public class FlashOverlay implements IGuiOverlay {

    /** Must match FlashExplosionHandler.FLASH_DURATION_TICKS */
    private static final float TOTAL_TICKS = 80.0f;

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        MobEffectInstance effect = mc.player.getEffect(ModEffects.FLASH.get());
        if (effect == null) return;

        // Fade from fully white at max duration to transparent as it expires
        float progress = (effect.getDuration() + partialTick) / TOTAL_TICKS;
        float alpha = Math.min(1.0f, progress);

        int a = (int) (alpha * 230); // keep slightly below 255 so very dim at peak
        if (a <= 0) return;

        graphics.fill(0, 0, width, height, (a << 24) | 0x00FFFFFF);
    }
}
