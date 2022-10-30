package catpouch.pip.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

public class RenderUtil {
    public static void renderPing(WorldRenderContext context) {

        MatrixStack stack = context.matrixStack();
        Camera camera = context.camera();

        //        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //        RenderSystem.setShaderColor(0f, 1f, 0f, 1f);
        //        MatrixStack stack = RenderSystem.getModelViewStack();
        //        stack.loadIdentity();
        //        stack.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0f, 90f, 0f)));
        //        DrawableHelper.fill(stack, 0, 0, 20, 20, 0xFF0000);
        //        stack.push();
        //        RenderSystem.recordRenderCall(() -> {
//        stack.push();
        stack.multiply(camera.getRotation());
        stack.push();
        //        RenderSystem.getModelViewMatrix().multiply(stack.peek().getPositionMatrix());
        final Identifier tex = new Identifier("pip", "icon.png");
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, tex);
        DrawableHelper.drawTexture(stack, 0, 0, 1,0, 0, 10, 10, 10, 10);
        stack.pop();
//        });

//        Matrix4f matrix = RenderSystem.getModelViewMatrix();
//        matrix.invert();
//        DrawableHelper.drawTexture(stack, 0, 0, 1, 0, 0, 10, 10, 10, 10);
//        stack.pop();
//        stack.push();
//        BufferBuilder builder = Tessellator.getInstance().getBuffer();
//        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//        builder.vertex(matrix, 0, 0, -1).texture(0, 0).next();
//        builder.vertex(matrix, 5, 0, -1).texture(0, 0).next();
//        builder.vertex(matrix, 5, 5, -1).texture(0, 0).next();
//        builder.vertex(matrix, 0, 5, -1).texture(0, 0).next();
//        builder.end();
//        BufferRenderer.draw(builder);
//        stack.pop();
    }

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
