package com.infrastructuresickos.flash_creeper.client;

import com.infrastructuresickos.flash_creeper.FlashCreeper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-side MOD bus subscriber — registers the white flash overlay.
 */
@Mod.EventBusSubscriber(modid = FlashCreeper.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlashClientSetup {

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("flash_overlay", new FlashOverlay());
    }
}
