package kabopc.village_marker;

import java.util.List;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.village.SavedVillageData;
import net.minecraft.world.village.Village;
import net.minecraft.world.village.VillageDoor;

import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class KaboVillageMarker {

	private final ServerWorld world;
	private final SavedVillageData villageData;

	private boolean dirty;
	private int cooldown;

	public KaboVillageMarker(ServerWorld world, SavedVillageData villageData) {
		this.world = world;
		this.villageData = villageData;
	}

	public void markDirty() {
		dirty = true;
	}

	public void tick() {
		if (dirty && cooldown-- == 0) {
			// TODO: the OG mod breaks all the data up into small parts
			// apparently packets were getting too big???
			// check if we need to do that...
			ServerPlayNetworking.send(world.dimension.getId(), KaboVillageMarkerMod.DATA_CHANNEL, this::write);

			dirty = false;
			cooldown = 400;
		}
	}

	public void write(PacketByteBuf buffer) {
		List<Village> villages = villageData.getVillages();

		buffer.writeInt(villages.size());

		for (Village village : villages) {
			List<VillageDoor> doors = village.getDoors();

			buffer.writeByte(village.getRadius());
			buffer.writeBlockPos(village.getCenter());

			buffer.writeInt(doors.size());

			for (VillageDoor door : doors) {
				buffer.writeBlockPos(door.getPos());
			}
		}
	}
}
