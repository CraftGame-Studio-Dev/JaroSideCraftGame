package io.github.javaherobrine.format;
import java.util.AbstractMap;
import java.util.Map;
public interface JSONSerializable {
	AbstractMap.SimpleEntry<String, Object>[] values();
	void valueOf(Map<String, Object> input);
}
