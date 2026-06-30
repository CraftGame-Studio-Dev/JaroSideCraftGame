package io.github.javaherobrine.net.event;
import io.github.javaherobrine.net.*;
import io.github.javaherobrine.*;
import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
public class DisconnectEvent extends EventContent {
	;
	static {
		TrieNode.REGISTRY.put(DisconnectEvent.class.getName(), new DisconnectEvent(""));
	}
	private static final long serialVersionUID = 1L;
	String message;
	/**
	 * to disconnect correctly
	 * 
	 * @param serverside: if the value is true,a client is disconnected otherwise it
	 *                    is kicked
	 */
	@Override
	public void recvExec(boolean serverside) {
		try {
			recver.close();
		} catch (IOException e) {
			// do nothing,because the client is closed
		}
		if (serverside) {
			// do some finalization
			System.out.println("[INFO] A Player Disconnected");
		} else {
			System.err.println("[INFO] " + message);
		}
	}
	public DisconnectEvent(String msg) {
		message = msg;
	}
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] { new SimpleEntry<String, Object>("message", message) };
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		message = (String) input.get("message");
	}
	@Override
	public EventContent clone() {
		return new DisconnectEvent("");
	}
}
