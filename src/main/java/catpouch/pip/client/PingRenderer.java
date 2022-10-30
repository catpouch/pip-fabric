package catpouch.pip.client;

import catpouch.pip.client.util.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.*;

import java.util.*;

public class PingRenderer implements WorldRenderEvents.Last {

    private final PingManager manager;

    public PingRenderer(PingManager manager) {
        this.manager = manager;
    }

    private List<Vec3f> projectedPositions;

    public List<Vec3f> getProjectedPositions() {
        return projectedPositions;
    }

    @Override
    public void onLast(WorldRenderContext context) {
        projectedPositions = new ArrayList<>();
        for(Ping ping : manager.getPings()) {
            BlockPos position = ping.getPos();
            Vec3f projected = RenderUtil.projectPoint(context, new Vector4f(position.getX() + 0.5f, position.getY() + 0.5f, position.getZ() + 0.5f, 1f));
            if(projected != null) {
                projectedPositions.add(projected);
            }
        }
    }
}