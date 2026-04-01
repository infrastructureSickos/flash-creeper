package com.infrastructuresickos.flash_creeper.client;

import com.infrastructuresickos.flash_creeper.FlashCreeper;
import com.infrastructuresickos.flash_creeper.effect.ModEffects;
import com.infrastructuresickos.flash_creeper.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-side FORGE bus subscriber — mutes all non-ringing sounds while Flash is active.
 * During the second half of the effect duration, sounds are gradually allowed back.
 */
@Mod.EventBusSubscriber(modid = FlashCreeper.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlashSoundHandler {

    private static final float TOTAL_TICKS = 80.0f;

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        var effect = mc.player.getEffect(ModEffects.FLASH.get());
        if (effect == null) return;

        // Let the ringing sound through unconditionally
        SoundInstance sound = event.getSound();
        if (sound != null) {
            String loc = sound.getLocation().toString();
            if (loc.equals(FlashCreeper.MOD_ID + ":flash_ringing")) return;
        }

        // During the first 60% of the effect, fully suppress all other sounds.
        // During the remaining 40%, allow them back (deafness fades).
        float progress = effect.getDuration() / TOTAL_TICKS;
        if (progress > 0.4f) {
            event.setSound(null); // suppress
        }
        // else: allow sound through as deafness fades
    }
}
