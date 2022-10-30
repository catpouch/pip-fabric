package catpouch.pip.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.List;

public class PingHudOverlay implements HudRenderCallback {
    //TODO move all of this code into a different render call

    private final PingRenderer projector;

    private static final Identifier tex = new Identifier("pip", "ping.png");

    public PingHudOverlay(PingRenderer projector) {
        this.projector = projector;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        List<Vec3f> positions = projector.getProjectedPositions();

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, tex);

        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();

        matrixStack.push();
        matrixStack.scale(1f/scale, 1f/scale, 1f);
        for(Vec3f pos : positions) {
            final int width = 24 * scale;
            final int height = 12 * scale;
            final int posX = (int) pos.getX();
            final int posY = (int) pos.getY();
            final int textY = posY - 15 * scale;
            final int x = posX - width / 2;
            final int y = posY - height;
            DrawableHelper.drawTexture(matrixStack, x, y, -91, 0, 0, width, height, width, height); //-91 is the depth below all other hud elements
            DrawableHelper.drawCenteredText(matrixStack, textRenderer, "balls", posX, textY, 0xFFFFFF);
        }
        matrixStack.pop();
    }
}
