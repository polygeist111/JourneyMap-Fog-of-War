package journeymapfogofwar.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.ChunkInfoHandler;
import journeymapfogofwar.network.dispatch.ServerNetworkDispatcher;

public class ChunkInfoPacket
{

    private ChunkPos chunkPos;
    private Boolean slimeChunk;
    private Boolean isRevealed = false;

    public ChunkInfoPacket(Boolean slimeChunk, ChunkPos chunkPos, Boolean isRevealed)
    {
        this.slimeChunk = slimeChunk;
        this.chunkPos = chunkPos;
        this.isRevealed = isRevealed;
    }

    public ChunkInfoPacket(FriendlyByteBuf buf)
    {
        try
        {
            if (buf.capacity() > 1)
            {
                this.slimeChunk = buf.readBoolean();
                this.chunkPos = buf.readChunkPos();
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error(String.format("Failed to read message for teleport packet: %s", t));
        }
    }

    public void encode(FriendlyByteBuf buf)
    {
        try
        {
            buf.writeBoolean(this.slimeChunk);
            buf.writeChunkPos(this.chunkPos);
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error("[toBytes]Failed to write message for teleport packet:" + t);
        }
    }

    public ChunkPos getChunkPos()
    {
        return chunkPos;
    }

    public Boolean isSlimeChunk()
    {
        return slimeChunk;
    }

    public Boolean isRevealed() {
        return isRevealed;
    }

    public static void handle(ChunkInfoPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            //if the executing body is the server
            if (ctx.get().getDirection().getReceptionSide().isServer())
            {   
                //return the chunk info packet to the original client
                ServerNetworkDispatcher.sendChunkInfoPacket(ctx.get().getSender(), packet.chunkPos);
            }
            else
            {
                ChunkInfoHandler.handle(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
