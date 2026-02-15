package io.github.javaherobrine;
import io.github.javaherobrine.render.*;
public class Main {
	public static void main(String[] args) throws Exception {
		Window win = new Window();
		Renderer render = new Renderer(win);
		Renderer.renderer=render;
		render.run();
	}
}
