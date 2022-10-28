package catpouch.pip.client;

import net.minecraft.util.math.BlockPos;

import java.io.Serializable;
import java.util.UUID;

public class Ping implements Serializable {
    private BlockPos position;
    private UUID owner;

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

    public void setPos(BlockPos newPosition) {
        position = newPosition;
    }

    public void setOwner(UUID newOwner) {
        owner = newOwner;
    }
}
