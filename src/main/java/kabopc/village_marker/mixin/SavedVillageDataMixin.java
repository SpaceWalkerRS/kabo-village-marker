package kabopc.village_marker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import kabopc.village_marker.KaboVillageMarker;
import kabopc.village_marker.access.VillageDataAccess;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.village.SavedVillageData;

@Mixin(SavedVillageData.class)
public class SavedVillageDataMixin implements VillageDataAccess {

	@Shadow private World world;

	private KaboVillageMarker kvm$marker;

	@Inject(
		method = "<init>(Lnet/minecraft/world/World;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void kvm$init(World world, CallbackInfo ci) {
		kvm$initMarker();
	}

	@Inject(
		method = "setWorld(Lnet/minecraft/world/World;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void kvm$setWorld(World world, CallbackInfo ci) {
		kvm$initMarker();
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "HEAD"
		)
	)
	private void kvm$tick(CallbackInfo ci) {
		if (kvm$marker != null) {
			kvm$marker.tick();
		}
	}

	@Inject(
		method = "removeVillagesWithoutDoors",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/village/SavedVillageData;markDirty()V"
		)
	)
	private void kvm$markDirtyOnRemoveVillagesWithoutDoors(CallbackInfo ci) {
		kvm$markDirty();
	}

	@Inject(
		method = "addDoorsToVillages",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/village/Village;addDoor(Lnet/minecraft/world/village/VillageDoor;)V"
		)
	)
	private void kvm$markDirtyOnAddDoorsToVillages(CallbackInfo ci) {
		kvm$markDirty();
	}

	@Inject(
		method = "addNewDoor",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
		)
	)
	private void kvm$markDirtyOnAddNewDoor(CallbackInfo ci) {
		kvm$markDirty();
	}

	private void kvm$initMarker() {
		if (world instanceof ServerWorld) {
			kvm$marker = new KaboVillageMarker((ServerWorld)world, (SavedVillageData)(Object)this);
		}
	}

	@Override
	public KaboVillageMarker kvm$getMarker() {
		return kvm$marker;
	}

	@Override
	public void kvm$markDirty() {
		if (kvm$marker != null) {
			kvm$marker.markDirty();
		}
	}
}
