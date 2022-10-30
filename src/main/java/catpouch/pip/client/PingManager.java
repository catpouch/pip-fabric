package catpouch.pip.client;

import catpouch.pip.client.Ping;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public enum PingManager {
    INSTANCE(CacheBuilder.newBuilder().expireAfterWrite(100L, TimeUnit.SECONDS).build());

    private final Cache<UUID, Ping> pings;
//    private final List<Vec3f> projectedPositions;

    PingManager(Cache<UUID, Ping> cache) {
        this.pings = cache;
//        this.projectedPositions = new ArrayList<>();
    }

    public void addPing(Ping ping) {
        pings.put(ping.getOwner(), ping);
    }

//    public void addProjectedPos(Vec3f pos) {
//        projectedPositions.add(pos);
//    }
//
//    public void clearProjectedPositions() {
//        projectedPositions.clear();
//    }

    public Collection<Ping> getPings() {
        return pings.asMap().values();
    }

    public Ping getPingFromUuid(UUID uuid) {
        return pings.asMap().get(uuid);
    }

//    public List<Vec3f> getProjectedPositions() {
//        return projectedPositions;
//    }
}
