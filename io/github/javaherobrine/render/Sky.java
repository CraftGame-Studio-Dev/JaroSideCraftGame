package io.github.javaherobrine.render;
public class Sky implements Renderable{
	public TextureAtlas texture;
	private VAO skyVAO=VAO.skyVAO();
	@Override
	public void render(Renderer renderer) {
		texture.activate(0);
		skyVAO.apply();
	}
}
