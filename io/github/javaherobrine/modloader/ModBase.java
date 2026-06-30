package io.github.javaherobrine.modloader;
public abstract class ModBase {
	/**
	 * Extensions should implement this function, and loader will call it when
	 * loading.
	 * 
	 * @param modid the id of the extension, loader will fill in this parameter.
	 */
	public abstract void load(int modid);
	/**
	 * Implement this function to allow unload
	 */
	public void unload() {
	}
	public ModBase() {
	}
}
