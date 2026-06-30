package io.github.javaherobrine.net;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import io.github.javaherobrine.format.*;
public class EventWrapper implements JSONSerializable {
	private String type;
	private EventContent content;
	EventWrapper(String t, EventContent c) {
		type = t;
		content = c;
	}
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] { new SimpleEntry<String, Object>("type", type),
				new SimpleEntry<String, Object>("content", content) };
	}
	@Override
	public void valueOf(Map<String, Object> input) {
	}// unused
}
