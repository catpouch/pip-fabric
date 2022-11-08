package catpouch.pip;

import catpouch.pip.client.config.PipClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Pip implements ModInitializer {
    private void receivePingPacket(ServerPlayerEntity player, PacketByteBuf buf, PacketSender responseSender, PipConstants pingType) {
        UUID uuid = buf.readUuid();
        PacketByteBuf returnBuf = PacketByteBufs.create();
        returnBuf.writeUuid(uuid);
        switch (pingType) {
            case POS_PING_PACKET -> {
                BlockPos pos = buf.readBlockPos();
                returnBuf.writeBlockPos(pos);
            }
            case ENTITY_PING_PACKET -> {
                int entityId = buf.readInt();
                returnBuf.writeInt(entityId);
            }
        }
        for(ServerPlayerEntity onlinePlayer : PlayerLookup.around((ServerWorld) player.world, player.getPos(), AutoConfig.getConfigHolder(PipClientConfig.class).getConfig().pingReceptionRadius)) {
            ServerPlayNetworking.send(onlinePlayer, pingType.id(), returnBuf);
        }
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(PipClientConfig.class, GsonConfigSerializer::new);
        for(PipConstants id : PipConstants.values()) {
            ServerPlayNetworking.registerGlobalReceiver(id.id(), (server, player, handler, buf, responseSender) -> {
                receivePingPacket(player, buf, responseSender, id);
            });
        }
    }
}
