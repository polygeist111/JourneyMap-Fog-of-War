package journeymapfogofwar.network.dispatch;

import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;

public class ClientNetworkDispatcher
{
    public static void sendChunkInfoRequest(ChunkPos chunkPos)
    {
        // might be null if player is not fully logged in.
        if (Minecraft.getInstance().getConnection() != null)
        {
            PacketRegistry.REGISTRY.sendToServer(new ChunkInfoPacket(true, chunkPos, false));
        }
    }
}
