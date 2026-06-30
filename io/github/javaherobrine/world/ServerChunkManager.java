package io.github.javaherobrine.world;
import io.github.javaherobrine.net.*;
import io.github.javaherobrine.*;
import java.util.*;
public final class ServerChunkManager extends LocalChunkManager {
	public final ServerImpl server;
	public HashMap<SIITuple, Integer> count;
	public ServerChunkManager(Save s, ServerImpl server) {
		super(s);
		this.server = server;
	}
	public ServerChunkManager(LocalChunkManager localChunkManager, ServerImpl server2) {
		this(localChunkManager.sav, server2);
		loaded = localChunkManager.loaded;
		loaded.keySet().forEach(key -> {
			count.put(key, 1);
		});
	}
	public Chunk unload(String dimension, int x, int z) {
		SIITuple pair = new SIITuple(dimension, x, z);
		Chunk chk=loaded.get(pair);
		count.compute(pair, (k, v) -> {
			if (v == null) {
				throw new Error("panic");
			}
			--v;
			if (v == 0) {
				super.unload(dimension, x, z);
				return null;
			}
			return v;
		});
		return chk;
	}
	@Override
	public Chunk load(String dimension, int x, int z) {
		SIITuple pair = new SIITuple(dimension, x, z);
		Chunk chunk=super.load(dimension, x, z);
		count.compute(pair, (k, v) -> {
			if (v == null) {
				return 1;
			}
			return v + 1;
		});
		return chunk;
	}
}
