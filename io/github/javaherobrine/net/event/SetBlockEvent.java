package io.github.javaherobrine.net.event;
import io.github.javaherobrine.net.*;
import io.github.javaherobrine.render.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.function.*;
import io.github.javaherobrine.blocks.*;
import io.github.javaherobrine.world.*;
public class SetBlockEvent extends EventContent implements Consumer<Chunk> {
	private static final long serialVersionUID = 1L;
	private Block block;
	@Override
	public void recvExec(boolean serverside) throws Exception {
		ChunkManager.manager.forLoadedOrCached(block.dimension, block.x>>2, block.z>>2, this);
		if(serverside) {
			((ServerSideClientImpl)recver).s.sendAll(this);
		}
	}
	@Override
	public void valueOf(Map<String, Object> input) {
		block=new Block();
		block.valueOf(input);
	}
	@Override
	public SimpleEntry<String, Object>[] values() {
		return block.values();
	}
	@Override
	public void accept(Chunk t) {
		int nX=block.x&0xF,nZ=block.z&0xF;
		Block old=t.chunk[nX][block.y][nZ];
		boolean air=block.prototype==BlockPrototype.AIR;
		if(air) {
			t.chunk[nX][block.y][nZ]=null;
		}else {
			t.chunk[nX][block.y][nZ]=block;
		}
		if(t.rendering) {
			if(old!=null) {
				old.node.remove();
			}
			if(!air) {
				RenderQueue q=Renderer.renderer.queues[block.prototype.isLightSource?1:0];
				q.put(block);
			}
		}
	}
}
