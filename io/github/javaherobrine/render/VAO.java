package io.github.javaherobrine.render;
import static org.lwjgl.opengl.GL45.*;
import io.github.javaherobrine.*;
public class VAO {// compact data
	public static final float[] NO_ATLAS_COORDINATE=new float[] {0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1};
	public final float[] data;
	public final int size;
	private final int VAO;
	private int VBO,IBO;
	private int elements,datatype;
	public VAO(float[] data, int size) {
		this.data = data;
		this.size = size;
		this.VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		elements = data.length / size;
	}
	public int bindVBO(int mode) {// return: VBO ID
		VBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, data, mode);
		return VBO;
	}
	public int bindIBO(int[] indices, int mode) {// return: IBO ID
		IBO = glGenBuffers();
		datatype=GL_UNSIGNED_INT;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, mode);
		elements = indices.length;
		return IBO;
	}
	public int bindIBO(short[] indices, int mode) {// return: IBO ID
		IBO = glGenBuffers();
		datatype=GL_UNSIGNED_SHORT;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, mode);
		elements = indices.length;
		return IBO;
	}
	public int bindIBO(byte[] indices, int mode) {// return: IBO ID
		IBO = glGenBuffers();
		datatype=GL_UNSIGNED_BYTE;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
		long addr=GameUtils.address(indices);
		nglBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length,addr, mode);
		GameUtils.allowGC(addr, indices);
		elements = indices.length;
		return IBO;
	}
	public VAO attribute(int location, int size) {
		glVertexAttribPointer(location, size, GL_FLOAT, false, this.size << 2, 0);
		glEnableVertexAttribArray(location);
		return this;
	}
	public VAO attribute(int location, int size, long offset) {
		glVertexAttribPointer(location, size, GL_FLOAT, false, this.size << 2, offset << 2);
		glEnableVertexAttribArray(location);
		return this;
	}
	public void apply() {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		if(elements!=-1) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
			glDrawElements(GL_TRIANGLES, elements, datatype, 0);
		}else
			apply0();
	}
	private void apply0() {
		glDrawArrays(GL_TRIANGLES,0,data.length/size);
	}
	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
	}
	public void points() {
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		if(elements!=-1) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IBO);
			glDrawElements(GL_POINTS, elements, datatype, 0);
		}else
			points0();
	}
	private void points0() {
		glDrawArrays(GL_POINTS,0,data.length/size);
	}
	/**
	 * face order:
	 * up down left right front back
	 * Warning: No checks
	 * content: [coordinate(3 floats)][texture coordinate(2 floats)][normal vector(3 floats)]
	 * @return VAO
	 */
	public static VAO blockVAO(Texture up,Texture down,Texture left,Texture right,Texture front,Texture back,int mode) {
		VAO vao=new VAO(new float[] {
				0,0.99999f,0,up.ulx(),up.uly(),0,1,0,
				0.99999f,0.99999f,0.99999f,up.drx(),up.dry(),0,1,0,
				0,0.99999f,0.99999f,up.ulx(),up.dry(),0,1,0,
				0.99999f,0.99999f,0,up.drx(),up.uly(),0,1,0,
				0,0,0.99999f,down.ulx(),down.uly(),0,-1,0,
				0.99999f,0,0,down.drx(),down.dry(),0,-1,0,
				0,0,0,down.ulx(),down.dry(),0,-1,0,
				0.99999f,0,0.99999f,down.drx(),down.uly(),0,-1,0,
				0,0.99999f,0,left.ulx(),left.uly(),-1,0,0,
				0,0,0.99999f,left.drx(),left.dry(),-1,0,0,
				0,0,0,left.ulx(),left.dry(),-1,0,0,
				0,0.99999f,0.99999f,left.drx(),left.uly(),-1,0,0,
				0.99999f,0.99999f,0.99999f,right.ulx(),right.uly(),1,0,0,
				0.99999f,0,0,right.drx(),right.dry(),1,0,0,
				0.99999f,0,0.99999f,right.ulx(),right.dry(),1,0,0,
				0.99999f,0.99999f,0,right.drx(),right.uly(),1,0,0,
				0,0.99999f,0.99999f,front.ulx(),front.uly(),0,0,1,
				0.99999f,0,0.99999f,front.drx(),front.dry(),0,0,1,
				0,0,0.99999f,front.ulx(),front.dry(),0,0,1,
				0.99999f,0.99999f,0.99999f,front.drx(),front.uly(),0,0,1,
				0.99999f,0.99999f,0,back.ulx(),back.uly(),0,0,-1,
				0,0,0,back.drx(),back.dry(),0,0,-1,
				0.99999f,0,0,back.ulx(),back.dry(),0,0,-1,
				0,0.99999f,0,back.drx(),back.uly(),0,0,-1
		},8);
		vao.bindIBO(new byte[] {
				0,1,2,
				0,1,3,
				4,5,6,
				4,5,7,
				8,9,10,
				8,9,11,
				12,13,14,
				12,13,15,
				16,17,18,
				16,17,19,
				20,21,22,
				20,21,23
		}, mode);
		return vao;
	}
	public static VAO skyVAO() {
		VAO vao=new VAO(new float[] {
			    -1.0f,  1.0f, -1.0f,
			    -1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			    -1.0f, -1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
		},3);
		vao.bindIBO(new byte[] {
				0,1,2,
				2,3,0,
				4,1,0,
				0,5,4,
				2,6,7,
				7,3,2,
				4,5,7,
				7,6,4,
				0,3,7,
				7,5,0,
				1,4,2,
				2,4,6,
		},GL_STATIC_DRAW);
		return vao;
	} 
}
