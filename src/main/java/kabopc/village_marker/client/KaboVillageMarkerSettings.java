package kabopc.village_marker.client;

import net.ornithemc.osl.config.api.ConfigManager;
import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.api.config.BaseConfig;
import net.ornithemc.osl.config.api.config.option.BooleanOption;
import net.ornithemc.osl.config.api.config.option.FloatOption;
import net.ornithemc.osl.config.api.config.option.IntegerOption;
import net.ornithemc.osl.config.api.config.option.validator.IntegerValidators;
import net.ornithemc.osl.config.api.serdes.FileSerializerType;
import net.ornithemc.osl.config.api.serdes.SerializerTypes;

public class KaboVillageMarkerSettings extends BaseConfig {

	public static final BooleanOption DRAW_VILLAGES = new BooleanOption("draw_villages", "", true);
	public static final BooleanOption DRAW_GOLEM_AREA = new BooleanOption("draw_golem_area", "", true);
	public static final BooleanOption DRAW_VILLAGE_SPHERE = new BooleanOption("draw_village_sphere", "", true);
	public static final FloatOption SPHERE_DENSITY = new FloatOption("sphere_density", "", 0.225F, value -> value >= 0.0F && value <= 1.0F);
	public static final IntegerOption DOT_SIZE = new IntegerOption("dot_size", "", 0, IntegerValidators.minmax(0, 3));

	@Override
	public String getNamespace() {
		return null;
	}

	@Override
	public String getName() {
		return "Kabo Village Marker";
	}

	@Override
	public String getSaveName() {
		return "kabo-village-marker.json";
	}

	@Override
	public ConfigScope getScope() {
		return ConfigScope.GLOBAL;
	}

	@Override
	public LoadingPhase getLoadingPhase() {
		return LoadingPhase.READY;
	}

	@Override
	public FileSerializerType<?> getType() {
		return SerializerTypes.JSON;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void init() {
		registerOptions("settings", DRAW_VILLAGES, DRAW_GOLEM_AREA, DRAW_VILLAGE_SPHERE, SPHERE_DENSITY, DOT_SIZE);
	}

	private static KaboVillageMarkerSettings instance;

	public static void initialize() {
		if (instance == null) {
			ConfigManager.register(instance = new KaboVillageMarkerSettings());
		}
	}
}
