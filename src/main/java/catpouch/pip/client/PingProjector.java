package catpouch.pip.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.*;

public class PingProjector implements WorldRenderEvents.Last {

    private Vec3f winPos;

    public Vec3i getWinPos() {
        return new Vec3i((int) winPos.getX(), (int) winPos.getY(), (int) winPos.getZ());
    }

    @Override
    public void onLast(WorldRenderContext context) {

        Matrix4f position = context.matrixStack().peek().getPositionMatrix();
        Matrix4f projection = context.projectionMatrix();
        Window window = MinecraftClient.getInstance().getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        Vector4f[] pos = {new Vector4f(2f, 1f, -3f, 1f)};

        for(Vector4f ping : pos) {
            Vec3d cameraPos = context.camera().getPos();
            ping.transform(Matrix4f.translate((float) -cameraPos.x, (float) -cameraPos.y, -(float) cameraPos.z));
            ping.transform(position);
            ping.transform(projection);
            float posW = ping.getW();
            winPos = new Vec3f(ping.getX() / posW, (ping.getY() / posW) * -1f, ping.getZ() / posW);
            winPos.add(1f, 1f, 1f);
            winPos.scale(0.5f);
            winPos.multiplyComponentwise(width, height, 1);
        }
    }
}