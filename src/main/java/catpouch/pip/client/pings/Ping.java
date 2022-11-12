package catpouch.pip.client.pings;

import net.minecraft.util.math.Vec3f;

import java.util.UUID;

public class Ping {
    protected UUID owner;
    private Vec3f projectedPos;

    public Vec3f getPos() { return null; }

    public UUID getOwner() {
        return owner;
    }

    public Vec3f getProjectedPos() { return projectedPos; }

    public void setProjectedPos(Vec3f newProjectedPos) { projectedPos = newProjectedPos; }
}
