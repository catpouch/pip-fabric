package catpouch.pip.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

public class PingHudOverlay implements HudRenderCallback {
    private final PingProjector projector;

    private static final Identifier tex = new Identifier("pip", "icon.png");

    public PingHudOverlay(PingProjector projector) {
        this.projector = projector;
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Vec3i pos = projector.getWinPos();
//        DrawableHelper.fill(matrixStack, pos.getX(), pos.getY(), pos.getX() + 10, pos.getY() + 10, 50);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, tex);
        DrawableHelper.drawTexture(matrixStack, pos.getX(), pos.getY(), 0, 0, 10, 10, 10, 10);
    }
}
