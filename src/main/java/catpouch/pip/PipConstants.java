package catpouch.pip;

import net.minecraft.util.Identifier;

public enum PipConstants {
    POS_PING_PACKET(new Identifier("pip", "ping_pos_packet")),
    ENTITY_PING_PACKET(new Identifier("pip", "ping_entity_packet")),
    BLOCK_PING_PACKET(new Identifier("pip", "ping_block_packet"));
    private final Identifier id;
    PipConstants(Identifier id) {
        this.id = id;
    }
    public Identifier id() { return id; }
}
