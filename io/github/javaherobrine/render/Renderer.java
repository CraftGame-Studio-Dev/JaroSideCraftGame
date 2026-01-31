package io.github.javaherobrine.render;
import xueli.game2.lifecycle.*;
import java.io.*;
import java.nio.FloatBuffer;
import io.github.javaherobrine.math.MatrixHelper;
import io.github.javaherobrine.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL45.*;
import xueli.utils.io.*;
import org.joml.*;
import org.lwjgl.system.MemoryUtil;
public class Renderer implements RunnableLifeCycle {
	private Window win;
	private long frame = -1;
	private Shader shader;
	private Shader sky;
	private FloatBuffer model=MemoryUtil.memAllocFloat(16);
	private FloatBuffer projection=MemoryUtil.memAllocFloat(16);
	private FloatBuffer lookAt=MemoryUtil.memAllocFloat(16);
	private Texture skyTexture=Texture.sky();
	private VAO skyVAO=VAO.skyVAO();
	private Texture[] loaded= {
			Texture.create(Files.getResourcePackedInJarStream("/textures/andesite.png")),
			Texture.create(Files.getResourcePackedInJarStream("/textures/grassblock.png")),
			Constant.INVALID_TEXTURE_HARD_CODING};
	private VAO vao;
	public Renderer(Window window) {
		win = window;
		vao=VAO.blockVAO(new Vector2f(0,1), new Vector2f(0,0),new Vector2f(1,1), new Vector2f(1,0),GL_STATIC_DRAW);
		vao.bindVBO(GL_STATIC_DRAW);
		skyVAO.bindVBO(GL_STATIC_DRAW);
	}
	@Override
	public void init() {
		try {
			shader = new Shader(Files.getResourcePackedInJarStream("/shaders/block/vertex.vs").readAllBytes(),
					Files.getResourcePackedInJarStream("/shaders/block/fragment.fs").readAllBytes());
			shader.exec();
			sky=new Shader(new String(Files.getResourcePackedInJarStream("/shaders/sky/vertex.vs").readAllBytes()),
					new String(Files.getResourcePackedInJarStream("/shaders/sky/fragment.fs").readAllBytes()));
			glUniform1i(3, 0);
			glUniform1i(4, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		win.init();
		frame = System.currentTimeMillis();
	}
	@Override
	public void tick() {// In something about rendering, this is called "Render" ---LovelyZeeiam
		// compute delta time
		long current = System.currentTimeMillis();
		long deltaTime = current - frame;
		frame = current;
		// process input
		win.input(deltaTime);
		// render
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Constant.INVALID_TEXTURE_HARD_CODING.activate(0);
		shader.exec();
		vao.bind();
		vao.attribute(0, 3);
		vao.attribute(1, 2, 3);
		shader.uniform(1,win.camera.lookAt(),lookAt);
		shader.uniform(2, win.projection);
		for(int i=0;i<3;++i) {
			shader.exec();
			loaded[i].activate(0);
			for(int j=0;j<10;++j) {
				shader.uniform(0,new Matrix4f().translate(i, 0, j));
				Constant.BREAKING_BLOCKS[j].activate(1);
				vao.apply();
			}
		}
		sky.exec();
		skyVAO.bind();
		skyVAO.attribute(0,3);
		sky.uniform(0,new Matrix4f());
		GameUtils.to3x3(lookAt);
		glUniformMatrix4fv(1, false, lookAt);
		sky.uniform(2,win.projection,projection);
		glUniform1i(3, 0);
		skyTexture.activate(0);
		skyVAO.apply();
		// process events and swap buffers
		win.tick();
		model.rewind();
		projection.rewind();
		lookAt.rewind();
	}
	@Override
	public void release() {
		win.release();
	}
	@Override
	public boolean isRunning() {
		return !glfwWindowShouldClose(win.window);
	}
}
