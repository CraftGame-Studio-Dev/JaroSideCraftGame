package io.github.javaherobrine.world;
import java.util.*;
import java.util.function.*;
import java.lang.ref.*;
import java.util.function.*;
import io.github.javaherobrine.*;
public sealed abstract class ChunkManager permits LocalChunkManager, NetworkChunkManager {
	public HashMap<SIITuple, Chunk> loaded=new HashMap<>();
	private HashMap<SIITuple, WeakReference<Chunk>> buffered=new HashMap<>();
	private static final BiFunction<SIITuple,WeakReference<Chunk>,WeakReference<Chunk>> handleWeakRef=(k,v)->{
		if(v.get()==null) {
			return null;
		}
		return v;
	};
	public Consumer<Chunk> onLoad=NONE;
	public Consumer<Chunk> onUnload=NONE;
	private final BiFunction<SIITuple,Chunk,Chunk> unloadAll=(k,v)->{
		buffered.put(k,new WeakReference<>(v));
		onUnload.accept(v);
		return null;
	};
	private static final Consumer<Chunk> NONE=v->{};
	protected String dimension = "";
	public Chunk unload(String dimension,int x, int y) {
		SIITuple tuple=new SIITuple(dimension,x,y);
		Chunk chunk=loaded.remove(tuple);
		buffered.put(tuple,new WeakReference<>(chunk));
		onUnload.accept(chunk);
		return chunk;
	}
	public void unloadAll() {
		loaded.replaceAll(unloadAll);
	}
	public Chunk load(String dimension,int x, int z) {
		SIITuple tuple=new SIITuple(dimension,x,z);
		Chunk chunk=loaded.get(tuple);
		if(chunk==null) {
			var chunkRef=buffered.computeIfPresent(tuple,handleWeakRef);
			if(chunkRef==null) {
				chunk=getUnloadedChunk(dimension,x,z);
			}else {
				chunk=chunkRef.get();
			}
		}
		return chunk;
	}
	public void forLoadedOrCached(String dimension,int x,int z, Consumer<Chunk> apply) {
		SIITuple tuple=new SIITuple(dimension,x,z);
		Chunk chunk=loaded.get(tuple);
		if(chunk==null) {
			var chunkRef=buffered.computeIfPresent(tuple, handleWeakRef);
			if(chunkRef!=null) {
				chunk=chunkRef.get();
			}
		}
	}
	public abstract Chunk getUnloadedChunk(String dimension, int x, int z);
	public void changeDimension(String d) {
		if (d.equals(dimension)) {
			return;
		}
		dimension = d;
		unloadAll();
	}
	public boolean offerChunk(String dimension, int x, int z, Chunk chk) {
		SIITuple tuple=new SIITuple(dimension,x,z);
		if(loaded.containsKey(tuple)) {
			loaded.put(new SIITuple(dimension, x, z), chk);
			return true;
		}else if(buffered.containsKey(tuple)){
			buffered.get(tuple).refersTo(chk);
		}
		return false;
	}
	public static ChunkManager manager;
}
