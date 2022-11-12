package catpouch.pip.client;

import catpouch.pip.client.config.PipClientConfig;
import catpouch.pip.client.pings.Ping;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.shedaniel.autoconfig.AutoConfig;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public enum PingManager {
    INSTANCE(CacheBuilder.newBuilder().expireAfterWrite(AutoConfig.getConfigHolder(PipClientConfig.class).get().pingExpirationDelay, TimeUnit.SECONDS).build());

    private final Cache<UUID, Ping> pings;

    PingManager(Cache<UUID, Ping> cache) {
        this.pings = cache;
    }

    public void addPing(Ping ping) {
        pings.put(ping.getOwner(), ping);
    }

    public void removePing(UUID uuid) { pings.invalidate(uuid); }

    public Collection<Ping> getPings() {
        return pings.asMap().values();
    }

    public Ping getPingFromUuid(UUID uuid) {
        return pings.asMap().get(uuid);
    }
}
