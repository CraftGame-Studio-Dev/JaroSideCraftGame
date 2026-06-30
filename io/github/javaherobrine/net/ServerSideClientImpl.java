package io.github.javaherobrine.net;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import io.github.javaherobrine.*;
import io.github.javaherobrine.world.*;
public class ServerSideClientImpl extends ServerSideClient<String> {
	public HashSet<SIITuple> loaded = new HashSet<>();
	protected ServerSideClientImpl(Socket sc, Server<String> server, EventDispatchThread handle) throws IOException {
		super(sc, server, handle);
	}
	@Override
	public Protocol protocol() throws IOException {
		return ((ServerImpl) s).protocol.clone();
	}
	@Override
	public void handshake() throws IOException {
		if (protocol instanceof HandshakeModifier modifier) {
			modifier.handshakeServer();
		}
	}
	@Override
	public void close() throws IOException {
		super.close();
		ServerChunkManager scm = (ServerChunkManager) ChunkManager.manager;
		loaded.stream().forEach(pair -> {
			scm.unload(pair.x(), pair.y(), pair.z());
		});
		loaded.clear();
	}
}
