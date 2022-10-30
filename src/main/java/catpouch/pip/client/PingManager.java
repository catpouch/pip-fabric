package catpouch.pip.client;

import catpouch.pip.client.Ping;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public enum PingManager {
    INSTANCE(CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build());

    private final Cache<UUID, Ping> pings;

    PingManager(Cache<UUID, Ping> cache) {
        this.pings = cache;
    }

    public PingManager getInstance() {
        return INSTANCE;
    }

    public void addPing(Ping ping) {
        pings.put(ping.getOwner(), ping);
    }

    public Collection<Ping> getPings() {
        return pings.asMap().values();
    }

    public Ping getPingFromUuid(UUID uuid) {
        return pings.asMap().get(uuid);
    }
}