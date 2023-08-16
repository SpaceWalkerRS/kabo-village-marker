package kabopc.village_marker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import kabopc.village_marker.access.VillageDataAccess;

import net.minecraft.world.World;
import net.minecraft.world.village.Village;

@Mixin(Village.class)
public class VillageMixin {

	@Shadow private World world;

	@Inject(
		method = "cleanUpDoors",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/village/Village;updateCenterAndRadius()V"
		)
	)
	private void kvm$markDirtyOnCleanUpDoors(CallbackInfo ci) {
		((VillageDataAccess)world.villages).kvm$markDirty();
	}
}
