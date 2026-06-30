package io.github.javaherobrine.render;
import org.joml.*;
import org.joml.Math;
public class Camera {
	public float x = 0, y = 0, z = 0, pitch=0, yaw=0;
	public static float speed = 0.01f, omega = -0.01f;
	public static byte pitchDirection=1,yawDirection=1;
	/*
line 0, column 0= cos(yaw)
line 0, column 1= 0
line 0, column 2= -sin(yaw)
line 0, column 3= -cos(yaw)x+sin(yaw)z
line 1, column 0= sin(pitch)sin(yaw)
line 1, column 1= cos(pitch)
line 1, column 2= cos(yaw)sin(pitch)
line 1, column 3= -sin(pitch)sin(yaw)x-cos(pitch)y-cos(yaw)sin(pitch)z
line 2, column 0= cos(pitch)sin(yaw)
line 2, column 1= -sin(pitch)
line 2, column 2= cos(pitch)cos(yaw)
line 2, column 3= -cos(pitch)sin(yaw)x+sin(pitch)y-cos(pitch)cos(yaw)z
line 3, column 0= 0
line 3, column 1= 0
line 3, column 2= 0
line 3, column 3= 1
	 */
	public Matrix4f lookAt() {
		float pitchC = (float) Math.cos(pitch);
		float pitchS = (float) Math.sin(pitch);
		float yawC = (float) Math.cos(yaw);
		float yawS = (float) Math.sin(yaw);
		return new Matrix4f(
				yawC,pitchS*yawS,pitchC*yawS,0,
				0,pitchC,-pitchS,0,
				-yawS,yawC*pitchS,pitchC*yawC,0,
				-yawC*x+yawS*z,-pitchS*yawS*x-pitchC*y-yawC*pitchS*z,-pitchC*yawS*x+pitchS*y-pitchC*yawC*z,1);
	}
	public void moveRight(float distance) {
		x -= distance * Math.cos(yaw);
		z += distance * Math.sin(yaw);
	}
	public void moveLeft(float distance) {
		moveRight(-distance);
	}
	public void moveForward(float distance) {
		x += distance * Math.sin(yaw);
		z += distance * Math.cos(yaw);
	}
	public void moveBackward(float distance) {
		moveForward(-distance);
	}
}
