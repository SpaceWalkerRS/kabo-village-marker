package kabopc.village_marker;

import java.io.IOException;
import java.util.List;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.village.SavedVillageData;
import net.minecraft.world.village.Village;
import net.minecraft.world.village.VillageDoor;

import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class KaboVillageMarker {

	private final ServerWorld world;
	private final SavedVillageData villageData;

	private boolean dirty;

	public KaboVillageMarker(ServerWorld world, SavedVillageData villageData) {
		this.world = world;
		this.villageData = villageData;
	}

	public void markDirty() {
		dirty = true;
	}

	public void tick() {
		if (dirty) {
			// TODO: the OG mod breaks all the data up into small parts
			// apparently packets were getting too big???
			// check if we need to do that...
			ServerPlayNetworking.send(world.dimension.id, KaboVillageMarkerMod.DATA_CHANNEL, this::write);

			dirty = false;
		}
	}

	public void write(PacketBuffer data) throws IOException {
		@SuppressWarnings("unchecked")
		List<Village> villages = villageData.getVillages();

		data.writeInt(villages.size());

		for (Village village : villages) {
			Vec3i center = village.getCenter();
			@SuppressWarnings("unchecked")
			List<VillageDoor> doors = village.getDoors();

			data.writeByte(village.getRadius());
			data.writeInt(center.x);
			data.writeInt(center.y);
			data.writeInt(center.z);

			data.writeInt(doors.size());

			for (VillageDoor door : doors) {
				data.writeInt(door.x);
				data.writeInt(door.y);
				data.writeInt(door.z);
			}
		}
	}
}
