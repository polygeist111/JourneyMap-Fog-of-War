package journeymapfogofwar.network.dispatch;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.monitoring.jmx.MinecraftServerStatistics;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.targets.FMLServerLaunchHandler;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.HashMap;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.SavedExplorationMap;
import journeymapfogofwar.network.packet.*;

public class ServerNetworkDispatcher
{
    private static MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    public static void sendChunkInfoPacket(ServerPlayer player, ChunkPos chunkPos)
    {
        //LevelChunk chunk = player.server.getLevel(null)
        SavedExplorationMap.accessMap(server);
        CompoundTag tag = new CompoundTag();
        SavedExplorationMap map = SavedExplorationMap.load(tag);

        LevelChunk chunk = player.level().getChunk(chunkPos.x, chunkPos.z);
        ChunkPos playerPos = player.chunkPosition();
        //distance to check
        Boolean isRevealed = map.isExplored(chunkPos);
        if (!isRevealed) {
            if (Math.sqrt(Math.pow(chunkPos.x - playerPos.x, 2) + Math.pow(chunkPos.z - playerPos.z, 2)) <= 6) {
                map.exploreChunk(chunkPos, player);
                isRevealed = true;
                //send updates to map to all players
                PacketRegistry.REGISTRY.send(PacketDistributor.ALL.noArg(), new ChunkInfoPacket(JourneymapAdditions.isSlimeChunk(chunk), chunkPos, isRevealed));
            }
        } else {
            //send existing slime chunks only to caller player
            PacketRegistry.REGISTRY.sendTo(new ChunkInfoPacket(JourneymapAdditions.isSlimeChunk(chunk), chunkPos, isRevealed), player.connection.connection, PLAY_TO_CLIENT);
        }
    }

    //Sync caller player map
    public static void sendMapSyncPacket(ServerPlayer player) {
        SavedExplorationMap.accessMap(server);
        CompoundTag tag = new CompoundTag();
        SavedExplorationMap map = SavedExplorationMap.load(tag);
        PacketRegistry.REGISTRY.sendTo(new MapSyncPacket(map.getMapSync()), player.connection.connection, PLAY_TO_CLIENT);   
    }
// /https://forums.minecraftforge.net/topic/89019-1161solved-sending-packets-to-client-from-dedicated-server/ check here to see how to send to all
}

