package io.github.javaherobrine;
import io.github.javaherobrine.render.*;
import xueli.utils.io.*;
public class Constant {// Declare constants
	public static final String TITLE = "CraftGame FPS=";
	public static final Texture INVALID_TEXTURE_HARD_CODING=TextureAtlas.error();
	public static final Texture[] BREAKING_BLOCKS= {
		TextureAtlas.transparent1x1(),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_0.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_1.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_2.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_3.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_4.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_5.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_6.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_7.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_8.png")),
		TextureAtlas.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_9.png"))
	};
	public static final boolean DEBUG=true;
}
