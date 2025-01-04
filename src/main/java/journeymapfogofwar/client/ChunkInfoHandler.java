package journeymapfogofwar.client;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.integration.SlimeChunkOverlayHandler;
import journeymapfogofwar.network.packet.ChunkInfoPacket;

public class ChunkInfoHandler
{

    public static void handle(ChunkInfoPacket packet)
    {
        try
        {
            // handle slimechunks as called from server
            if (packet.isSlimeChunk())
            {
                SlimeChunkOverlayHandler.getInstance().addChunk(packet.getChunkPos());
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error(t.getMessage(), t);

        }
    }
}
