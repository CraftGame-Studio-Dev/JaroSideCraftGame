package io.github.javaherobrine.render;
import static org.lwjgl.opengl.GL45.*;
import org.joml.*;
import java.nio.*;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.system.*;
import io.github.javaherobrine.*;
public class Shader {
	public final int program, vertex, fragment;
	public Shader(String vertexCode, String fragmentCode) {
		System.err.println("vs: " + vertexCode);
		System.err.println("fs: " + fragmentCode);
		program = glCreateProgram();
		vertex = glCreateShader(GL_VERTEX_SHADER);
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, fragmentCode);
		glShaderSource(vertex, vertexCode);
		glCompileShader(vertex);
		System.err.println(glGetShaderInfoLog(vertex));
		glCompileShader(fragment);
		System.err.println(glGetShaderInfoLog(fragment));
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		glLinkProgram(program);
		System.err.println(glGetProgramInfoLog(program));
		glDeleteShader(fragment);
		glDeleteShader(vertex);
	}
	public Shader(byte[] vertexCode, byte[] fragmentCode) {
		System.err.println("vs: " + vertexCode);
		System.err.println("fs: " + fragmentCode);
		program = glCreateProgram();
		vertex = glCreateShader(GL_VERTEX_SHADER);
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		long[] ptr=new long[2];
		long addr=GameUtils.pointerOfPointer(ptr);
		ptr[0]=GameUtils.address(fragmentCode);
		ptr[1]=fragmentCode.length;
		GL45C.nglShaderSource(fragment,1, addr,addr+8);
		glCompileShader(fragment);
		GameUtils.allowGC(ptr[0], fragmentCode);
		ptr[0]=GameUtils.address(vertexCode);
		ptr[1]=vertexCode.length;
		GL45C.nglShaderSource(vertex,1,addr,addr+8);
		GameUtils.allowGC(ptr[0], vertexCode);
		GameUtils.freePointerOfPointer(addr, ptr);
		glCompileShader(vertex);
		System.err.println(glGetShaderInfoLog(vertex));
		glCompileShader(fragment);
		System.err.println(glGetShaderInfoLog(fragment));
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		glLinkProgram(program);
		System.err.println(glGetProgramInfoLog(program));
		glDeleteShader(fragment);
		glDeleteShader(vertex);
	}
	public void exec() {
		glUseProgram(program);
	}
	public int uniform(String name) {
		return glGetUniformLocation(program, name);
	}
	public void uniform(String name, Matrix4f mat) {
		// System.out.println(mat);
		uniform(uniform(name), mat);
	}
	public void uniform(int location, Matrix4f mat) {
		// System.out.println(mat);
		FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
		glUniformMatrix4fv(location, false, mat.get(buffer));
		MemoryUtil.memFree(buffer);
	}
	public void uniform(int location, Matrix4f mat,FloatBuffer buffer) {
		// System.out.println(mat);
		glUniformMatrix4fv(location, false, mat.get(buffer));
	}
}
