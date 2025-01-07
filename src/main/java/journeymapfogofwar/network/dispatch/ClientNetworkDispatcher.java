package journeymapfogofwar.network.dispatch;

import java.util.HashMap;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;
import journeymapfogofwar.network.packet.MapSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;

public class ClientNetworkDispatcher
{
    public static void sendChunkInfoRequest(ChunkPos chunkPos)
    {
        // might be null if player is not fully logged in.
        if (Minecraft.getInstance().getConnection() != null)
        {
            JourneymapAdditions.getLogger().info("Receiving packet info request");
            PacketRegistry.REGISTRY.sendToServer(new ChunkInfoPacket(true, chunkPos, false));
        }
    }

    public static void sendMapSyncRequest() {
        // might be null if player is not fully logged in.
        if (Minecraft.getInstance().getConnection() != null) {
            JourneymapAdditions.getLogger().info("Receiving map sync request");
            PacketRegistry.REGISTRY.sendToServer(new MapSyncPacket(new HashMap<ChunkPos, String>()));
        }
    }
}
