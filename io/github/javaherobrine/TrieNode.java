package io.github.javaherobrine;
import xueli.registry.*;
public class TrieNode {
	private TrieNode[] linkto = new TrieNode[256];
	private Object obj = null;
	public static final TrieNode REGISTRY = new TrieNode();
	public Object access(String str) {
		TrieNode raw = rawNode(str);
		if (raw == null) {
			return null;
		}
		return raw.obj;
	}
	public void put(String str, Object o) {
		char[] ch = str.toCharArray();
		TrieNode current = this;
		for (int i = 0; i < ch.length; i++) {
			if (current.linkto[ch[i]] == null) {
				current.linkto[ch[i]] = new TrieNode();
			}
			current = current.linkto[ch[i]];
		}
		current.obj = o;
	}
	public TrieNode rawNode(String str) {
		char[] ch = str.toCharArray();
		TrieNode current = this;
		for (int i = 0; i < ch.length; i++) {
			if (current.linkto[ch[i]] == null) {
				return null;
			}
			current = current.linkto[ch[i]];
		}
		return current;
	}
	public void put(Identifier i, Object o) {
		put(i.toString(), o);
	}
	public Object access(Identifier i) {
		return access(i.toString());
	}
}
