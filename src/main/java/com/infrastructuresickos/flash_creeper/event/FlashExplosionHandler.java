package com.infrastructuresickos.flash_creeper.event;

import com.infrastructuresickos.flash_creeper.effect.ModEffects;
import com.infrastructuresickos.flash_creeper.network.FlashPacket;
import com.infrastructuresickos.flash_creeper.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Intercepts creeper explosions:
 *  - Cancels all block destruction and entity damage.
 *  - Applies full knockback manually.
 *  - Applies the Flash effect to hit players.
 *  - Sends a packet to rotate the player's view away.
 *  - Prevents flashed players from attacking.
 *
 * Registered manually on the FORGE bus — do NOT add @Mod.EventBusSubscriber.
 */
public class FlashExplosionHandler {

    /** 4 seconds at 20 ticks/sec */
    private static final int FLASH_DURATION_TICKS = 80;

    /** Knockback multiplier — tuned to feel like a flashbang push. */
    private static final double KNOCKBACK_FORCE = 3.5;

    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();

        // Only intercept creeper explosions
        Entity source = explosion.getIndirectSourceEntity();
        if (!(source instanceof Creeper)) return;

        Vec3 center = explosion.getPosition();

        // Capture entities before clearing — we apply our own effects
        List<Entity> affected = new ArrayList<>(event.getAffectedEntities());
        event.getAffectedBlocks().clear();
        event.getAffectedEntities().clear();

        for (Entity entity : affected) {
            if (!(entity instanceof ServerPlayer player)) continue;

            // Manual knockback: push the player away from the blast
            Vec3 delta = player.position().subtract(center);
            double dist = delta.length();
            if (dist > 0) {
                double scale = KNOCKBACK_FORCE * Math.max(0, 1.0 - dist / 8.0);
                Vec3 push = delta.normalize().scale(scale);
                player.push(push.x, 0.35 * scale, push.z);
                player.hurtMarked = true;
            }

            // Apply Flash disorientation effect
            player.addEffect(new MobEffectInstance(ModEffects.FLASH.get(), FLASH_DURATION_TICKS, 0, false, true));

            // Send packet to snap the client view away from the blast
            PacketHandler.sendToPlayer(new FlashPacket(center), player);
        }
    }

    /** Prevent flashed players from landing attacks. */
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity().hasEffect(ModEffects.FLASH.get())) {
            event.setCanceled(true);
        }
    }

    /** Prevent flashed players from receiving additional damage (they are already disoriented). */
    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        // Flash is a disorientation effect, not a damage shield — allow incoming damage.
        // (Nothing to cancel here; method kept for clarity.)
    }
}
