package io.github.javaherobrine.render;
import static org.lwjgl.opengl.GL45.*;
import org.joml.*;
import java.nio.*;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.system.*;
import io.github.javaherobrine.*;
import io.github.javaherobrine.debug.*;
public class Shader {
	public final int program, vertex, fragment;
	public Shader(String vertexCode, String fragmentCode) {
		vertex = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertex, vertexCode);
		glCompileShader(vertex);
		if(Constant.DEBUG) {
			int success=glGetShaderi(vertex, GL_COMPILE_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				throw new GLSLCompilationError(glGetShaderInfoLog(vertex));
			}
		}
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, fragmentCode);
		glCompileShader(fragment);
		if(Constant.DEBUG) {
			int success=glGetShaderi(fragment, GL_COMPILE_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				glDeleteShader(fragment);
				throw new GLSLCompilationError(glGetShaderInfoLog(fragment));
			}
		}
		program = glCreateProgram();
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		glLinkProgram(program);
		if(Constant.DEBUG) {
			int success=glGetProgrami(program, GL_LINK_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				glDeleteShader(fragment);
				glDeleteProgram(program);
				throw new ShaderProgramLinkError(glGetProgramInfoLog(program));
			}
		}
		glDeleteShader(fragment);
		glDeleteShader(vertex);
	}
	public Shader(byte[] vertexCode, byte[] fragmentCode) {
		program = glCreateProgram();
		vertex = glCreateShader(GL_VERTEX_SHADER);
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		long[] ptr=new long[2];
		long addr=GameUtils.pointerOfPointer(ptr);
		ptr[0]=GameUtils.address(fragmentCode);
		ptr[1]=fragmentCode.length;
		GL45C.nglShaderSource(fragment,1, addr,addr+8);
		GameUtils.allowGC(ptr[0], fragmentCode);
		ptr[0]=GameUtils.address(vertexCode);
		ptr[1]=vertexCode.length;
		GL45C.nglShaderSource(vertex,1,addr,addr+8);
		GameUtils.allowGC(ptr[0], vertexCode);
		GameUtils.freePointerOfPointer(addr, ptr);
		glCompileShader(vertex);
		if(Constant.DEBUG) {
			int success=glGetShaderi(vertex, GL_COMPILE_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				glDeleteShader(fragment);
				glDeleteProgram(program);
				throw new GLSLCompilationError(glGetProgramInfoLog(vertex));
			}
		}
		glCompileShader(fragment);
		if(Constant.DEBUG) {
			int success=glGetShaderi(fragment, GL_COMPILE_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				glDeleteShader(fragment);
				glDeleteProgram(program);
				throw new GLSLCompilationError(glGetProgramInfoLog(fragment));
			}
		}
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		glLinkProgram(program);
		if(Constant.DEBUG) {
			int success=glGetProgrami(program, GL_LINK_STATUS);
			if(success==0) {
				glDeleteShader(vertex);
				glDeleteShader(fragment);
				glDeleteProgram(program);
				throw new ShaderProgramLinkError(glGetProgramInfoLog(program));
			}
		}
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
		uniform(uniform(name), mat);
	}
	public void uniform(int location, Matrix4f mat) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
		glUniformMatrix4fv(location, false, mat.get(buffer));
		MemoryUtil.memFree(buffer);
	}
	public void uniform(int location, float a,float b,float c) {
		glUniform3f(location,a,b,c);
	}
	public void uniform(int location, Matrix4f mat,FloatBuffer buffer) {
		glUniformMatrix4fv(location, false, mat.get(buffer));
	}
	public void uniform(int location,int i) {
		glUniform1i(location, i);
	}
}
