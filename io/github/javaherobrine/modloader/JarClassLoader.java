package io.github.javaherobrine.modloader;
import java.io.*;
import java.util.Arrays;
import java.util.jar.*;
import java.net.*;
import static io.github.javaherobrine.modloader.DependenceDAG.GraphNode;
public class JarClassLoader extends URLClassLoader {
	public Attributes attr;
	private ModBase instance;
	private Class<? extends ModBase> mainClass;
	public boolean valid() {
		return mainClass != null && attr != null;
	}
	protected ModBase getInstance() {
		if (!valid()) {
			return null;
		}
		return instance;
	}
	public JarClassLoader(URL url) {
		super(new URL[] { url });
		try {
			attr = ((JarURLConnection) (new URL("jar:" + url + "!/").openConnection())).getManifest()
					.getMainAttributes();
			GraphNode node = DependenceDAG.getNodeByID(getID());
			node.info = this;
			Arrays.stream(getLibraries()).filter(lib -> {
				return lib.trim() != "";
			}).forEach(lib -> {
				GraphNode depend = DependenceDAG.getNodeByID(lib);
				depend.link(node);
			});
			try {
				instance = (ModBase) (loadMainClass().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("Failed to load mod,URL:");
			System.err.println(url);
			e.printStackTrace();
		}
	}
	public String getMainClassName() {
		if (!valid()) {
			return "";
		}
		return attr.getValue(Attributes.Name.MAIN_CLASS);
	}
	public String getID() {
		if (!attr.containsKey("mod-id")) {
			mainClass = null;
			return "";
		}
		if (!valid()) {
			return "";
		}
		return attr == null ? null : attr.getValue("mod-id");
	}
	public String[] getLibraries() {
		if (!valid()) {
			return new String[0];
		}
		if (!attr.containsKey("libraries")) {
			return new String[0];
		} else {
			String str = attr.getValue("libraries");
			if (str == null) {
				return new String[] {};
			} else {
				str.trim();
				if (str.equals("")) {
					return new String[] {};
				} else {
					return str.split(",");
				}
			}
		}
	}
	public static JarClassLoader getLoaderFromFile(File f) throws MalformedURLException {
		return new JarClassLoader(f.toURI().toURL());
	}
	@SuppressWarnings("unchecked")
	private Class<? extends ModBase> loadMainClass() {
		try {
			return (Class<? extends ModBase>) loadClass(getMainClassName());
		} catch (ClassNotFoundException e) {
			System.err.println("The mod is bad (the main class is missing)");
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void close() throws IOException {
		if (valid()) {
			instance.unload();
		}
		super.close();
	}
	public boolean getSCSync() {
		if (!attr.containsKey("sync") || !valid()) {
			return true;
		}
		return Boolean.valueOf(attr.getValue("sync"));
	}
}
