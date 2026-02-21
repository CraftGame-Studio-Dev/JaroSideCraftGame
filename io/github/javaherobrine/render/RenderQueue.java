package io.github.javaherobrine.render;
public class RenderQueue implements Renderable{
	LinkedList internal=new LinkedList();
	Runnable before;
	public static class LinkedListNode{
		public Renderable renderable;
		private LinkedListNode prev,next;
		public LinkedListNode remove() {
			prev.next=next;
			next.prev=prev;
			LinkedListNode next=this.next;
			this.next=null;
			this.prev=null;
			renderable=null;//for GC
			return next;
		}
		public synchronized void link(LinkedListNode t0) {
			synchronized(t0) {
				t0.next=next;
				t0.prev=this;
				next=t0;
			}
		}
	}
	private static class LinkedList{
		final LinkedListNode NIL;
		LinkedList(){
			NIL=new LinkedListNode();
			NIL.prev=NIL;
			NIL.next=NIL;
		}
	}
	@Override
	public void render(Renderer renderer) {
		before.run();
		LinkedListNode nil=internal.NIL;
		nil=nil.next;
		while(nil!=internal.NIL) {
			synchronized(nil) {
				nil.renderable.render(renderer);
				nil=nil.next;
			}
		}
	}
	public LinkedListNode put(Renderable renderable) {
		LinkedListNode node=new LinkedListNode();
		node.renderable=renderable;
		internal.NIL.link(node);
		return node;
	}
}
