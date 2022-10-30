package catpouch.pip;

import catpouch.pip.client.util.SimpleConfig;
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
    public SimpleConfig CONFIG = SimpleConfig.of("pip").provider(this::configProvider).request();

    private String configProvider(String filename) {
        return "pingRadius=100d";
    }

    public interface PipConstants {
        Identifier PING_PACKET_ID = new Identifier("pip", "ping_packet");
    }

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(PipConstants.PING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            UUID uuid = buf.readUuid();
            PacketByteBuf returnBuf = PacketByteBufs.create();
            returnBuf.writeBlockPos(blockPos);
            returnBuf.writeUuid(uuid);
            for(ServerPlayerEntity onlinePlayer : PlayerLookup.around((ServerWorld) player.world, player.getPos(), CONFIG.getOrDefault("pingRadius", 100d))) {
                ServerPlayNetworking.send(onlinePlayer, PipConstants.PING_PACKET_ID, returnBuf);
            }
        });
    }
}
