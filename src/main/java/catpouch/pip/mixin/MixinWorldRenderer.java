package catpouch.pip.mixin;

import catpouch.pip.client.pings.Ping;
import catpouch.pip.client.PingManager;
import catpouch.pip.client.pings.EntityPing;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if(vertexConsumers instanceof OutlineVertexConsumerProvider consumerProvider) {
            for(Ping ping : PingManager.INSTANCE.getPings()) {
                if(ping instanceof EntityPing && ((EntityPing) ping).entityId == entity.getId()) {
                    consumerProvider.setColor(136, 146, 201, 255);
                    break;
                }
            }
        }
    }
}
