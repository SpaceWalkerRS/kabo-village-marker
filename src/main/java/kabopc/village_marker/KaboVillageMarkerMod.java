package kabopc.village_marker;

import kabopc.village_marker.access.VillageDataAccess;
import kabopc.village_marker.client.KaboVillageMarkerClient;
import kabopc.village_marker.client.KaboVillageMarkerSettings;

import net.minecraft.resource.Identifier;
import net.minecraft.world.village.SavedVillageData;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class KaboVillageMarkerMod implements ModInitializer, ClientModInitializer {

	public static final Identifier DATA_CHANNEL = new Identifier("kvm", "data");

	@Override
	public void init() {
		ServerPlayNetworking.registerListener(DATA_CHANNEL, (server, handler, player, data) -> {
			SavedVillageData villages = player.world.getVillages();
			VillageDataAccess villageData = (VillageDataAccess)villages;
			KaboVillageMarker marker = villageData.kvm$getMarker();

			if (marker != null) {
				ServerPlayNetworking.send(player, DATA_CHANNEL, marker::write);
			}

			return true;
		});
	}

	@Override
	public void initClient() {
		KaboVillageMarkerSettings.initialize();

		ClientConnectionEvents.LOGIN.register(minecraft -> {
			KaboVillageMarkerClient.INSTANCE.reset(true);
		});
		ClientPlayNetworking.registerListener(DATA_CHANNEL, (minecraft, handler, data) -> {
			KaboVillageMarkerClient.INSTANCE.update(data);
			return true;
		});
	}
}
