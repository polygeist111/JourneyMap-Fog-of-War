package journeymapfogofwar.network.dispatch;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;

public class ServerNetworkDispatcher
{
    public static void sendChunkInfoPacket(ServerPlayer player, ChunkPos chunkPos)
    {
        //LevelChunk chunk = player.server.getLevel(null)
        LevelChunk chunk = player.level().getChunk(chunkPos.x, chunkPos.z);
        PacketRegistry.REGISTRY.sendTo(new ChunkInfoPacket(JourneymapAdditions.isSlimeChunk(chunk), chunkPos, true), player.connection.connection, PLAY_TO_CLIENT);
    }

}

