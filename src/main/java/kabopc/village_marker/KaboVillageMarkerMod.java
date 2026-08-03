package kabopc.village_marker;

import kabopc.village_marker.access.VillageDataAccess;
import kabopc.village_marker.client.KaboVillageMarkerClient;
import kabopc.village_marker.client.KaboVillageMarkerSettings;

import net.minecraft.world.village.SavedVillageData;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.networking.api.ChannelIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import net.ornithemc.osl.networking.api.server.ServerPlayNetworking;

public class KaboVillageMarkerMod implements ModInitializer, ClientModInitializer {

	public static final NamespacedIdentifier DATA_CHANNEL = ChannelRegistry.register(ChannelIdentifiers.from("kvm", "data"));

	@Override
	public void init() {
		ServerPlayNetworking.registerListener(DATA_CHANNEL, (context, data) -> {
			context.ensureOnMainThread();

			SavedVillageData villages = context.player().world.villages;
			VillageDataAccess villageData = (VillageDataAccess)villages;
			KaboVillageMarker marker = villageData.kvm$getMarker();

			if (marker != null) {
				ServerPlayNetworking.send(context.player(), DATA_CHANNEL, marker::write);
			}
		});
	}

	@Override
	public void initClient() {
		KaboVillageMarkerSettings.initialize();

		ClientConnectionEvents.PLAY_READY.register(minecraft -> {
			KaboVillageMarkerClient.INSTANCE.reset(true);
		});
		ClientPlayNetworking.registerListener(DATA_CHANNEL, (context, data) -> {
			context.ensureOnMainThread();

			if (data != null) {
				KaboVillageMarkerClient.INSTANCE.update(data);
			}
		});
	}
}
