package catpouch.pip.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class PipClient implements ClientModInitializer {
    Logger logger = LogUtils.getLogger();

    public interface PipClientConstants {
        Identifier PING_PACKET_ID = new Identifier("pip", "ping_packet");
    }

//    private byte[] serializePing(Ping ping) throws IOException {
//        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//            ObjectOutputStream out = new ObjectOutputStream(bos);
//            out.writeObject(ping);
//            out.flush();
//            return bos.toByteArray();
//        }
//    }

    @Override
    public void onInitializeClient() {
        KeyBinding pingBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pip.ping",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
                "key.category.pip.test"
        ));

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        PingRenderer renderer = new PingRenderer();

        WorldRenderEvents.LAST.register(renderer);
        HudRenderCallback.EVENT.register(new PingHudOverlay(renderer));

        ClientPlayNetworking.registerGlobalReceiver(PipClientConstants.PING_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            UUID uuid = buf.readUuid();

            client.execute(() -> {
                if(client.player != null) {
                    renderer.addPing(pos, uuid);
                    //TODO: previous task is still scheduled even when new ping is placed. cancel previous task. also fix the weird stuff with the ping changing position
                    executorService.schedule(() -> renderer.removePing(uuid), 10, TimeUnit.SECONDS);
                } else {
                    logger.warn("Client is missing player instance!");
                }
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(pingBinding.wasPressed()) {
                HitResult hit = client.crosshairTarget;
                if(client.player == null || hit == null) {
                    continue;
                }
                PacketByteBuf buf = PacketByteBufs.create();
                switch(hit.getType()) {
                    case MISS:
                        break;
                    case BLOCK:
                        BlockHitResult blockHit = (BlockHitResult) hit;
                        BlockPos blockPos = blockHit.getBlockPos();
                        buf.writeBlockPos(blockPos);
                        buf.writeUuid(client.player.getUuid());
                        ClientPlayNetworking.send(PipClientConstants.PING_PACKET_ID, buf);
                        break;
                    case ENTITY:
                        EntityHitResult entityHit = (EntityHitResult) hit;
                        BlockPos entityPos = entityHit.getEntity().getBlockPos();
                        buf.writeBlockPos(entityPos);
                        buf.writeUuid(client.player.getUuid());
                        ClientPlayNetworking.send(PipClientConstants.PING_PACKET_ID, buf);
                        break;
                }
            }
        });
    }
}
