package kabopc.village_marker.client;

import java.util.ArrayList;
import java.util.List;

import kabopc.village_marker.KaboVillageMarkerMod;

import net.minecraft.network.PacketByteBuf;

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
			int centerX = data.readInt();
			int centerY = data.readInt();
			int centerZ = data.readInt();

			VillageView village = new VillageView(radius, centerX, centerY, centerZ);
			villages.add(village);

			int doorCount = data.readInt();

			for (int j = 0; j < doorCount; j++) {
				int doorX = data.readInt();
				int doorY = data.readInt();
				int doorZ = data.readInt();

				DoorView door = new DoorView(doorX, doorY, doorZ);
				village.doors.add(door);
			}
		}
	}
}
