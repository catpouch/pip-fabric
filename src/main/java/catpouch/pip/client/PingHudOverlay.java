package catpouch.pip.client;

import catpouch.pip.client.util.SimpleConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PingHudOverlay implements HudRenderCallback {

    private static final Identifier tex = new Identifier("pip", "ping.png");

    private final SimpleConfig CONFIG = SimpleConfig.of("pip").request();

    public PingHudOverlay() {}

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Collection<Ping> pings = PingManager.INSTANCE.getPings();
        TextRenderer textRenderer = client.textRenderer;
        RenderSystem.setShaderTexture(0, tex);

        boolean useInGameScale = CONFIG.getOrDefault("pingUseInGameGuiScale", false);
        int inGameScale = (int) client.getWindow().getScaleFactor();
        int scale = useInGameScale ? inGameScale : CONFIG.getOrDefault("pingGuiScale", (int) client.getWindow().getScaleFactor());

        matrixStack.push();
        matrixStack.scale(1f/inGameScale, 1f/inGameScale, 1f);
        for(Ping ping : pings) {
            final int width = 24 * scale;
            final int height = 12 * scale;
            final int posX = (int) ping.getProjectedPos().getX();
            final int posY = (int) ping.getProjectedPos().getY();
            final int x = posX - width / 2;
            final int y = posY - height;
            DrawableHelper.drawTexture(matrixStack, x, y, -91, 0, 0, width, height, width, height); //-91 is the depth below all other hud elements
            matrixStack.scale(scale, scale, 1f);
            World world = client.world;
            String playerName = "";
            if(world != null) {
                PlayerEntity player = world.getPlayerByUuid(ping.getOwner());
                if(player != null) {
                    playerName = player.getDisplayName().asString();
                }
            }
            float textX = (float) posX / scale - (float) (textRenderer.getWidth(playerName) / 2);
            float textY = ((float) posY / scale) - 12 - textRenderer.fontHeight;
            if(scale == 1) {
                textX = Math.round(textX);
                textY = Math.round(textY);
            }
            textRenderer.draw(matrixStack, playerName, textX, textY, 0xFFFFFF);
        }
        matrixStack.scale((float) inGameScale/scale, (float) inGameScale/scale, 1f);
        matrixStack.pop();
    }
}
