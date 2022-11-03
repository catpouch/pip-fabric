package catpouch.pip;

import catpouch.pip.client.config.PipClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Pip implements ModInitializer {
    public interface PipConstants {
        Identifier PING_PACKET_ID = new Identifier("pip", "ping_packet");
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(PipClientConfig.class, GsonConfigSerializer::new);
        final PipClientConfig CONFIG = AutoConfig.getConfigHolder(PipClientConfig.class).getConfig();

        ServerPlayNetworking.registerGlobalReceiver(PipConstants.PING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            UUID uuid = buf.readUuid();
            PacketByteBuf returnBuf = PacketByteBufs.create();
            returnBuf.writeBlockPos(blockPos);
            returnBuf.writeUuid(uuid);
            for(ServerPlayerEntity onlinePlayer : PlayerLookup.around((ServerWorld) player.world, player.getPos(), CONFIG.pingRadius)) {
                ServerPlayNetworking.send(onlinePlayer, PipConstants.PING_PACKET_ID, returnBuf);
            }
        });
    }
}
