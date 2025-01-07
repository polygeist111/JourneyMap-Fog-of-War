package journeymapfogofwar.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

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

import javax.annotation.Nonnull;

import journeymap.client.api.display.PolygonOverlay;
import journeymapfogofwar.JourneymapAdditions;
import journeymapfogofwar.network.PacketRegistry;
import journeymapfogofwar.network.packet.ChunkInfoPacket;

public class TestDataPersistence extends SavedData {
	
	private int test = 0;

	public int getTest() {
		return this.test;
	}

	public void setTest(int test) {
		this.test = test;
		this.setDirty();
	}

	public static TestDataPersistence create() {
		return new TestDataPersistence();
	}
	
	public static TestDataPersistence load(CompoundTag tag) {
		TestDataPersistence data = create();
		data.test = tag.getInt("test");
		return data;
	}

	public CompoundTag save(@Nonnull CompoundTag tag) {
		tag.putInt("test", test);
		return tag;
	}

	public static TestDataPersistence manage(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(TestDataPersistence::load, TestDataPersistence::create, "testData");
	}
	
}