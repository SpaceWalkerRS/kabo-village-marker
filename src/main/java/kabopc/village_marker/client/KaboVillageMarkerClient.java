package kabopc.village_marker.client;

import java.util.ArrayList;
import java.util.List;

import kabopc.village_marker.KaboVillageMarkerMod;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;

public class KaboVillageMarkerClient {

	public static final KaboVillageMarkerClient INSTANCE = new KaboVillageMarkerClient();

	public final KaboVillageMarkerRenderer renderer;
	public final List<VillageView> villages;

	private KaboVillageMarkerClient() {
		this.renderer = new KaboVillageMarkerRenderer(this);
		this.villages = new ArrayList<>();
	}

	public void reset(boolean query) {
		villages.clear();

		if (query) {
			ClientPlayNetworking.send(KaboVillageMarkerMod.DATA_CHANNEL, data -> { });
		}
	}

	public void update(PacketByteBuf data) {
		int villageCount = data.readInt();

		for (int i = 0; i < villageCount; i++) {
			int radius = data.readByte();
			BlockPos center = data.readBlockPos();

			VillageView village = new VillageView(radius, center);
			villages.add(village);

			int doorCount = data.readInt();

			for (int j = 0; j < doorCount; j++) {
				BlockPos doorPos = data.readBlockPos();

				DoorView door = new DoorView(doorPos);
				village.doors.add(door);
			}
		}
	}
}
