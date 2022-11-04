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
    private void receivePingPacket(ServerPlayerEntity player, PacketByteBuf buf, PacketSender responseSender, boolean isEntity) {
        UUID uuid = buf.readUuid();
        PacketByteBuf returnBuf = PacketByteBufs.create();
        returnBuf.writeUuid(uuid);
        if(isEntity) {
            int entityId = buf.readInt();
            returnBuf.writeInt(entityId);
        } else {
            BlockPos pos = buf.readBlockPos();
            returnBuf.writeBlockPos(pos);
        }
        for(ServerPlayerEntity onlinePlayer : PlayerLookup.around((ServerWorld) player.world, player.getPos(), AutoConfig.getConfigHolder(PipClientConfig.class).getConfig().pingRadius)) {
            ServerPlayNetworking.send(onlinePlayer, isEntity ? PipConstants.ENTITY_PING_PACKET_ID : PipConstants.POS_PING_PACKET_ID, returnBuf);
        }
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(PipClientConfig.class, GsonConfigSerializer::new);
        ServerPlayNetworking.registerGlobalReceiver(PipConstants.POS_PING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            receivePingPacket(player, buf, responseSender, false);
        });
        ServerPlayNetworking.registerGlobalReceiver(PipConstants.ENTITY_PING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            receivePingPacket(player, buf, responseSender, true);
        });
    }
}
