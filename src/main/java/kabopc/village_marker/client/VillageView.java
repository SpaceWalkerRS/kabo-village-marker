package kabopc.village_marker.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Vec3i;

public class VillageView {

	public final int radius;
	public final int x;
	public final int y;
	public final int z;
	public final List<DoorView> doors = new ArrayList<>();

	public VillageView(int radius, int x, int y, int z) {
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3i getCenter() {
		return new Vec3i(x, y, z);
	}
}
