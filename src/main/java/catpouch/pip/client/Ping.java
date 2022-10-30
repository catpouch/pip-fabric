package catpouch.pip.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

import java.io.Serializable;
import java.util.UUID;

public class Ping implements Serializable {
    private BlockPos position;
    private UUID owner;
    private Vec3f projectedPos;

    public Ping(BlockPos position, UUID owner) {
        this.owner = owner;
        this.position = position;
    }

    public BlockPos getPos() {
        return position;
    }

    public UUID getOwner() {
        return owner;
    }

    public Vec3f getProjectedPos() { return projectedPos; }

    public void setPos(BlockPos newPosition) {
        position = newPosition;
    }

    public void setOwner(UUID newOwner) {
        owner = newOwner;
    }

    public void setProjectedPos(Vec3f newProjectedPos) { projectedPos = newProjectedPos; }
}
