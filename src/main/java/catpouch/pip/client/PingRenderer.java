package catpouch.pip.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.*;

import java.util.*;

public class PingRenderer implements WorldRenderEvents.Last {

    private List<Vec3f> projectedPositions;

    private final Map<UUID, Vector4f> pings = new HashMap<UUID, Vector4f>();

    public List<Vec3f> getProjectedPositions() {
//        return new Vec3i((int) winPos.getX(), (int) winPos.getY(), (int) winPos.getZ());
        return projectedPositions;
    }

    public void addPing(BlockPos pos, UUID uuid) {
        System.out.println(pos.toShortString());
        System.out.println(uuid.toString());
        pings.put(uuid, new Vector4f(pos.getX(), pos.getY(), pos.getZ(), 1f));
    }

    public void removePing(UUID uuid) {
        System.out.println("pign removed");
        System.out.println(uuid.toString());
        pings.remove(uuid);
    }

    @Override
    public void onLast(WorldRenderContext context) {

//        System.out.println(pings.toString());

        projectedPositions = new ArrayList<Vec3f>();
        Matrix4f position = context.matrixStack().peek().getPositionMatrix();
        Matrix4f projection = context.projectionMatrix();
        Window window = MinecraftClient.getInstance().getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        for(Vector4f ping : pings.values()) {
            Vec3d cameraPos = context.camera().getPos();
            ping.transform(Matrix4f.translate((float) -cameraPos.x, (float) -cameraPos.y, -(float) cameraPos.z));
            ping.transform(position);
            ping.transform(projection);
            float posW = ping.getW();
            Vec3f winPos = new Vec3f(ping.getX() / posW, (ping.getY() / posW) * -1f, ping.getZ() / posW);
            winPos.add(1f, 1f, 1f);
            winPos.scale(0.5f);
            winPos.multiplyComponentwise(width, height, 1);
            projectedPositions.add(winPos);
        }
    }
}