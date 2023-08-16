package kabopc.village_marker.access;

import kabopc.village_marker.KaboVillageMarker;

public interface VillageDataAccess {

	KaboVillageMarker kvm$getMarker();

	void kvm$markDirty();

}
