package journeymapfogofwar.client.forge;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.integration.SlimeChunkOverlayHandler;
import journeymapfogofwar.network.dispatch.ClientNetworkDispatcher;
import net.minecraft.world.level.ChunkPos;
//import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEvents
{
    /**
     * Listen for Forge chunk load, show polygon overlay if it is a slime chunk.
     */
    @SubscribeEvent
    public void onChunkLoadEvent(ChunkEvent.Load event)
    {
        try
        {   //event.getWorld() != null
            if (event.getLevel() != null)
            {
                ChunkPos chunkPos = event.getChunk().getPos();
                ClientNetworkDispatcher.sendChunkInfoRequest(chunkPos);
            }
        }
        catch (Throwable t)
        {
            JourneymapAdditions.getLogger().error(t.getMessage(), t);
        }
    }

    /**
     * Listen for Forge chunk unload, remove polygon overlay if it is a slime chunk.
     */
    @SubscribeEvent
    public void onChunkUnloadEvent(ChunkEvent.Unload event)
    {
        SlimeChunkOverlayHandler.getInstance().remove(event.getChunk().getPos());
    }
}
