package io.github.javaherobrine.world;
import java.util.AbstractMap.SimpleEntry;
import java.math.*;
import java.util.*;
import java.io.*;
import io.github.javaherobrine.blocks.*;
import io.github.javaherobrine.format.*;
import io.github.javaherobrine.*;
public class Chunk implements Serializable, JSONSerializable {
	private static final long serialVersionUID = 1L;
	public static final Chunk NULL_CHUNK=new Chunk();
	public Block[][][] chunk = new Block[16][256][16];// It's an air on condition that its value is null
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		ArrayList<BlockWrapper> arr = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 16; k++) {
					if (chunk[i][j][k] != null) {
						BlockWrapper wrap = new BlockWrapper();
						wrap.x = i;
						wrap.y = j;
						wrap.z = k;
						wrap.b = chunk[i][j][k];
						arr.add(wrap);
					}
				}
			}
		}
		return new SimpleEntry[] { new SimpleEntry<String, Object>("chunk", arr.toArray()) };
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		Object[] arr = (Object[]) input.get("chunk");
		for (int i = 0; i < arr.length; i++) {
			BlockWrapper wrap = ((BlockWrapper) arr[i]);
			chunk[wrap.x][wrap.y][wrap.z] = wrap.b;
		}
	}
	private static class BlockWrapper implements JSONSerializable {
		Block b;
		int x, y, z;
		@SuppressWarnings("unchecked")
		@Override
		public SimpleEntry<String, Object>[] values() {
			return new SimpleEntry[] { new SimpleEntry<String, Object>("x", x), new SimpleEntry<String, Object>("y", y),
					new SimpleEntry<String, Object>("z", z), new SimpleEntry<String, Object>("block", b) };
		}
		@SuppressWarnings("unchecked")
		@Override
		public void valueOf(Map<String, Object> input) {
			x = ((BigInteger) input.get("x")).intValue();
			y = ((BigInteger) input.get("y")).intValue();
			z = ((BigInteger) input.get("z")).intValue();
			Map<String, Object> map = (Map<String, Object>) input.get("block");
			Block block = ((Block) TrieNode.REGISTRY.access((String) map.get("type"))).clone();
			block.valueOf(map);
			b = block;
		}
	}
}
