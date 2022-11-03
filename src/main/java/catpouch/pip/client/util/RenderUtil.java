package catpouch.pip.client.util;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.*;

public class RenderUtil {
    public static Vec3f projectPoint(WorldRenderContext context, Vector4f ping) {
        Matrix4f position = context.matrixStack().peek().getPositionMatrix();
        Matrix4f projection = context.projectionMatrix();
        Window window = MinecraftClient.getInstance().getWindow();
        int width = window.getWidth();
        int height = window.getHeight();

        Vec3d cameraPos = context.camera().getPos();
        ping.transform(Matrix4f.translate((float) -cameraPos.x, (float) -cameraPos.y, -(float) cameraPos.z));
        ping.transform(position);
        ping.transform(projection);
        float posW = ping.getW();
        Vec3f winPos = new Vec3f(ping.getX() / posW, (ping.getY() / posW) * -1f, ping.getZ() / posW);
        winPos.add(1f, 1f, 1f);
        winPos.scale(0.5f);
        if(winPos.getX() < 0f || winPos.getX() > 1f || winPos.getY() < 0f || winPos.getY() > 1f || winPos.getZ() > 1f) {
            return new Vec3f(-1f, -1f, -1f);
        }
        winPos.multiplyComponentwise(width, height, 1);
        return winPos;
    }
}
