package io.github.javaherobrine.render.input;
import java.util.function.*;
public record KeyBinding(int key, int scancode, boolean click, String name, LongConsumer callback) {
	
}
