package catpouch.pip.client.pings;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

public class PosPing extends Ping {
    private final BlockPos pos;

    public PosPing(BlockPos pos, UUID owner) {
        this.pos = pos;
        this.owner = owner;
    }

    @Override
    public Vec3f getPos() {
        return(new Vec3f(pos.getX() + 0.5f, pos.getY() + 1f, pos.getZ() + 0.5f));
    }
}
