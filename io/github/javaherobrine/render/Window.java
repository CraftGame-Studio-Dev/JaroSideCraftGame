package io.github.javaherobrine.render;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import org.lwjgl.opengl.GL;
import xueli.game2.lifecycle.*;
import java.util.*;
import java.util.function.*;
import org.joml.Math;
public class Window implements LifeCycle {
	public final long window;
	private double lPosX[]=new double[1],lPosY[]=new double[1],cPosX[]=new double[1],cPosY[]=new double[1];
	public Camera camera=new Camera();
	public final ArrayList<KeyBinding> bindings = new ArrayList<>();
	public boolean paused=false;
	public Window() {
		glfwInit();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		window = glfwCreateWindow(1920, 1080, "CraftGame", 0, 0);
		System.err.println(window);
		glfwSetWindowSizeCallback(window, (win, width, height) -> {
			glViewport(0, 0, width, height);
		});
		glfwMakeContextCurrent(window);
		glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		bindings.add(new KeyBinding(GLFW_KEY_W,-1,true,"",l->camera.moveForward(-l*Camera.speed)));
		bindings.add(new KeyBinding(GLFW_KEY_S,-1,true,"",l->camera.moveBackward(-l*Camera.speed)));
		bindings.add(new KeyBinding(GLFW_KEY_A,-1,true,"",l->camera.moveLeft(-l*Camera.speed)));
		bindings.add(new KeyBinding(GLFW_KEY_D,-1,true,"",l->camera.moveRight(-l*Camera.speed)));
		bindings.add(new KeyBinding(GLFW_KEY_SPACE,-1,true,"",l->camera.y+=Camera.speed*l));
		bindings.add(new KeyBinding(GLFW_KEY_LEFT_SHIFT,-1,true,"",l->camera.y-=Camera.speed*l));
		bindings.add(new KeyBinding(GLFW_KEY_ESCAPE,-1,true,"",new LongConsumer() {
			@Override
			public void accept(long value) {
				if(paused) {
					paused=true;
				}
			}
		}));
	}
	@Override
	public void init() {
		glViewport(0, 0, 1920, 1080);
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
		Iterator<KeyBinding> iter = bindings.iterator();
		while (iter.hasNext()) {
			KeyBinding binding = iter.next();
			if (glfwGetKey(window, binding.key()) == (binding.click() ? GLFW_PRESS : GLFW_RELEASE)) {
				binding.callback().accept(delta);
			}
		}
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
	}
}
