package io.github.javaherobrine.net.event;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.math.*;
import io.github.javaherobrine.world.*;
import io.github.javaherobrine.net.*;
import io.github.javaherobrine.*;
public class ChunkLoadEvent extends EventContent {
	private static final long serialVersionUID = 1L;
	public Chunk chk = null;
	public boolean unload = false;
	public String dimension;
	public int x;
	public int z;
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] { new SimpleEntry<String, Object>("dimension", dimension),
				new SimpleEntry<String, Object>("x", x), new SimpleEntry<String, Object>("z", z),
				new SimpleEntry<String, Object>("chunk", chk)
			};
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		x = ((BigInteger) input.get("x")).intValue();
		z = ((BigInteger) input.get("z")).intValue();
		dimension = ((String) input.get("dimension"));
		chk = (Chunk) input.get("chunk");
	}
	@Override
	public void recvExec(boolean serverside) throws Exception {
		if (serverside) {
			ServerChunkManager scm = (ServerChunkManager) ChunkManager.manager;
			ServerSideClientImpl recv = (ServerSideClientImpl) recver;
			if (unload) {
				scm.unload(dimension, x, z);
				recv.loaded.remove(new SIITuple(dimension, x, z));
				return;
			}
			recv.loaded.add(new SIITuple(dimension, x, z));
			chk = scm.load(dimension, x, z);
			recv.send(this);
		} else {
			ChunkManager.manager.changeDimension(dimension);
			if(ChunkManager.manager.offerChunk(dimension, x, z, chk)) {
				ChunkManager.manager.onLoad.accept(chk);
			}
		}
	}
	@Override
	public EventContent clone() {
		return new ChunkLoadEvent();
	}
}
