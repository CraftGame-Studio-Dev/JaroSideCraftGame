package io.github.javaherobrine.render;
import static org.lwjgl.opengl.GL20.glUniform1i;
public class Particle implements Renderable{
	public ParticleMetadata metadata;
	@Override
	public void render(Renderer renderer) {
		metadata.texture.activate(0);
		glUniform1i(3,0);
		metadata.vao.points();
	}
	public static class ParticleMetadata{
		public Texture texture;
		public VAO vao;
	}
}
