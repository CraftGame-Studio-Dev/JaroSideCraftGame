package io.github.javaherobrine.render;
public record Texture(TextureAtlas t,float ulx,float uly,float drx,float dry) {
	public void activate(int text) {
		t.activate(text);
	}
}
