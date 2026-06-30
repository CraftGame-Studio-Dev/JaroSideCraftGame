package io.github.javaherobrine.net;
import java.io.*;
import java.net.*;
import io.github.javaherobrine.*;
public class ObjectStreamProtocol extends Protocol {
	static {
		TrieNode.REGISTRY.put(ObjectStreamProtocol.class.getName(), new ObjectStreamProtocol());
	}
	public ObjectStreamProtocol(Socket soc) throws IOException {
		super();
		setSocket(soc);
	}
	private ObjectStreamProtocol() {
	}
	@Override
	public EventContent next() {
		if (hasNext()) {
			try {
				return (EventContent) ((ObjectInputStream) in).readObject();
			} catch (Throwable e) {
				exception = e;
			}
		}
		return null;
	}
	@Override
	public void send(EventContent ec) throws IOException {
		((ObjectOutputStream) out).writeObject(ec);
	}
	@Override
	public Protocol clone() {
		return new ObjectStreamProtocol();
	}
	@Override
	public void setSocket(Socket soc) throws IOException {
		out = new ObjectOutputStream(soc.getOutputStream());
		in = new ObjectInputStream(soc.getInputStream());
	}
}
