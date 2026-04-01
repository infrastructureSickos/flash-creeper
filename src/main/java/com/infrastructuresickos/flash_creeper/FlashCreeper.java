package com.infrastructuresickos.flash_creeper;

import com.infrastructuresickos.flash_creeper.effect.ModEffects;
import com.infrastructuresickos.flash_creeper.event.FlashExplosionHandler;
import com.infrastructuresickos.flash_creeper.network.PacketHandler;
import com.infrastructuresickos.flash_creeper.sound.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FlashCreeper.MOD_ID)
public class FlashCreeper {

    public static final String MOD_ID = "flash_creeper";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public FlashCreeper() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEffects.EFFECTS.register(modBus);
        ModSounds.SOUNDS.register(modBus);

        PacketHandler.register();

        MinecraftForge.EVENT_BUS.register(new FlashExplosionHandler());

        LOGGER.info("FlashCreeper initialized");
    }
}
