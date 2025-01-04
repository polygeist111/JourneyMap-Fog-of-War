package journeymapfogofwar;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import net.minecraftforge.fml.loading.FMLEnvironment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import journeymapfogofwar.client.forge.ForgeEvents;
import journeymapfogofwar.network.PacketRegistry;


@Mod(JourneymapAdditions.MOD_ID)
public class JourneymapAdditions
{
    public static final String MOD_ID = "journeymapfogofwar";

    @SuppressWarnings({ "removal", "deprecation" }) //CODE: fix this one as soon as possible
    public JourneymapAdditions()
    {
        // Setup the Client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ForgeEvents()));
        //FMLEnvironment.dist.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ForgeEvents()));
        //FMLEnvironment.dist.

        // First Event
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
    }

    /**
     * Common setup event.
     *
     * @param event the event
     */
    public void commonSetupEvent(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            getLogger().info("Initializing Packet Registries");
            PacketRegistry.init();
        });
    }


    /**
     * Get the common logger.
     *
     * @return the logger
     */
    public static Logger getLogger()
    {
        return LogManager.getLogger(MOD_ID);
    }

    /**
     * Magic formula for slime chunk discovery.
     *
     * @param chunk the chunk
     * @return true if it's a slime chunk
     */
    @SuppressWarnings("null")
    public static boolean isSlimeChunk(LevelChunk chunk)
    {
        if (!chunk.getLevel().isClientSide())
        {
            //return true;
            //return WorldgenRandom.seedSlimeChunk(chunk.getPos().x, chunk.getPos().z, chunk.getLevel().getServer().getWorldData().worldGenSettings().seed(), 987234911L).nextInt(10) == 0;
            return WorldgenRandom.seedSlimeChunk(chunk.getPos().x, chunk.getPos().z, chunk.getLevel().getServer().getWorldData().worldGenOptions().seed(), 987234911L).nextInt(10) == 0;
        }
        return false;
    }
}
