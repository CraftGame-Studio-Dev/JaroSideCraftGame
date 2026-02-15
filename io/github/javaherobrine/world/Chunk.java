package io.github.javaherobrine.world;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.io.*;
import io.github.javaherobrine.blocks.*;
import io.github.javaherobrine.format.*;
public class Chunk implements Serializable, JSONSerializable {
	private static final long serialVersionUID = 1L;
	public static final Chunk NULL_CHUNK=new Chunk();
	public boolean rendering=false;
	public Block[][][] chunk = new Block[16][256][16];// It's an air on condition that its value is null
	@SuppressWarnings("unchecked")
	@Override
	public SimpleEntry<String, Object>[] values() {
		ArrayList<Block> arr = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 16; k++) {
					if (chunk[i][j][k] != null) {
						arr.add(chunk[i][j][k]);
					}
				}
			}
		}
		return new SimpleEntry[] { new SimpleEntry<String, Object>("chunk", arr.toArray()) };
	}
	@SuppressWarnings("unchecked")
	@Override
	public void valueOf(Map<String, Object> input) {
		Object[] arr = (Object[]) input.get("chunk");
		for (int i = 0; i < arr.length; i++) {
			Block block=new Block();
			block.valueOf((HashMap<String, Object>)arr[i]);
		}
	}
}
