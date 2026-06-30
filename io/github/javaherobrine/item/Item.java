package io.github.javaherobrine.item;
import xueli.registry.*;
public abstract class Item {
	public abstract Identifier identifier();
	public abstract void leftClick();
	public abstract void rightClick();
	public abstract int maxNumber();
}
