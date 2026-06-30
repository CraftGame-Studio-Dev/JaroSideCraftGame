package io.github.javaherobrine;
public class PossibilityTree extends TreeNode {
	private double possibility;
	private double p[];
	private String item;
	@Override
	/**
	 * randomly choose an item
	 * 
	 * @return result
	 * @param args please pass null
	 */
	public Object iterate(Object... args) {
		int index = GameUtils.possibilty(p);
		if (index == p.length) {
			return item;
		}
		return linkto.get(index).iterate((Object[]) null);
	}
	@Override
	public void construct() {
		super.construct();
		p = new double[linkto.size()];
		for (int i = 0; i < p.length; i++) {
			PossibilityTree temp = (PossibilityTree) linkto.get(i);
			p[i] = temp.possibility;
			temp.construct();
		}
		GameUtils.prefixSum(p);
	}
	public PossibilityTree(double poss, String str) {
		item = str;
		possibility = poss;
	}
}
