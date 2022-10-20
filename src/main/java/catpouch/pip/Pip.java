package catpouch.pip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Pip implements ModInitializer {
    public interface PipConstants {
        Identifier PING_PACKET_ID = new Identifier("pip", "ping_packet");
    }

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(PipConstants.PING_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            System.out.println(blockPos.toShortString());
            PacketByteBuf returnBuf = PacketByteBufs.create();
            returnBuf.writeBlockPos(blockPos);
            for(ServerPlayerEntity onlinePlayer : PlayerLookup.all(server)) {
                System.out.println(onlinePlayer.getName());
                ServerPlayNetworking.send(onlinePlayer, PipConstants.PING_PACKET_ID, returnBuf);
            }
        });
    }
}
