package catpouch.pip.client.pings;

import java.util.UUID;

import catpouch.pip.client.Ping;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3f;

public class EntityPing extends Ping {
    private Entity entity;
    private MinecraftClient client = MinecraftClient.getInstance();

    public EntityPing(int entityId, UUID owner) {
        this.entity = client.world.getEntityById(entityId);
        this.owner = owner;
    }

    @Override
    public Vec3f getPos() {
        if(client == null) {
            return new Vec3f(0, 0, 0);
        }
        Vec3f pos = new Vec3f(entity.getCameraPosVec(client.getTickDelta()));
        pos.add(0f, 0.25f, 0f);
        return(pos);
    }
}
