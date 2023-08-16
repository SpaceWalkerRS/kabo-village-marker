package kabopc.village_marker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import kabopc.village_marker.client.KaboVillageMarkerClient;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(
		method = "teleportToDimension",
		at = @At(
			value = "HEAD"
		)
	)
	private void kvm$clearMarker(CallbackInfo ci) {
		KaboVillageMarkerClient.INSTANCE.reset(true);
	}
}
