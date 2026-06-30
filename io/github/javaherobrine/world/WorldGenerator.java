package io.github.javaherobrine.world;
import io.github.javaherobrine.blocks.*;
public abstract class WorldGenerator {
	public abstract Block generate(int x, int y, int z);
}
