package journeymapfogofwar.client.integration;

import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.PolygonHelper;
import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.dispatch.ClientNetworkDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class FogOfWarOverlayHandler
{
    private IClientAPI jmAPI;
    private HashMap<ChunkPos, PolygonOverlay> fowChunkOverlays;
    private static FogOfWarOverlayHandler instance;

    private FogOfWarOverlayHandler(IClientAPI jmAPI)
    {
        this.jmAPI = jmAPI;
        this.fowChunkOverlays = new HashMap<>();
    }

    public static void init(IClientAPI jmAPI)
    {
        instance = new FogOfWarOverlayHandler(jmAPI);
    }

    public static FogOfWarOverlayHandler getInstance()
    {
        return instance;
    }


    @SuppressWarnings("null")
    public PolygonOverlay create(ChunkPos chunkCoords)
    {
        String displayId = "unexplored_" + chunkCoords.toString();
        String groupName = "Unexplored Chunks";
        String label = String.format("Unexplored Chunk [%s,%s]", chunkCoords.x, chunkCoords.z);
        ResourceKey<Level> dimension = Minecraft.getInstance().level.dimension();

        // Style the polygon
        ShapeProperties shapeProps = new ShapeProperties()
                .setStrokeWidth(2)
                .setStrokeColor(0x000000).setStrokeOpacity(1.0f)
                .setFillColor(0x000000).setFillOpacity(1.0f);

        // Style the text
        TextProperties textProps = new TextProperties()
                .setBackgroundColor(0x000022)
                .setBackgroundOpacity(.5f)
                .setColor(0x00ff00)
                .setOpacity(1f)
                .setMinZoom(2)
                .setFontShadow(true);

        // Define the shape
        MapPolygon polygon = PolygonHelper.createChunkPolygon(chunkCoords.x, 0, chunkCoords.z);

        // Create the overlay
        PolygonOverlay fowChunkOverlay = new PolygonOverlay(JourneymapAdditions.MOD_ID, displayId, dimension, shapeProps, polygon);
        //PolygonOverlay slimeChunkOverlay = new PolygonOverlay(JourneymapAdditions.MOD_ID, displayId, dimensionToInt(dimensionName), shapeProps, polygon);
            //in journeymap-api v 1.9, dimension is an int, not a ResourceKey<Level>
        // Set the text
        fowChunkOverlay.setOverlayGroupName(groupName)
                .setLabel(label)
                .setTextProperties(textProps);

        return fowChunkOverlay;
    }

    public void addChunk(ChunkPos chunkPos) throws Exception
    {
        PolygonOverlay overlay = this.create(chunkPos);
        fowChunkOverlays.put(chunkPos, overlay);
        jmAPI.show(overlay);
    }

    public void remove(ChunkPos chunkPos, String discoverer) {
        //came with a !
        //if (fowChunkOverlays.containsKey(chunkPos))
        //{
            //fowChunkOverlays.put(chunkPos);
            PolygonOverlay overlay = fowChunkOverlays.remove(chunkPos);
            if (overlay != null)
            {
                jmAPI.remove(overlay);
            }
        //}
    }

    @SubscribeEvent
    public static void onPlayerRespawn( PlayerEvent.PlayerRespawnEvent event) {
        Player pe = event.getEntity();
        Level warudo = pe.level();
        if (warudo.isClientSide()) {
            ClientNetworkDispatcher.sendMapSyncRequest();
        }
        //System.out.println("\n\n\n #### player has respawn " + ", " + warudo.isClientSide() + "\n\n\n" );
        //PlayerEvent$Clone
    }
}

//change this to be the handler for FoW
/*
 * THE PLAN
 * store explored map on the server x
 * when players load in, change dimensions, or respawn, sync their locally stored one to the server
 * anytime a chunk is added to the explored map, send that data to all online players x (currently only sends to original caller I think)
 * when a chunk is called up that is not in the server map, add it and remove the overlay x
 * consider adding configurable range (e.g. six chunks) x not currently referencing config, but there is a distance check
 * overworld first, then make sure it works on all three dimensions x ow
 * add config to support slime overlay toggle, distance threshold
 * consider world border overlay
 * 
 * look into switching to overlay with holes
 */