package io.github.javaherobrine.modloader;
import java.util.*;
import io.github.javaherobrine.net.event.*;
/**
 * Dependence DAG for mods To load mods in a correct order(the dependence items
 * should be loaded previously)(use topological sort) If dependence items
 * missing dependence relations forming a circle,throw an exception and
 * terminate
 */
public class DependenceDAG {
	private static final Map<String, GraphNode> LOADED_MODS = new HashMap<>();
	public static GraphNode getNodeByID(String ID) {
		return LOADED_MODS.computeIfAbsent(ID, N -> {
			return new GraphNode();
		});
	}
	/**
	 * Node for building DAG Each node represents a mod
	 */
	public static class GraphNode {
		JarClassLoader info = null;
		ArrayList<GraphNode> linkto = new ArrayList<GraphNode>();
		ArrayList<GraphNode> linkfrom = new ArrayList<GraphNode>();
		int indegree = 0;
		boolean enable = true;
		public void link(GraphNode to) {
			linkto.add(to);
			to.indegree += 1;
			to.linkfrom.add(this);
		}
		/**
		 * Enable a mod and its dependence items
		 */
		public void enable() {
			if (enable) {
				return;
			}
			enable = true;
			linkfrom.forEach(from -> {
				from.enable();
			});
		}
		/**
		 * Disable a mod and mod(s) which depend(s) on it
		 */
		public void disable() {
			if (!enable) {
				return;
			}
			enable = false;
			linkto.forEach(to -> {
				to.disable();
			});
		}
		/**
		 * Load mods in a correct order
		 */
		public void topologicalSort() {
			Queue<GraphNode> q = new LinkedList<GraphNode>();
			q.add(this);
			List<GraphNode> order = new LinkedList<>();
			while (!q.isEmpty()) {
				GraphNode current = q.poll();
				order.add(current);
				current.linkto.stream().filter(next -> {// disabled mods won't be loaded
					return next.enable;
				}).forEach(next -> {
					next.indegree -= 1;
					if (next.indegree == 0) {
						q.add(next);
					}
				});
			}
			int num = 0;
			Iterator<GraphNode> iter = order.iterator();
			while (iter.hasNext()) {
				GraphNode node = iter.next();
				if (node.info == null) {
					ModLoader.crash("Some dependencies are missing");
				}
				if (!node.info.valid()) {
					ModLoader.crash("Some mods are corrupted");
				}
				if (node.info.getSCSync()) {
					LoginEvent.getInstance().sync.add(node.info.getID());
				}
				for (int i = 0; i < node.linkto.size(); ++i) {
					if (!node.linkto.get(i).enable) {
						continue;
					}
					if (node.linkto.get(i).indegree != 0) {
						ModLoader.crash("Not a DAG");// circle
					}
				}
			}
			iter = order.iterator();
			while (iter.hasNext()) {
				iter.next().info.getInstance().load(num);
				++num;
			}
		}
	}
	public static boolean isIncluded(String ID) {
		return LOADED_MODS.containsKey(ID);
	}
}
