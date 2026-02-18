package io.github.javaherobrine.render;
import xueli.game2.lifecycle.*;
import java.io.*;
import java.nio.FloatBuffer;
import io.github.javaherobrine.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import xueli.utils.io.*;
import org.joml.*;
import org.lwjgl.system.MemoryUtil;
public class Renderer implements RunnableLifeCycle {
	public static Renderer renderer;
	private Window win;
	private long frame = -1;
	private Shader block;
	private Shader sky;
	private Shader lightSource;
	public final FloatBuffer model=MemoryUtil.memAllocFloat(16);
	public final FloatBuffer projection=MemoryUtil.memAllocFloat(16);
	public final FloatBuffer lookAt=MemoryUtil.memAllocFloat(16);
	public final long modelAddr;
	public final long projectionAddr;
	public final long lookAtAddr;
	private Texture skyTexture=Texture.sky();
	private VAO skyVAO=VAO.skyVAO();
	private Texture[] loaded= {
			Constant.INVALID_TEXTURE_HARD_CODING,
			Texture.create(Files.getResourcePackedInJarStream("/textures/andesite.png")),
			Texture.create(Files.getResourcePackedInJarStream("/textures/grassblock.png")),
			Texture.error0()
	};
	private VAO vao;
	/**
	 * Indices:
	 * 0: block
	 * 1: light source
	 * 2: sky
	 * 3: HUD
	 * 4: particles
	 */
	public final RenderQueue[] queues=new RenderQueue[5];
	{
		for(int i=0;i<queues.length;++i) {
			queues[i]=new RenderQueue();
		}
	}
	public Renderer(Window window) {
		modelAddr=GameUtils.address(model);
		projectionAddr=GameUtils.address(projection);
		lookAtAddr=GameUtils.address(lookAt);
		GameUtils.makeIdentity(modelAddr);
//		queues[0].before=()->{
//			block.uniform(1,win.camera.lookAt(),lookAt);
//			block.uniform(2,win.projection,projection);
//		};
//		queues[1].before=()->{
//			lightSource.uniform(1,win.camera.lookAt(),lookAt);
//			lightSource.uniform(2,win.projection,projection);
//		};
//		queues[2].before=()->{
//			
//		};
		win = window;
		vao=VAO.blockVAO(VAO.NO_ATLAS_COORDINATE,GL_STATIC_DRAW);
		vao.bindVBO(GL_STATIC_DRAW);
		skyVAO.bindVBO(GL_STATIC_DRAW);
	}
	@Override
	public void init() {
		try {
			block = new Shader(Files.getResourcePackedInJarStream("/shaders/block/block.vs").readAllBytes(),
					Files.getResourcePackedInJarStream("/shaders/block/block.fs").readAllBytes());
			lightSource=new Shader(Files.getResourcePackedInJarStream("/shaders/block/lightSource.vs").readAllBytes(),
					Files.getResourcePackedInJarStream("/shaders/block/lightSource.fs").readAllBytes());
			sky=new Shader(Files.getResourcePackedInJarStream("/shaders/sky/vertex.vs").readAllBytes(),
					Files.getResourcePackedInJarStream("/shaders/sky/fragment.fs").readAllBytes());
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
		block.exec();
		vao.bind();
		vao.attribute(0, 3).attribute(1,2,3).attribute(2,3,5);
		block.uniform(1,win.camera.lookAt(),lookAt);
		block.uniform(2, win.projection);
		for(int i=0;i<7;++i) {
			block.uniform(3+i,i);
		}
		for(int i=0;i<4;++i) {
			for(int j=0;j<6;++j) {
				loaded[i].activate(j);
			}
			for(int j=0;j<11;++j) {
				block.uniform(0,new Matrix4f().translate(i, 0, j),model);
				Constant.BREAKING_BLOCKS[j].activate(6);
				vao.apply();
			}
		}
		block.uniform(10,0.2f,0.05f,0.2f);
		lightSource.exec();
		vao.bind();
		vao.attribute(0, 3).attribute(1,2,3).attribute(2,3,5);
		lightSource.uniform(0,new Matrix4f().translate(10, 10, 10),model);
		lightSource.uniform(1,win.camera.lookAt(),lookAt);
		lightSource.uniform(2, win.projection);
		for(int i=0;i<7;++i) {
			lightSource.uniform(3+i,i);
		}
		lightSource.exec();
		loaded[0].activate(0);
		loaded[1].activate(1);
		loaded[2].activate(2);
		Constant.BREAKING_BLOCKS[10].activate(3);
		loaded[3].activate(4);
		Constant.BREAKING_BLOCKS[1].activate(5);
		Constant.BREAKING_BLOCKS[0].activate(6);
		vao.apply();
		sky.exec();
		skyVAO.bind();
		skyVAO.attribute(0,3);
		//sky.uniform(0,new Matrix4f(),model);
		GameUtils.to3x3(lookAt);
		glUniformMatrix4fv(1, false, lookAt);
		sky.uniform(2,win.projection,projection);
		sky.uniform(3,0);
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
