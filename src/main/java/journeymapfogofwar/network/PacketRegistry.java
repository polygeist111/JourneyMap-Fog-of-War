package journeymapfogofwar.network;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.packet.ChunkInfoPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketRegistry
{
    private static PacketRegistry INSTANCE;
    public static SimpleChannel REGISTRY;
    private static final String MOD_VERSION = "0.0.1";

    public static void init()
    {
        JourneymapAdditions.getLogger().debug("Registering internal Network handler.");
        INSTANCE = new PacketRegistry();
        REGISTRY = net.minecraftforge.network.NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(JourneymapAdditions.MOD_ID, "common"))
                .clientAcceptedVersions((c)-> true)
                .serverAcceptedVersions((s)-> true)
                .networkProtocolVersion(() -> MOD_VERSION)
                .simpleChannel();

        REGISTRY.registerMessage(0, ChunkInfoPacket.class, ChunkInfoPacket::encode, ChunkInfoPacket::new, ChunkInfoPacket::handle);
    }

    public static PacketRegistry getInstance()
    {
        if (INSTANCE != null)
        {
            return INSTANCE;
        }
        else
        {
            JourneymapAdditions.getLogger().error("Packet Handler not initialized before use.");
            throw new UnsupportedOperationException("Packet Handler not Initialized");
        }
    }
}
