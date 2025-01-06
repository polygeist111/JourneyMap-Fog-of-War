package journeymapfogofwar.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.ChunkInfoHandler;
import journeymapfogofwar.network.dispatch.ServerNetworkDispatcher;

public class MapSyncPacket
{

    private HashMap<ChunkPos, String> exploredMap;

    public MapSyncPacket(HashMap<ChunkPos, String> map)
    {
        this.exploredMap = map;
    }

    public MapSyncPacket(FriendlyByteBuf buf)
    {
        try
        {
            if (buf.capacity() > 1)
            {
                //this.slimeChunk = buf.readBoolean();
                //this.chunkPos = buf.readChunkPos();
                for (int i = 0; i < buf.readInt(); i++) {
                    String[] entryString = buf.readUtf().split("_");
                    exploredMap.put(new ChunkPos(Integer.parseInt(entryString[0]), Integer.parseInt(entryString[1])), entryString[2]);
                }
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error(String.format("Failed to read message for fog sync packet: %s", t));
        }
    }

    public void encode(FriendlyByteBuf buf)
    {
        try
        {
            //buf.writeBoolean(this.slimeChunk);
            //buf.writeChunkPos(this.chunkPos);
            int entryCount = this.exploredMap.keySet().size();
            buf.writeInt(entryCount);
            for (ChunkPos pos : this.exploredMap.keySet()) {
                buf.writeUtf(pos.x + "_" + pos.z + "_" + exploredMap.get(pos));
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error("[toBytes]Failed to write message for fog sync packet:" + t);
        }
    }

    public HashMap<ChunkPos, String> getMap() {
        return exploredMap;
    }

    public static void handle(MapSyncPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            //if the executing body is the server
            if (ctx.get().getDirection().getReceptionSide().isServer())
            {   
                //return the chunk info packet to the original client
                ServerNetworkDispatcher.sendMapSyncPacket(ctx.get().getSender());
                //CODE: write new event to return full map
            }
            else
            {
                ChunkInfoHandler.handle(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
