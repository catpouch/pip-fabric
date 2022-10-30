package catpouch.pip.client;

import catpouch.pip.client.util.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.*;

public class PingRenderer implements WorldRenderEvents.Last {
    @Override
    public void onLast(WorldRenderContext context) {
//        PingManager.INSTANCE.clearProjectedPositions();

        for(Ping ping : PingManager.INSTANCE.getPings()) {
            BlockPos position = ping.getPos();
            Vec3f projected = RenderUtil.projectPoint(context, new Vector4f(position.getX() + 0.5f, position.getY() + 1f, position.getZ() + 0.5f, 1f));
            ping.setProjectedPos(projected);
        }
    }
}