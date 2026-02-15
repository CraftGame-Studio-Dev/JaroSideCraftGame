package io.github.javaherobrine.blocks;
import io.github.javaherobrine.render.*;
import io.github.javaherobrine.*;
import io.github.javaherobrine.format.*;
import java.io.*;
import xueli.registry.*;
public class BlockPrototype implements JSONString, Serializable{
	private static final long serialVersionUID = 1L;
	public transient Texture up,down,left,right,front,end;
	public final String ID;
	public boolean isLightSource;
	public boolean isTransparent;
	public VAO vao;
	public BlockPrototype(String ID) {
		this.ID=ID;
		TrieNode.REGISTRY.put(new Identifier("block_prototype",ID), this);
	}
	@Override
	public void valueOf(String value) {}//Singleton, so unused
	@Override
	public String toString() {
		return ID;
	}
	public static final BlockPrototype AIR=new BlockPrototype("craftgame:air");
}
