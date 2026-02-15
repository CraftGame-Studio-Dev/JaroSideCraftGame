package io.github.javaherobrine.net.event;
import io.github.javaherobrine.net.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;
import io.github.javaherobrine.*;
public class LoginEvent extends EventContent {
	static {
		TrieNode.REGISTRY.put(LoginEvent.class.getName(), new LoginEvent());
	}
	private static final long serialVersionUID = 1L;
	private static LoginEvent instance = null;
	public String player;
	public HashSet<String> sync = new HashSet<>();
	@Override
	public void recvExec(boolean serverside) {
	}
	private LoginEvent() {
	}
	public static LoginEvent getInstance() {
		if (instance == null) {
			instance = new LoginEvent();
		}
		return instance;
	}
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] { new SimpleEntry<String, Object>("player", player),
				new SimpleEntry<String, Object>("sync", sync)
			};
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		player = (String) input.get("player");
		Stream.of((Object[]) input.get("sync")).forEach(n -> {
			sync.add((String) n);
		});
	}
	@Override
	public EventContent clone() {
		return new LoginEvent();
	}
}
