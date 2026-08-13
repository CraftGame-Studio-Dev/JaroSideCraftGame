package io.github.javaherobrine.ecs;
import io.github.javaherobrine.*;
import java.util.*;
/**
 * It's NOT thread safe!
 */
public class EventQueue {
	private LinkedList<AbstractEvent> queue=new LinkedList<>();
	public void put(AbstractEvent event) {
		queue.add(event);
	}
	public void processAll() throws Exception {
		while(!queue.isEmpty()) {
			queue.pop().process();
		}
	}
}
