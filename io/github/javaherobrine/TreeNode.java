package io.github.javaherobrine;
import java.util.ArrayList;
public abstract class TreeNode {
	public TreeNode() {
	}
	protected ArrayList<TreeNode> linkto = new ArrayList<>();
	public void link(TreeNode kid) {
		linkto.add(kid);
	}
	public abstract Object iterate(Object... args);
	public void construct() {
		linkto.trimToSize();
	}
}
