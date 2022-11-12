package catpouch.pip.mixin;

import catpouch.pip.client.pings.Ping;
import catpouch.pip.client.PingManager;
import catpouch.pip.client.pings.EntityPing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        for(Ping ping : PingManager.INSTANCE.getPings()) {
            if(ping instanceof EntityPing && ((EntityPing) ping).entityId == entity.getId()) {
                cir.setReturnValue(true);
                break;
            }
        }
    }
}
