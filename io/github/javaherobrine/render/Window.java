package io.github.javaherobrine.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import io.github.javaherobrine.math.*;
import io.github.javaherobrine.render.input.*;
import xueli.game2.lifecycle.*;
import xueli.utils.exception.*;
import java.util.function.*;
import org.joml.Math;
import org.joml.*;
public class Window implements LifeCycle {
	public final long window;
	public float fov=90;
	public int width,height;
	private double lPosX[]=new double[1],lPosY[]=new double[1],cPosX[]=new double[1],cPosY[]=new double[1];
	public Camera camera=new Camera();
	Matrix4f projection;
	public InputBindings bindings;
	public boolean paused=false,fullscreen=false;
	public Window() {
		glfwSetErrorCallback((errorID,pointer)->{
			try {
				new CrashReport("GLFW Error, code="+errorID,new Error(MemoryUtil.memUTF8Safe(pointer))).showCrashReport().join();
			} catch (InterruptedException e) {}
			System.exit(errorID);
		});
		glfwInit();
		long monitor=glfwGetPrimaryMonitor();
		var video=glfwGetVideoMode(monitor);
		int monitorWidth=video.width(),monitorHeight=video.height();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_POSITION_X, monitorWidth>>2);
		glfwWindowHint(GLFW_POSITION_Y, monitorHeight>>2);
		window = glfwCreateWindow(monitorWidth>>1,monitorHeight>>1, "CraftGame", 0, 0);
		projection=MatrixHelper.perspective(monitorWidth>>1,monitorHeight>>1,90,0.1f,1000);
		System.err.println(window);
		width=monitorWidth>>1;
		height=monitorHeight>>1;
		glfwSetWindowSizeCallback(window, (win, width, height) -> {
			this.width=width;
			this.height=height;
			glViewport(0, 0, width, height);
			projection=MatrixHelper.perspective(width, height, fov, 0.1f, 1000);
		});
		glfwMakeContextCurrent(window);
		glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthFunc(GL_LEQUAL);
		InputBindings game=new InputBindings();
		InputBindings pause=new InputBindings();
		LongConsumer fullScreen=new LongConsumer() {
			@Override
			public void accept(long value) {
				if(fullscreen) {
					fullscreen=false;
					width=monitorWidth>>1;
					height=monitorHeight>>1;
					glfwSetWindowMonitor(window,0,monitorWidth>>2,monitorHeight>>2,width,height,0);
				}else {
					fullscreen=true;
					width=video.width();
					height=video.height();
					glfwSetWindowMonitor(window, monitor,0,0,width,height,video.refreshRate());
				}
				glfwGetCursorPos(window, lPosX, lPosY);
				glfwGetCursorPos(window, cPosX, cPosY);
			}
		};
		game.add(new KeyBinding(GLFW_KEY_W,-1,false,"",l->camera.moveForward(-l*Camera.speed)));
		game.add(new KeyBinding(GLFW_KEY_S,-1,false,"",l->camera.moveBackward(-l*Camera.speed)));
		game.add(new KeyBinding(GLFW_KEY_A,-1,false,"",l->camera.moveLeft(-l*Camera.speed)));
		game.add(new KeyBinding(GLFW_KEY_D,-1,false,"",l->camera.moveRight(-l*Camera.speed)));
		game.add(new KeyBinding(GLFW_KEY_SPACE,-1,false,"",l->camera.y+=Camera.speed*l));
		game.add(new KeyBinding(GLFW_KEY_LEFT_SHIFT,-1,false,"",l->camera.y-=Camera.speed*l));
		game.add(new KeyBinding(GLFW_KEY_ESCAPE,-1,true,"",new LongConsumer() {
			@Override
			public void accept(long value) {
				paused=true;
				bindings=pause;
				glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_NORMAL);
				glfwGetCursorPos(window, lPosX, lPosY);
				glfwGetCursorPos(window, cPosX, cPosY);
			}
		}));
		game.add(new KeyBinding(GLFW_KEY_F11,-1,true,"",fullScreen));
		game.add(new KeyBinding(GLFW_KEY_UP,-1,false,"",l->{
			fov-=l*0.01;
			if(fov<30) {
				fov=30;
			}
			projection=MatrixHelper.perspective(width, height, fov,0.1f,1000);
		}));
		game.add(new KeyBinding(GLFW_KEY_DOWN,-1,false,"",l->{
			fov+=l*0.01;
			if(fov>120) {
				fov=120;
			}
			projection=MatrixHelper.perspective(width, height, fov,0.1f,1000);
		}));
		game.mouse=()->{
			glfwSetWindowTitle(window,"CraftGame Position="+"("+(long)camera.x+","+(long)camera.y+","+(long)camera.z+")");
			glfwGetCursorPos(window, cPosX, cPosY);
			camera.pitch+=Camera.omega*Camera.pitchDirection*(cPosY[0]-lPosY[0]);
			camera.yaw+=Camera.omega*Camera.yawDirection*(cPosX[0]-lPosX[0]);
			lPosY[0]=cPosY[0];
			lPosX[0]=cPosX[0];
			if(camera.pitch>Math.PI_OVER_2_f) {
				camera.pitch=Math.PI_OVER_2_f;
			}else if(camera.pitch<-Math.PI_OVER_2_f) {
				camera.pitch=-Math.PI_OVER_2_f;
			}
			while(camera.yaw<0) {
				camera.yaw+=Math.PI_TIMES_2_f;
			}
			while(camera.yaw>=Math.PI_TIMES_2_f) {
				camera.yaw-=Math.PI_TIMES_2_f;
			}
		};
		pause.add(new KeyBinding(GLFW_KEY_ESCAPE,-1,true,"",new LongConsumer() {
			@Override
			public void accept(long value) {
				paused=false;
				bindings=game;
				glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
			}
		}));
		pause.mouse=()->{
			glfwGetCursorPos(window, lPosX, lPosY);
			glfwGetCursorPos(window, cPosX, cPosY);
		};
		pause.add(new KeyBinding(GLFW_KEY_F11,-1,true,"",fullScreen));
		bindings=game;
	}
	@Override
	public void init() {
		glViewport(0, 0, width, height);
	}
	@Override
	public void tick() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	@Override
	public void release() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	public void input(long delta) {
		InputBindings bindings=this.bindings;
		bindings.input(window, delta);
	}
}
