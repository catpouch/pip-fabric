package catpouch.pip.client;

import catpouch.pip.client.config.PipClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Collection;

public class PingHudOverlay implements HudRenderCallback {

    private static final Identifier tex = new Identifier("pip", "ping.png");

    private final PipClientConfig CONFIG = AutoConfig.getConfigHolder(PipClientConfig.class).getConfig();

    public PingHudOverlay() {}

    //TODO attempt to move this somewhere else (BEFORE_DEBUG?)
    //TODO make this ping class specific and add custom outline renderers for item + entity + block pings
    //TODO add player-specific colors?
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Collection<Ping> pings = PingManager.INSTANCE.getPings();
        TextRenderer textRenderer = client.textRenderer;
        RenderSystem.setShaderTexture(0, tex);

        boolean useInGameScale = CONFIG.useInGameGuiScale;
        int inGameScale = (int) client.getWindow().getScaleFactor();
        int scale = useInGameScale ? inGameScale : CONFIG.pingGuiScale;

        matrixStack.push();
        matrixStack.scale(1f/inGameScale, 1f/inGameScale, 1f);
        matrixStack.translate(0d, 0d, -91d);
        for(Ping ping : pings) {
            final int width = 24 * scale;
            final int height = 12 * scale;
            final int posX = (int) ping.getProjectedPos().getX();
            final int posY = (int) ping.getProjectedPos().getY();
            final int x = posX - width / 2;
            final int y = posY - height;
            DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, 0, width, height, width, height);
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
            textRenderer.drawWithShadow(matrixStack, playerName, textX, textY, 0xFFFFFF);
        }
        matrixStack.translate(0d, 0d, 91d);
        matrixStack.scale((float) inGameScale/scale, (float) inGameScale/scale, 1f);
        matrixStack.pop();
    }
}
