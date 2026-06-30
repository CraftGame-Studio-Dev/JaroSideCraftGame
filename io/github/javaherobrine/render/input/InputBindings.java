package io.github.javaherobrine.render.input;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import java.util.*;
public class InputBindings {
	public ArrayList<KeyBinding> bindings=new ArrayList<>();
	public Runnable mouse=()->{};
	private static final HashMap<Integer, Boolean> prev=new HashMap<>();
	public void add(KeyBinding bind) {
		prev.put(bind.key(), false);
		bindings.add(bind);
	}
	public void input(long window,long delta) {
		Iterator<KeyBinding> iter = bindings.iterator();
		while (iter.hasNext()) {
			KeyBinding binding = iter.next();
			if (glfwGetKey(window, binding.key())==GLFW_PRESS){
				if(binding.click()&&prev.get(binding.key())) {
					continue;
				}
				binding.callback().accept(delta);
				prev.put(binding.key(), true);
			}else {
				prev.put(binding.key(),false);
			}
		}
		mouse.run();
	}
}
