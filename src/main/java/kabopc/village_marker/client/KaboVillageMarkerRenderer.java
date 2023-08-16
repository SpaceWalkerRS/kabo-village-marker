package kabopc.village_marker.client;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.BufferBuilder;

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

		BufferBuilder bufferBuilder = BufferBuilder.INSTANCE;

		double cameraX = minecraft.camera.prevX + (minecraft.camera.x - minecraft.camera.prevX) * tickDelta;
		double cameraY = minecraft.camera.prevY + (minecraft.camera.y - minecraft.camera.prevY) * tickDelta;
		double cameraZ = minecraft.camera.prevZ + (minecraft.camera.z - minecraft.camera.prevZ) * tickDelta;

		bufferBuilder.offset(-cameraX, -cameraY, -cameraZ);

		Color color = Color.values()[0];

		for (VillageView village : marker.villages) {
			int radius = village.radius;
			BlockPos center = village.getCenter();
			List<DoorView> doors = village.doors;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_POINT_SMOOTH);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_CONSTANT_COLOR);
			GL11.glColor4f(color.r, color.g, color.b, color.a);
			GL11.glPointSize(KaboVillageMarkerSettings.DOT_SIZE.get() + 1);
			GL11.glLineWidth(2.0F);

			if (KaboVillageMarkerSettings.DRAW_VILLAGE_SPHERE.get()) {
				float density = KaboVillageMarkerSettings.SPHERE_DENSITY.get();
				int intervals = 24 + (int)(density * 72);
				double[] xs = new double[intervals * (intervals / 2 + 1)];
				double[] ys = new double[intervals * (intervals / 2 + 1)];
				double[] zs = new double[intervals * (intervals / 2 + 1)];

				for (double phi = 0.0F; phi < 2 * Math.PI; phi += (2 * Math.PI) / intervals) {
					for (double theta = 0.0F; theta < Math.PI; theta += (2 * Math.PI) / intervals) {
						int index = (int)(phi * intervals / (2 * Math.PI) + intervals * theta * intervals / (2 * Math.PI));
						xs[index] = center.x + radius * Math.sin(phi) * Math.cos(theta);
						ys[index] = center.y + radius * Math.cos(phi);
						zs[index] = center.z + radius * Math.sin(phi) * Math.sin(theta);
					}
				}

				bufferBuilder.start(GL11.GL_POINTS);

				for (int index = 0; index < intervals * (intervals / 2 + 1); index++) {
					bufferBuilder.vertex(xs[index], ys[index], zs[index]);
				}

				bufferBuilder.end();
			}

			bufferBuilder.start(GL11.GL_LINES);

			for (DoorView door : doors) {
				bufferBuilder.vertex(door.x, door.y, door.z);
				bufferBuilder.vertex(center.x, center.y, center.z);
			}

			bufferBuilder.end();

			if (KaboVillageMarkerSettings.DRAW_GOLEM_AREA.get()) {
				int width = 8;
				int height = 3;

				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE);
				GL11.glColor4f(color.rw, color.gw, color.bw, color.aw);
				GL11.glLineWidth(2.0F);

				bufferBuilder.start(GL11.GL_QUADS);

				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);

				bufferBuilder.end();

				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_CONSTANT_COLOR);
				GL11.glColor4f(color.r, color.g, color.b, color.a);

				bufferBuilder.start(GL11.GL_QUADS);

				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z + width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y - height, center.z - width);
				bufferBuilder.vertex(center.x + width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y + height, center.z - width);
				bufferBuilder.vertex(center.x - width, center.y - height, center.z - width);

				bufferBuilder.end();

				GL11.glEnable(GL11.GL_LINE_STIPPLE);
				GL11.glLineStipple(5, (short)-30584);

				bufferBuilder.start(GL11.GL_LINES);

				for (int w = -width; w <= width; w++) {
					for (int h = -height; h <= height; h++) {
						if (w == -width || w == width || h == -height || h == height) {
							bufferBuilder.vertex(center.x + w, center.y + h, center.z - width);
							bufferBuilder.vertex(center.x + w, center.y + h, center.z + width);
							bufferBuilder.vertex(center.x - width, center.y + h, center.z + w);
							bufferBuilder.vertex(center.x + width, center.y + h, center.z + w);
						}
					}
					for (int l = -width; l < width; l++) {
						if (w == -width || w == width || l == -width || l == width) {
							bufferBuilder.vertex(center.x + w, center.y + height, center.z + l);
							bufferBuilder.vertex(center.x + w, center.y - height, center.z + l);
						}
					}
				}

				bufferBuilder.end();

				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glDisable(GL11.GL_LINE_STIPPLE);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);

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
