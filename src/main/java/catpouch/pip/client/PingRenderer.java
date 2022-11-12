package catpouch.pip.client;

import catpouch.pip.client.pings.Ping;
import catpouch.pip.client.util.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.*;

public class PingRenderer implements WorldRenderEvents.Last {
    @Override
    public void onLast(WorldRenderContext context) {
        for(Ping ping : PingManager.INSTANCE.getPings()) {
            Vec3f position = ping.getPos();
            if(position == null) {
                PingManager.INSTANCE.removePing(ping.getOwner());
                continue;
            }
            Vec3f projected = RenderUtil.projectPoint(context, new Vector4f(position.getX(), position.getY(), position.getZ(), 1f));
            ping.setProjectedPos(projected);
        }
    }
}