package catpouch.pip.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

@Environment(EnvType.CLIENT)
public class PipClient implements ClientModInitializer {
    public interface PipClientConstants {
        Identifier PING_PACKET_ID = new Identifier("pip", "ping_packet");
    }


    @Override
    public void onInitializeClient() {
        KeyBinding pingBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pip.ping",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
                "key.category.pip.test"
        ));

        ClientPlayNetworking.registerGlobalReceiver(PipClientConstants.PING_PACKET_ID, (client, handler, buf, responseSender) -> {
//            client.player.sendMessage(new LiteralText(buf.readBlockPos().toShortString()), false);
            BlockPos target = buf.readBlockPos();
            String message = target.toShortString();

            client.execute(() -> {
                client.player.sendMessage(new LiteralText(message), false);
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(pingBinding.wasPressed()) {
                HitResult hit = client.crosshairTarget;
                PacketByteBuf buf = PacketByteBufs.create();
                switch(hit.getType()) {
                    case MISS:
                        break;
                    case BLOCK:
                        BlockHitResult blockHit = (BlockHitResult) hit;
                        BlockPos blockPos = blockHit.getBlockPos();
                        buf.writeBlockPos(blockPos);
                        ClientPlayNetworking.send(PipClientConstants.PING_PACKET_ID, buf);
                        break;
                    case ENTITY:
                        EntityHitResult entityHit = (EntityHitResult) hit;
                        BlockPos entityPos = entityHit.getEntity().getBlockPos();
                        buf.writeBlockPos(entityPos);
                        ClientPlayNetworking.send(PipClientConstants.PING_PACKET_ID, buf);
                }
            }
        });
    }
}
