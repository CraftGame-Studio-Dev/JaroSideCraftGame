package io.github.javaherobrine.net.event;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import io.github.javaherobrine.net.*;
public class ChatEvent extends EventContent {
	private static final long serialVersionUID = 1L;
	public String destination = null;
	public String content;
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] { new SimpleEntry<String, Object>("destination", destination),
				new SimpleEntry<String, Object>("content", content) };
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		destination = (String) input.get("destination");
		content = (String) input.get("content");
	}
	@Override
	public void recvExec(boolean serverside) throws Exception {
		if (serverside) {
			ServerSideClientImpl c = (ServerSideClientImpl) recver;
			System.out.println(c.player + ": " + content);
			// TODO command process
			if (destination == null) {
				destination = c.player;
				c.s.sendAll(this);
			} else {
				String dest = destination;
				destination = c.player;
				c.s.getClient(dest).send(this);
				c.send(this);
			}
		} else {
			System.err.println(destination + ": " + content);
			// TODO show the message
		}
	}
	private ChatEvent() {
	}
	public ChatEvent(String content) {
		this.content = content;
	}
	public ChatEvent(String destination, String content) {
		this.destination = destination;
		this.content = content;
	}
	@Override
	public EventContent clone() {
		return new ChatEvent();
	}
}
