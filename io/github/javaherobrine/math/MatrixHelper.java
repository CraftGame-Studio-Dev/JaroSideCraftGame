package io.github.javaherobrine.math;
import org.joml.Math;
import org.joml.*;
public class MatrixHelper {
	public static final Vector3f X = new Vector3f(1, 0, 0);
	public static final Vector3f Y = new Vector3f(0, 1, 0);
	public static final Vector3f Z = new Vector3f(0, 0, 1);
	public static Matrix4f rotate(Vector3f axis, float theta) {
		float C = Math.cos(theta);
		float S = Math.sin(theta);
		float NC = 1 - C;
		float XY = axis.x * axis.y * NC;
		float XZ = axis.x * axis.z * NC;
		float YZ = axis.y * axis.z * NC;
		return new Matrix4f(C + axis.x * axis.x * NC, XY - axis.z * S, XZ + axis.y * S, 0, XY + axis.z * S,
				C + axis.y * axis.y * NC, YZ - axis.x * S, 0, XZ - axis.y * S, YZ + axis.x * S,
				C + axis.z * axis.z * NC, 0, 0, 0, 0, 1);
	}
	/**
	 * Euler angle
	 */
	public static Matrix4f rotate(float rotX, float rotY, float rotZ) {
		Matrix4f src = new Matrix4f();
		return src.rotateXYZ(rotX, rotY, rotZ);
	}
	public static Vector3f getAxis(float theta, float phi) {
		return new Vector3f(Math.cos(phi) * Math.cos(theta), Math.cos(phi) * Math.sin(theta), Math.sin(phi));
	}
	/**
	 * Code from LovelyZeeiam, but use JOML Library
	 * @author LovelyZeeiam
	 * @param fov fovX
	 */
	public static Matrix4f perspective(float width, float height, float fov, float near, float far) {
		Matrix4f projectionMatrix = new Matrix4f();

		float ratio = width / height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2F))) * ratio);
		float x_scale = y_scale / ratio;
		float frustum_length = far - near;

		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-(far + near) / frustum_length);
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * far * near) / frustum_length));
		projectionMatrix.m33(0);

		return projectionMatrix;
	}
	public static Matrix4f translate(float x, float y, float z) {
		return new Matrix4f(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
	}
}
