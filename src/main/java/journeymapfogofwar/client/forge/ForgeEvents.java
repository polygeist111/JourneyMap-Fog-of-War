package journeymapfogofwar.client.forge;

import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.client.integration.SlimeChunkOverlayHandler;
import journeymapfogofwar.network.dispatch.ClientNetworkDispatcher;
import net.minecraft.world.level.ChunkPos;
//import net.minecraftforge.event.world.ChunkEvent;
import journeymapfogofwar.network.TestDataPersistence;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeEvents
{
    /**
     * Listen for Forge chunk load, show polygon overlay if it is a slime chunk.
     */
    @SubscribeEvent
    public void onChunkLoadEvent(ChunkEvent.Load event)
    {
        int i = 0;
        try
        {   //event.getWorld() != null
            if (event.getLevel() != null)
            {
                ChunkPos chunkPos = event.getChunk().getPos();
                ClientNetworkDispatcher.sendChunkInfoRequest(chunkPos);
                JourneymapAdditions.getLogger().info("Requesting packet info");

                TestDataPersistence data = TestDataPersistence.manage(/*event.getLevel().getServer()*/ ServerLifecycleHooks.getCurrentServer());
                data.setTest(i);
                i++;
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
        TestDataPersistence data = TestDataPersistence.manage(/*event.getLevel().getServer()*/ ServerLifecycleHooks.getCurrentServer());
        int testInt = data.getTest();
        JourneymapAdditions.getLogger().info(testInt);
    }
}
