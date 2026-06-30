package io.github.javaherobrine.net.verify;
public abstract class PlayerChecker {
	public abstract boolean check(String player);
	public abstract void add(String player);
	public abstract void remove(String player);
	public boolean enabled = true;
}
