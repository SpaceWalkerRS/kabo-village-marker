package kabopc.village_marker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import kabopc.village_marker.client.KaboVillageMarkerClient;

import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(
		method = "render(IFJ)V",
		at = @At(
			value = "INVOKE",
			shift = Shift.AFTER,
			target = "Lnet/minecraft/client/render/world/WorldRenderer;renderMiningProgress(Lcom/mojang/blaze3d/vertex/Tessellator;Lcom/mojang/blaze3d/vertex/BufferBuilder;Lnet/minecraft/entity/Entity;F)V"
		)
	)
	private void kvm$render(int anaglyphRenderPass, float tickDelta, long renderTimeLimit, CallbackInfo ci) {
		KaboVillageMarkerClient.INSTANCE.renderer.render(tickDelta);
	}
}
