package io.github.javaherobrine.world;
import java.io.IOException;
public sealed class LocalChunkManager extends ChunkManager permits ServerChunkManager {
	protected Save sav;
	public LocalChunkManager(Save s) {
		sav = s;
	}
	@Override
	public Chunk unload(String dimension,int x, int z) {
		super.unload(dimension, x, z);
		Chunk chk = super.unload(dimension, x, z);
		try {
			sav.writeChunk(chk, dimension, x, z);
		} catch (IOException e) {
			throw new Error(e);
		}
		return chk;
	}
	@Override
	public Chunk getUnloadedChunk(String dimension, int x, int z) {
		Chunk c = null;
		try {
			c = sav.readChunk(dimension, x, z);
		} catch (IOException e) {
			System.err.println("bad file");
		}
		if (c != null) {
			return c;
		}
		c = new Chunk();
		Dimension d = Dimension.ALL_DIMENSIONS.get(dimension);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 16; k++) {
					c.chunk[i][j][k] = d.gen().generate((x << 4) | i, j, (z << 4) | k);
				}
			}
		}
		return c;
	}
}
