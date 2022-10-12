package net.combatspells.client;

import net.combatspells.CombatSpells;
import net.combatspells.client.animation.AnimatablePlayer;
import net.combatspells.internals.SpellRegistry;
import net.combatspells.network.Packets;
import net.combatspells.utils.ParticleHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class ClientNetwork {
    public static void initializeHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.ConfigSync.ID, (client, handler, buf, responseSender) -> {
            var config = Packets.ConfigSync.read(buf);
            CombatSpells.config = config;
        });

        ClientPlayNetworking.registerGlobalReceiver(Packets.SpellRegistrySync.ID, (client, handler, buf, responseSender) -> {
            SpellRegistry.decodeContent(buf);
        });

        ClientPlayNetworking.registerGlobalReceiver(Packets.ParticleBatches.ID, (client, handler, buf, responseSender) -> {
            var packet = Packets.ParticleBatches.read(buf);
            var source = client.world.getEntityById(packet.sourceEntityId());
            var instructions = ParticleHelper.convertToInstructions(source,0, 0, packet.batches());
            client.execute(() -> {
                for(var instruction: instructions) {
                    instruction.perform(client.world);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(Packets.SpellAnimation.ID, (client, handler, buf, responseSender) -> {
            var packet = Packets.SpellAnimation.read(buf);
            client.execute(() -> {
                var entity = client.world.getEntityById(packet.playerId());
                if (entity instanceof PlayerEntity player) {
                    ((AnimatablePlayer)player).playAnimation(packet.type(), packet.name());
                }
            });
        });
    }
}
