package com.infrastructuresickos.flash_creeper.sound;

import com.infrastructuresickos.flash_creeper.FlashCreeper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FlashCreeper.MOD_ID);

    public static final RegistryObject<SoundEvent> FLASH_RINGING =
        SOUNDS.register("flash_ringing",
            () -> SoundEvent.createVariableRangeEvent(
                new ResourceLocation(FlashCreeper.MOD_ID, "flash_ringing")));
}
