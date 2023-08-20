package kabopc.village_marker.client;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tessellator;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class KaboVillageMarkerRenderer {

	private final Minecraft minecraft;
	private final KaboVillageMarkerClient marker;

	public KaboVillageMarkerRenderer(KaboVillageMarkerClient marker) {
		this.minecraft = Minecraft.getInstance();
		this.marker = marker;
	}

	public void render(float tickDelta) {
		if (!KaboVillageMarkerSettings.DRAW_VILLAGES.get()) {
			return;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		double cameraX = minecraft.getCamera().prevX + (minecraft.getCamera().x - minecraft.getCamera().prevX) * tickDelta;
		double cameraY = minecraft.getCamera().prevY + (minecraft.getCamera().y - minecraft.getCamera().prevY) * tickDelta;
		double cameraZ = minecraft.getCamera().prevZ + (minecraft.getCamera().z - minecraft.getCamera().prevZ) * tickDelta;

		bufferBuilder.offset(-cameraX, -cameraY, -cameraZ);

		Color color = Color.values()[0];

		for (VillageView village : marker.villages) {
			int radius = village.radius;
			BlockPos center = village.center;
			List<DoorView> doors = village.doors;

			GlStateManager.enableBlend();
			GlStateManager.disableTexture();
			GlStateManager.depthMask(false);
			GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.CONSTANT_COLOR);
			GlStateManager.color4f(color.r, color.g, color.b, color.a);
			GlStateManager.lineWidth(2.0F);
			GL11.glPointSize(KaboVillageMarkerSettings.DOT_SIZE.get() + 1);

			if (KaboVillageMarkerSettings.DRAW_VILLAGE_SPHERE.get()) {
				float density = KaboVillageMarkerSettings.SPHERE_DENSITY.get();
				int intervals = 24 + (int)(density * 72);
				double[] xs = new double[intervals * (intervals / 2 + 1)];
				double[] ys = new double[intervals * (intervals / 2 + 1)];
				double[] zs = new double[intervals * (intervals / 2 + 1)];

				for (double phi = 0.0F; phi < 2 * Math.PI; phi += (2 * Math.PI) / intervals) {
					for (double theta = 0.0F; theta < Math.PI; theta += (2 * Math.PI) / intervals) {
						int index = (int)(phi * intervals / (2 * Math.PI) + intervals * theta * intervals / (2 * Math.PI));
						xs[index] = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
						ys[index] = center.getY() + radius * Math.cos(phi);
						zs[index] = center.getZ() + radius * Math.sin(phi) * Math.sin(theta);
					}
				}

				bufferBuilder.begin(GL11.GL_POINTS, DefaultVertexFormat.POSITION);

				for (int index = 0; index < intervals * (intervals / 2 + 1); index++) {
					bufferBuilder.vertex(xs[index], ys[index], zs[index]).nextVertex();
				}

				tessellator.end();
			}

			bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

			for (DoorView door : doors) {
				bufferBuilder.vertex(door.pos.getX(), door.pos.getY(), door.pos.getZ()).nextVertex();
				bufferBuilder.vertex(center.getX(), center.getY(), center.getZ()).nextVertex();
			}

			tessellator.end();

			if (KaboVillageMarkerSettings.DRAW_GOLEM_AREA.get()) {
				int width = 8;
				int height = 3;

				GlStateManager.disableCull();
				GlStateManager.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				GlStateManager.blendFunc(SourceFactor.DST_ALPHA, DestFactor.ONE);
				GlStateManager.color4f(color.rw, color.gw, color.bw, color.aw);
				GlStateManager.lineWidth(2.0F);

				bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();

				tessellator.end();

				GlStateManager.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.CONSTANT_COLOR);
				GlStateManager.color4f(color.r, color.g, color.b, color.a);

				bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() + width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() - height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() + width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() + height, center.getZ() - width).nextVertex();
				bufferBuilder.vertex(center.getX() - width, center.getY() - height, center.getZ() - width).nextVertex();

				tessellator.end();

				GL11.glEnable(GL11.GL_LINE_STIPPLE);
				GL11.glLineStipple(5, (short)-30584);

				bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION);

				for (int w = -width; w <= width; w++) {
					for (int h = -height; h <= height; h++) {
						if (w == -width || w == width || h == -height || h == height) {
							bufferBuilder.vertex(center.getX() + w, center.getY() + h, center.getZ() - width).nextVertex();
							bufferBuilder.vertex(center.getX() + w, center.getY() + h, center.getZ() + width).nextVertex();
							bufferBuilder.vertex(center.getX() - width, center.getY() + h, center.getZ() + w).nextVertex();
							bufferBuilder.vertex(center.getX() + width, center.getY() + h, center.getZ() + w).nextVertex();
						}
					}
					for (int l = -width; l < width; l++) {
						if (w == -width || w == width || l == -width || l == width) {
							bufferBuilder.vertex(center.getX() + w, center.getY() + height, center.getZ() + l).nextVertex();
							bufferBuilder.vertex(center.getX() + w, center.getY() - height, center.getZ() + l).nextVertex();
						}
					}
				}

				tessellator.end();

				GlStateManager.enableCull();
				GlStateManager.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				GL11.glDisable(GL11.GL_LINE_STIPPLE);
			}

			GlStateManager.enableTexture();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);

			color = color.cycle();
		}

		bufferBuilder.offset(0.0D, 0.0D, 0.0D);
	}

	private enum Color {

		RED    (1.0F, 0.0F, 0.0F, 1.0F, 0.12F , 0.0F  , 0.0F  , 0.25F),
		MAGENTA(1.0F, 0.0F, 1.0F, 1.0F, 0.105F, 0.0F  , 0.105F, 0.25F),
		BLUE   (0.0F, 0.0F, 1.0F, 1.0F, 0.0F  , 0.0F  , 0.125F, 0.25F),
		CYAN   (0.0F, 1.0F, 1.0F, 1.0F, 0.0F  , 0.105F, 0.105F, 0.25F),
		GREEN  (0.0F, 1.0F, 0.0F, 1.0F, 0.0F  , 0.1F  , 0.0F  , 0.25F),
		YELLOW (1.0F, 1.0F, 0.0F, 1.0F, 0.105F, 0.105F, 0.0F  , 0.25F);

		public final float r;
		public final float g;
		public final float b;
		public final float a;
		public final float rw;
		public final float gw;
		public final float bw;
		public final float aw;

		private Color(float r, float g, float b, float a, float rw, float gw, float bw, float aw) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			this.rw = rw;
			this.gw = gw;
			this.bw = bw;
			this.aw = aw;
		}

		public Color cycle() {
			Color[] colors = values();
			int index = ordinal() + 1;
			return index < colors.length ? colors[index] : colors[0];
		}
	}
}
