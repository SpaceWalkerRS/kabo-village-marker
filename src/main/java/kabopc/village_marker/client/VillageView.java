package kabopc.village_marker.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

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

	public BlockPos getCenter() {
		return new BlockPos(x, y, z);
	}
}
