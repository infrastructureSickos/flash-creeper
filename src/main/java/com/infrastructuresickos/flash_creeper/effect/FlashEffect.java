package com.infrastructuresickos.flash_creeper.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class FlashEffect extends MobEffect {

    // Reduces movement speed by 50%
    private static final UUID SPEED_UUID = UUID.fromString("8a9a5e93-5b47-4b68-a28c-1f9d2f3a4b5c");

    public FlashEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
        this.addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            SPEED_UUID.toString(),
            -0.5,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
