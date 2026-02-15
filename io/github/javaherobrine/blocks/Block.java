package io.github.javaherobrine.blocks;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.io.*;
import java.math.*;
import io.github.javaherobrine.*;
import io.github.javaherobrine.TrieNode;
import io.github.javaherobrine.format.*;
import io.github.javaherobrine.render.*;
import xueli.registry.*;
public class Block implements JSONSerializable, Serializable, Renderable{
	private static final long serialVersionUID = 1L;
	public BlockPrototype prototype;
	public String dimension;
	public RenderQueue.LinkedListNode node;
	public int x,y,z;
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		return new SimpleEntry[] {
				new SimpleEntry<String,Object>("x",x),
				new SimpleEntry<String,Object>("y",y),
				new SimpleEntry<String,Object>("z",z),
				new SimpleEntry<String,Object>("prototype",prototype),
				new SimpleEntry<String,Object>("dimension",dimension)
		};
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		x=((BigInteger)input.get("x")).intValue();
		y=((BigInteger)input.get("y")).intValue();
		z=((BigInteger)input.get("z")).intValue();
		prototype=(BlockPrototype)TrieNode.REGISTRY.access(new Identifier("prototype",(String)input.get("block_prototype")));
	}
	@Override
	public void render(Renderer renderer) {
		GameUtils.modelMatrix(renderer.modelAddr, x, y, z);
		prototype.up.activate(3);
		prototype.down.activate(4);
		prototype.front.activate(5);
		prototype.end.activate(6);
		prototype.left.activate(7);
		prototype.right.activate(8);
		prototype.vao.apply();
	}
}
