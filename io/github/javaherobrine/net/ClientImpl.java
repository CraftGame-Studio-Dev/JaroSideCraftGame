package io.github.javaherobrine.net;
import java.io.IOException;
import java.net.Socket;
import io.github.javaherobrine.net.event.*;
public class ClientImpl extends Client {
	public ClientImpl(Socket ac) throws IOException {
		super(ac);
	}
	@Override
	public Protocol protocol() throws IOException {
		// TODO let player choose one
		return null;
	}
	@Override
	public void handshake() throws IOException {
		if (protocol instanceof HandshakeModifier modifier) {
			modifier.handshakeClient();
		} else {
			protocol.send(LoginEvent.getInstance());
		}
	}
}
