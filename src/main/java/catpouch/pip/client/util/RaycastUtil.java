package catpouch.pip.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class RaycastUtil {
    public static HitResult raycast(MinecraftClient client) {
        //TODO add margin so targeting items doesn't suck
        if(client.getCameraEntity() == null) {
            return null;
        }
        float tickDelta = client.getTickDelta();
        Entity camera = client.getCameraEntity();
        Vec3d direction = camera.getRotationVec(tickDelta);
        Vec3d position = camera.getCameraPosVec(tickDelta);
        HitResult blockHitResult = client.getCameraEntity().raycast(10d, tickDelta, true);
        HitResult entityHitResult = ProjectileUtil.raycast(camera, position, position.add(direction.multiply(10d)), camera.getBoundingBox().stretch(direction.multiply(10d)), (a) -> true, 100d);
        if(entityHitResult == null || entityHitResult.squaredDistanceTo(camera) > blockHitResult.squaredDistanceTo(camera)) {
            return blockHitResult;
        } else {
            return entityHitResult;
        }
    }
}
