package com.infrastructuresickos.flash_creeper.effect;

import com.infrastructuresickos.flash_creeper.FlashCreeper;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FlashCreeper.MOD_ID);

    public static final RegistryObject<MobEffect> FLASH =
        EFFECTS.register("flash", FlashEffect::new);
}
