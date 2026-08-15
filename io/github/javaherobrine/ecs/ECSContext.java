package io.github.javaherobrine.ecs;
import xueli.game2.lifecycle.*;
import io.github.javaherobrine.*;
import java.util.*;
/**
 * Use sparse set.The entities are compacted. Low 32-bits are index, the rest are generation
 */
public class ECSContext implements LifeCycle{
	private final ArrayList<Integer/*int*/> sparse=new ArrayList<>();
	private final ArrayList<Long/*long*/> reuse=new ArrayList<>();
	private final ArrayList<Long/*long*/> dense=new ArrayList<>();
	private final EventQueue deferred=new EventQueue();
	private final ArrayList<ECSSystem> systems=new ArrayList<>();
	private final ArrayList<ComponentManager<?>> managers=new ArrayList<>();
	private final HashMap<String,ComponentManager<?>> query=new HashMap<>();
	private int entity=-1;
	private int size=0;
	public int spawn() {
		if(Constant.DEBUG) {
			if(entity==-2) {
				throw new IllegalStateException("Can't spawn an entity after releasing ECS");
			}
		}
		int ret;
		if(reuse.isEmpty()) {
			sparse.add(size);
			dense.add((long)(++entity));
			size+=1;
			ret=entity;
		}else {
			long entity=reuse.removeLast();
			int index=(int)(entity&0xFFFFFFFF);
			dense.set(index,entity);
			ret=index;
		}
		for(int i=0;i<managers.size();++i) {
			managers.get(i).nullElement();
		}
		return ret;
	}
	public void kill(int entity) {
		if(Constant.DEBUG) {
			if(entity==-2) {
				throw new IllegalStateException("Can't kill an entity after releasing ECS");
			}
		}
		deferred.put(new KillEvent(entity));
	}
	@Override
	public void init() {
		managers.addAll(query.values());
		systems.forEach(s->{
			s.init();
		});
	}
	@Override
	public void tick() {
		for(int i=0;i<systems.size();++i) {
			systems.get(i).tick(this);
		}
		try {
			deferred.processAll();
		} catch (Exception e) {
			throw new Error("panic",e);//Won't get here
		}
	}
	@Override
	public void release() {
		systems.forEach(s->{
			s.release();
		});
		entity=-2;
	}
	public void addSystem(ECSSystem system) {
		if(Constant.DEBUG) {
			if(managers.size()>0) {
				throw new IllegalStateException("Can't add a system after initialization");
			}
		}
		systems.add(system);
	}
	public ComponentManager<?> query(String key) {
		if(Constant.DEBUG) {
			if(query.containsKey(key)) {
				throw new NoSuchElementException("No such component: "+key);
			}
		}
		return query.get(key);
	}
	public void register(String key,ComponentManager<?> manager) {
		if(Constant.DEBUG) {
			if(managers.size()>0) {
				throw new IllegalStateException("Can't register a component after initialization");
			}
			if(query.containsKey(key)) {
				System.err.println("Warning: duplicate component, the older one is replaced, system: "+key);
			}
		}
		query.put(key, manager);
	}
	public int getSparse(int entityID) {
		return sparse.get(entityID);
	}
	private final class KillEvent extends AbstractEvent{
		final int entity;
		@Override
		public void process() {
			if(entity>sparse.size()) {
				return;//Kill an entity that doesn't exist
			}
			int index=sparse.get(entity);
			if(index==-1) {
				return;
			}
			long old=dense.get(index)+0x100000000L;
			reuse.add(old);
			long last=dense.removeLast();
			dense.set(index,last);
			sparse.set(entity,-1);
			sparse.set((int)(last&0xFFFFFFFF),index);
			for(int i=0;i<managers.size();++i) {
				managers.get(i).delete(index);
			}
			--size;
		}
		KillEvent(int e){
			entity=e;
		}
	}
	/**
	 * The manager accepts sparse[entityID], so the name of parameter is index instead of entity
	 * @param <T> component type
	 */
	public final class ComponentManager<T>{
		private final T value;
		private final ArrayList<Boolean/*boolean*/> existance=new ArrayList<>();
		private final ArrayList<T> components=new ArrayList<>();
		public ComponentManager(T def) {
			value=def;
		}
		private boolean check(int index) {
			return index<size;
		}
		public boolean exists(int index) {
			if(check(index)) {
				return existance.get(index);
			}
			return false;
		}
		public T get(int index) {
			if(check(index)) {
				return components.get(index);
			}
			return value;
		}
		public void set(int index,T value) {
			components.set(index, value);
			existance.set(index, true);
		}
		public T remove(int index) {
			existance.set(index, false);
			return components.set(index, value);
		}
		private void nullElement() {
			components.add(value);
			existance.add(false);
		}
		private void delete(int index) {
			T last=components.removeLast();
			components.set(index, last);
			boolean lastExist=existance.getLast();
			existance.set(index, lastExist);
		}
	}
}
