package io.github.javaherobrine.world;
import java.io.*;
import io.github.javaherobrine.net.*;
import io.github.javaherobrine.net.event.*;
public final class NetworkChunkManager extends ChunkManager {
	private Client connection;
	public NetworkChunkManager(Client c) {
		connection = c;
	}
	@Override
	public Chunk getUnloadedChunk(String dimension, int x, int z) {
		ChunkLoadEvent e = new ChunkLoadEvent();
		e.dimension = dimension;
		e.x = x;
		e.z = z;
		try {
			connection.send(e);
			return Chunk.NULL_CHUNK;
		} catch (IOException e1) {
		} // network error
		return null;
	}
	@Override
	public Chunk unload(String dimension, int x, int z) {
		super.unload(dimension, x, z);
		Chunk chk = super.unload(dimension, x, z);
		ChunkLoadEvent e = new ChunkLoadEvent();
		e.x = x;
		e.z = z;
		e.dimension = dimension;
		e.unload = true;
		try {
			connection.send(e);
		} catch (IOException e1) {
		}
		return chk;
	}
}
