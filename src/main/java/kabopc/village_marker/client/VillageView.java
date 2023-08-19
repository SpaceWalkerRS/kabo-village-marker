package kabopc.village_marker.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class VillageView {

	public final int radius;
	public final BlockPos center;
	public final List<DoorView> doors = new ArrayList<>();

	public VillageView(int radius, BlockPos center) {
		this.radius = radius;
		this.center = center;
	}
}
