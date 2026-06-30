package io.github.javaherobrine.net;
import java.io.IOException;
import java.net.Socket;
import io.github.javaherobrine.GameUtils;
public class ServerImpl extends Server<String> {
	public Protocol protocol;
	public ServerImpl(int port, Protocol p) throws IOException {
		super(port,GameUtils.EDT);
		protocol = p;
	}
	@Override
	public ServerSideClient<String> accept(Socket socket) {
		return null;
	}
}
