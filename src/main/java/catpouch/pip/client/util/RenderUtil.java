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
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

public class RenderUtil {
    public static void renderPing(MatrixStack matrixStack) {
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        RenderSystem.setShaderColor(0f, 1f, 0f, 1f);
        MatrixStack stack = new MatrixStack();
        stack.loadIdentity();
//        DrawableHelper.fill(stack, 0, 0, 20, 20, 0xFF0000);
//        final Identifier tex = new Identifier("pip", "icon.png");
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//        RenderSystem.setShaderTexture(0, tex);
//        DrawableHelper.drawTexture(matrixStack, 0, 0, 0, 0, 10, 10, 10, 10);
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        builder.vertex(stack.peek().getPositionMatrix(), 0f, 0f, 0f).color(0xFF0000).next();
        builder.vertex(stack.peek().getPositionMatrix(), 5f, 0f, 0f).color(0xFF0000).next();
        builder.vertex(stack.peek().getPositionMatrix(), 5f, 5f, 0f).color(0xFF0000).next();
        builder.vertex(stack.peek().getPositionMatrix(), 0f, 5f, 0f).color(0xFF0000).next();
        builder.end();
        BufferRenderer.draw(builder);


    }

    public static Vec3f projectPoint(WorldRenderContext context, Vector4f ping) {
        Matrix4f position = context.matrixStack().peek().getPositionMatrix();
        Matrix4f projection = context.projectionMatrix();
        Window window = MinecraftClient.getInstance().getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        Vec3d cameraPos = context.camera().getPos();
        ping.transform(Matrix4f.translate((float) -cameraPos.x, (float) -cameraPos.y, -(float) cameraPos.z));
        ping.transform(position);
        ping.transform(projection);
        float posW = ping.getW();
        Vec3f winPos = new Vec3f(ping.getX() / posW, (ping.getY() / posW) * -1f, ping.getZ() / posW);
        winPos.add(1f, 1f, 1f);
        winPos.scale(0.5f);
        if(winPos.getX() < 0f || winPos.getX() > 1f || winPos.getY() < 0f || winPos.getY() > 1f || winPos.getZ() > 1f) {
            return null;
        }
        winPos.multiplyComponentwise(width, height, 1);
        return winPos;
    }
}
