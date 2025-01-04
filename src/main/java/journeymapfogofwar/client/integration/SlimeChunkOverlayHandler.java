package journeymapfogofwar.client.integration;

import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.PolygonHelper;
import journeymapfogofwar.JourneymapAdditions;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class SlimeChunkOverlayHandler
{
    private IClientAPI jmAPI;
    private HashMap<ChunkPos, PolygonOverlay> slimeChunkOverlays;
    private static SlimeChunkOverlayHandler instance;

    private SlimeChunkOverlayHandler(IClientAPI jmAPI)
    {
        this.jmAPI = jmAPI;
        this.slimeChunkOverlays = new HashMap<>();
    }

    public static void init(IClientAPI jmAPI)
    {
        instance = new SlimeChunkOverlayHandler(jmAPI);
    }

    public static SlimeChunkOverlayHandler getInstance()
    {
        return instance;
    }


    @SuppressWarnings("null")
    public PolygonOverlay create(ChunkPos chunkCoords)
    {
        String displayId = "slime_" + chunkCoords.toString();
        String groupName = "Slime Chunks";
        String label = String.format("Slime Chunk [%s,%s]", chunkCoords.x, chunkCoords.z);
        ResourceKey<Level> dimension = Minecraft.getInstance().level.dimension();

        // Style the polygon
        ShapeProperties shapeProps = new ShapeProperties()
                .setStrokeWidth(2)
                .setStrokeColor(0x00ff00).setStrokeOpacity(.7f)
                .setFillColor(0x00ff00).setFillOpacity(.4f);

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
        PolygonOverlay slimeChunkOverlay = new PolygonOverlay(JourneymapAdditions.MOD_ID, displayId, dimension, shapeProps, polygon);
        //PolygonOverlay slimeChunkOverlay = new PolygonOverlay(JourneymapAdditions.MOD_ID, displayId, dimensionToInt(dimensionName), shapeProps, polygon);
            //in journeymap-api v 1.9, dimension is an int, not a ResourceKey<Level>
        // Set the text
        slimeChunkOverlay.setOverlayGroupName(groupName)
                .setLabel(label)
                .setTextProperties(textProps);

        return slimeChunkOverlay;
    }

    public void addChunk(ChunkPos chunkPos) throws Exception
    {
        PolygonOverlay overlay = this.create(chunkPos);
        slimeChunkOverlays.put(chunkPos, overlay);
        jmAPI.show(overlay);
    }

    public void remove(ChunkPos chunkPos) {
        //came with a !
        if (slimeChunkOverlays.containsKey(chunkPos))
        {
            PolygonOverlay overlay = slimeChunkOverlays.remove(chunkPos);
            if (overlay != null)
            {
                jmAPI.remove(overlay);
            }
        }
    }
}
