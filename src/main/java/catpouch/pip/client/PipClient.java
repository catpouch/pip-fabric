package catpouch.pip.client;

import catpouch.pip.client.util.RaycastUtil;
import com.mojang.logging.LogUtils;

import catpouch.pip.PipConstants;
import catpouch.pip.client.pings.EntityPing;
import catpouch.pip.client.pings.PosPing;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class PipClient implements ClientModInitializer {
    Logger logger = LogUtils.getLogger();
    
    private void receivePingPacket(MinecraftClient client, PacketByteBuf buf, PipConstants pingType) {
        final UUID uuid = buf.readUuid();

        Ping ping;

        switch (pingType) {
            case POS_PING_PACKET -> {
                final BlockPos pos = buf.readBlockPos();
                ping = new PosPing(pos, uuid);
            }
            case ENTITY_PING_PACKET -> {
                final int entityId = buf.readInt();
                ping = new EntityPing(entityId, uuid);
            }
            default -> ping = null;
        }

        if(client == null || ping == null) {
            return;
        }

        client.execute(() -> PingManager.INSTANCE.addPing(ping));
    }

    @Override
    public void onInitializeClient() {
        KeyBinding pingBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pip.ping",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
                "key.category.pip.test"
        ));

        PingRenderer renderer = new PingRenderer();

        WorldRenderEvents.LAST.register(renderer);
        HudRenderCallback.EVENT.register(new PingHudOverlay());

        for(PipConstants id : PipConstants.values()) {
            ClientPlayNetworking.registerGlobalReceiver(id.id(), (client, handler, buf, responseSender) -> {
                receivePingPacket(client, buf, id);
            });
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(pingBinding.wasPressed()) {
                HitResult hit = RaycastUtil.raycast(client);
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
                        buf.writeUuid(client.player.getUuid());
                        buf.writeBlockPos(blockPos);
                        ClientPlayNetworking.send(PipConstants.POS_PING_PACKET.id(), buf);
                        break;
                    case ENTITY:
                        EntityHitResult entityHit = (EntityHitResult) hit;
                        Entity entity = entityHit.getEntity();
                        buf.writeUuid(client.player.getUuid());
                        buf.writeInt(entity.getId());
                        ClientPlayNetworking.send(PipConstants.ENTITY_PING_PACKET.id(), buf);
                        break;
                }
            }
        });
    }
}
