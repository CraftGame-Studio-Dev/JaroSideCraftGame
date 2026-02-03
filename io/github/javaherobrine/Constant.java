package io.github.javaherobrine;
import io.github.javaherobrine.render.*;
import xueli.utils.io.*;
public class Constant {// Declare constants
	public static final String TITLE = "CraftGame FPS=";
	public static final Texture INVALID_TEXTURE_HARD_CODING=Texture.error();
	public static final Texture[] BREAKING_BLOCKS= {
		Texture.transparent1x1(),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_0.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_1.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_2.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_3.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_4.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_5.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_6.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_7.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_8.png")),
		Texture.create(Files.getResourcePackedInJarStream("/textures/status/destroy_stage_9.png"))
	};
}
