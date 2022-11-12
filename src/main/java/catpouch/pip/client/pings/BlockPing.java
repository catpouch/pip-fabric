package catpouch.pip.client.pings;

import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

public class BlockPing extends Ping {
    private final BlockPos pos;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public BlockPing(BlockPos pos, UUID owner) {
        this.pos = pos;
        this.owner = owner;
    }

    @Override
    public Vec3f getPos() {
        if(client == null || client.world == null || client.world.getBlockState(pos).isAir()) {
            return null;
        }
        return(new Vec3f(pos.getX() + 0.5f, pos.getY() + 1f, pos.getZ() + 0.5f));
    }

    public BlockPos getBlockPos() { return pos; }
}
