package catpouch.pip.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

//TODO this isn't client only, also add server-only config
@Config(name = "pip")
public class PipClientConfig implements ConfigData {

    public boolean useInGameGuiScale = false;

    @ConfigEntry.BoundedDiscrete(min = 1L, max = 6L)
    public int pingGuiScale = 2;

    public long pingRadius = 100L;

    public long pingExpirationDelay = 15L;
}
