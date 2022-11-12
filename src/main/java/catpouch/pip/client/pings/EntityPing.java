package catpouch.pip.client.pings;

import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;

//TODO destroy when entity is removed from world
public class EntityPing extends Ping {
    public final int entityId;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public EntityPing(int entityId, UUID owner) {
        this.entityId = entityId;
        this.owner = owner;
    }

    @Override
    public Vec3f getPos() {
        if(client == null || client.world == null) {
            return new Vec3f(0, 0, 0);
        }
        Entity entity = client.world.getEntityById(entityId);
        if(entity == null) {
            return null;
        }
        Vec3f pos = new Vec3f(entity.getCameraPosVec(client.getTickDelta()));
        pos.add(0f, 0.25f, 0f);
        return(pos);
    }
}
