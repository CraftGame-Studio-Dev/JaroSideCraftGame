package io.github.javaherobrine.modloader;
import java.io.*;
import static io.github.javaherobrine.modloader.DependenceDAG.GraphNode;
import xueli.utils.exception.*;
import java.util.*;
public class ModLoader {
	public static ArrayList<JarClassLoader> classLoaders = new ArrayList<>();
	public static HashSet<String> loaded = new HashSet<>();
	public static ArrayList<GraphNode> root = new ArrayList<>();
	@SuppressWarnings("resource")
	public static void loadModsFrom(File f) throws IOException {
		if (f.exists()) {
			if (f.isFile()) {
				JarClassLoader loader = JarClassLoader.getLoaderFromFile(f);
				if (!loader.valid()) {
					return;
				}
				classLoaders.add(loader);
				String str = loader.getID();
				if (loaded.contains(str)) {
					crash("Duplicated Mods");
				}
				loaded.add(str);
			} else {
				for (File f0 : f.listFiles(f1 -> {
					return f1.isDirectory() || f1.toString().endsWith(".jar");
				})) {
					loadModsFrom(f0);
				}
			}
		}
	}
	public static void init() {
		classLoaders.stream().forEach(cl -> {
			String[] libs = cl.getLibraries();
			GraphNode n = DependenceDAG.getNodeByID(cl.getID());
			if (libs.length == 0) {
				root.add(n);
			} else {
				Arrays.stream(libs).forEach(str -> {
					DependenceDAG.getNodeByID(str).link(n);
				});
			}
		});
	}
	public static void load(int i) {
		root.get(i).topologicalSort();
	}
	public static void crash(final String msg) {
		System.err.println("[FATAL] the game must terminate because " + msg);
		try {
			new CrashReport(msg, new Error()).showCrashReport().join();
		} catch (InterruptedException e) {}
		System.exit(1);
	}
}
