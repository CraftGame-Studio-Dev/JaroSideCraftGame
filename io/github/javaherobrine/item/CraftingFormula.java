package io.github.javaherobrine.item; 
public class CraftingFormula {
	public static class CraftingSlot{
		Item item;
		boolean allowAlternatives;
		public boolean checkName(Item input) {
			if(allowAlternatives) {
				return input.identifier().location().equals(item.identifier().location());
			}else {
				return input.identifier().equals(item.identifier());
			}
		}
	}
	public boolean ordered;
	public CraftingSlot input[];
	public Item output[];
}
