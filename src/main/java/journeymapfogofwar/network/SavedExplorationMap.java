package journeymapfogofwar.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.SavedData;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.HashMap;
import java.util.Set;

import journeymap.client.api.display.PolygonOverlay;
import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;

public class SavedExplorationMap extends SavedData{
        private static SavedExplorationMap savedMap = null;
        private HashMap<ChunkPos, String> exploredChunks; //ChunkPos, name of player who discovered it

        public Boolean isExplored(ChunkPos pos) {
            JourneymapAdditions.getLogger().info(exploredChunks.containsKey(pos));
            return exploredChunks.containsKey(pos);
        }

        public void exploreChunk(ChunkPos pos, ServerPlayer discoverer) {
            exploredChunks.put(pos, discoverer.getScoreboardName());
            setDirty();
        }

        public static SavedExplorationMap create() {
            if (savedMap == null) {
                savedMap = new SavedExplorationMap();

            }
            return savedMap;
        }

        public static SavedExplorationMap load(CompoundTag tag) {
            SavedExplorationMap data = create();
            //int testInt = tag.getInt("test");
            //load all entries back from string representation as hashmap entries
            Set<String> keys = tag.getAllKeys();
            for (String key : keys) {
                data.exploredChunks.put(chunkFromString(key), tag.getString(key));
            }
            //data.test = testInt;
            return data;
	    }

        public CompoundTag save(CompoundTag tag) {
            //tag.put("explorationMap", exploredChunks);
            for (ChunkPos key : exploredChunks.keySet()) {
                //tag.putValue(key, exploredChunks.get(key))
                tag.putString(chunkToString(key), exploredChunks.get(key));
            }
            return tag;
        }

        public static void accessMap(MinecraftServer server) {
            server.overworld().getDataStorage().computeIfAbsent(SavedExplorationMap::load, SavedExplorationMap::create, "explorationMap");
        }
        //CODE: add support for other dimensions based on serverplayer level dimension

        private static String chunkToString(ChunkPos pos) {
            return pos.x + "_" + pos.z;
        }

        private static ChunkPos chunkFromString(String posString) {
            int posX = Integer.parseInt(posString.substring(0, posString.indexOf("_")));
            int posZ = Integer.parseInt(posString.substring(posString.indexOf("_") + 1));
            return new ChunkPos(posX, posZ);
        }

        public HashMap<ChunkPos, String> getMapSync() {
            HashMap<ChunkPos, String> clone = (HashMap<ChunkPos, String>) exploredChunks.clone();
            return clone;
        }
    
}
