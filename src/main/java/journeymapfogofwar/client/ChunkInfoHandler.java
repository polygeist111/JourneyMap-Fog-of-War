package journeymapfogofwar.client;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.integration.FogOfWarOverlayHandler;
import journeymapfogofwar.client.integration.SlimeChunkOverlayHandler;
import journeymapfogofwar.network.packet.ChunkInfoPacket;
import journeymapfogofwar.network.packet.MapSyncPacket;
import net.minecraft.world.level.ChunkPos;

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
            //handle unexplored chunks as called from server
            if (packet.isRevealed()) {
                FogOfWarOverlayHandler.getInstance().remove(packet.getChunkPos(), packet.getDiscoverer());
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error(t.getMessage(), t);

        }
    }

    public static void handle(MapSyncPacket packet) {
        try {
            FogOfWarOverlayHandler handler = FogOfWarOverlayHandler.getInstance();
            //handler.clear();
            //assumes square area
            int chunkRadius = 314;
            for (int i = -1 * (chunkRadius - 1); i < (chunkRadius - 1); i++) {
                for (int j = -1 * (chunkRadius - 1); j < (chunkRadius - 1); j++) {
                    handler.addChunk(new ChunkPos(i, j));
                }
            }
            for (ChunkPos key : packet.getMap().keySet()) {
                handler.remove(key, "");
            }
        } catch (Throwable t) {
            JourneymapAdditions.getLogger().error(t.getMessage(), t);
        }
    }
}
