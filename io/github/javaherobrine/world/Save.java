package io.github.javaherobrine.world;
import java.io.*;
import io.github.javaherobrine.net.event.*;
import io.github.javaherobrine.modloader.*;
import io.github.javaherobrine.format.*;
import java.util.*;
import java.util.zip.*;
public class Save {
	private String saveFolder;
	public Save(File input) throws IOException {
		saveFolder = input.getAbsolutePath();
		File mods = new File(saveFolder + "/loaded_mods.list");
		if (mods.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(mods));
			ArrayList<String> notLoaded = new ArrayList<>();
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.equals("")) {
					continue;
				}
				if (!ModLoader.loaded.contains(str)) {
					notLoaded.add(str);
				}
			}
			br.close();
			if (!notLoaded.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				notLoaded.forEach(s -> {
					sb.append(s);
					sb.append('\n');
				});
				String res = sb.toString();
				System.err.println("[WARNING] missing mods:" + res);
				// TODO show warnings
			}
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(mods));
		for (String str : LoginEvent.getInstance().sync) {
			bw.write(str);
			bw.newLine();
		}
		bw.close();
	}
	@SuppressWarnings("unchecked")
	public Chunk readChunk(String dimension, int x, int z) throws IOException {
		File f = new File(saveFolder + "/chunks/" + dimension + "-" + x + "," + z + ".dat");
		if (!f.exists()) {
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(f))));
		JSONReader r=new JSONReader(reader);
		Chunk chk=new Chunk();
		chk.valueOf((HashMap<String,Object>)r.readObject());
		r.close();
		return chk;
	}
	public void writeChunk(Chunk chk, String dimension, int x, int z) throws IOException {
		JSONWriter writer=new JSONWriter(new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(saveFolder + "/chunks/" + dimension + "-" + x + "," + z + ".dat")))));
		writer.writeObject(chk);
		writer.close();
	}
	public void writeWorldType(String[] worldTypes) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(saveFolder + "/world.type"));
		for (String entry : worldTypes) {
			bw.write(entry);
			bw.newLine();
		}
		bw.close();
	}
	public WorldType[] readWorldType() throws IOException {
		try {
			FileInputStream in = new FileInputStream(saveFolder + "/world.type");
			String[] res = new String(in.readAllBytes()).split("\n");
			in.close();
			WorldType[] types = new WorldType[res.length];
			for (int i = 0; i < res.length; ++i) {
				types[i] = WorldType.WORLD_TYPES.get(res[i]);
			}
			return types;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
