package io.github.javaherobrine.world;
import io.github.javaherobrine.*;
import java.util.*;
@Modification("Create an instance, put your modifications in field replacements(key: dimension, value: replacement) and register in WORLD_TYPES")
public class WorldType {
	public final ArrayList<AbstractMap.SimpleEntry<String, Dimension>> replacements = new ArrayList<>();
	public static final HashMap<String, WorldType> WORLD_TYPES = new HashMap<>();
}
