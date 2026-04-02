package com.infrastructuresickos.flash_creeper.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server → Client packet sent when a creeper explosion flashes nearby players.
 * On the client, snaps the player's view to face directly away from the blast.
 */
public class FlashPacket {

    private final double x;
    private final double y;
    private final double z;

    public FlashPacket(Vec3 center) {
        this.x = center.x;
        this.y = center.y;
        this.z = center.z;
    }

    /** Decode constructor — called by the network registry. */
    public FlashPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            // Snap the player's view away from the blast center
            Vec3 blastPos = new Vec3(x, y, z);
            Vec3 playerPos = mc.player.position();
            Vec3 away = playerPos.subtract(blastPos);

            if (away.lengthSqr() < 1e-6) return;

            double dx = away.x;
            double dz = away.z;

            // Yaw: atan2 gives the angle in the xz-plane pointing away from blast
            float newYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));

            mc.player.setYRot(newYaw);
            mc.player.yRotO = newYaw;
            if (mc.player.yHeadRot != newYaw) {
                mc.player.yHeadRot = newYaw;
            }
        });
        ctx.setPacketHandled(true);
    }
}
