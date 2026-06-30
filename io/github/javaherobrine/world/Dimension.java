package io.github.javaherobrine.world;
import java.util.HashMap;
public record Dimension(String id, WorldGenerator gen) {
	public static final HashMap<String, Dimension> ALL_DIMENSIONS = new HashMap<>();
	private static final HashMap<String, Dimension> BACKUP = new HashMap<>();
	public static Dimension createDimension(String id, WorldGenerator gen) {
		Dimension d = new Dimension(id, gen);
		ALL_DIMENSIONS.put(id, d);
		return d;
	}
	public static void apply(WorldType[] instance) {
		if (BACKUP.isEmpty()) {
			BACKUP.putAll(ALL_DIMENSIONS);
		}
		for (int i = 0; i < instance.length; ++i) {
			instance[i].replacements.forEach(key -> {
				ALL_DIMENSIONS.put(key.getKey(), key.getValue());
			});
		}
	}
	public static void recover() {
		ALL_DIMENSIONS.clear();
		ALL_DIMENSIONS.putAll(BACKUP);
	}
}
