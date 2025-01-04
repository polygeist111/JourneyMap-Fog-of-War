package journeymapfogofwar.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.HashMap;

import journeymap.client.api.display.PolygonOverlay;
import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;

public class ExplorationMap {
        private HashMap<ChunkPos, String> exploredChunks; //ChunkPos, name of player who discovered it

    
}
