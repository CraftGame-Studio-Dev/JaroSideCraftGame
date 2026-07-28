package io.github.javaherobrine.render.widgets;
import java.util.*;
import io.github.javaherobrine.render.*;
import org.joml.Math;
/**
 * Inspired by Java Desktop, this is a component tree in fact.
 * A rectangle component, the real shape depends on texture
 */
public class ComponentObject implements Renderable{
	ArrayList<ComponentObject> child=new ArrayList<>();
	float relativeULX,relativeULY,relativeRDX,relativeRDY;
	Texture texture;
	public void add(ComponentObject obj) {
		obj.relativeULX=Math.lerp(relativeULX, relativeRDX, obj.relativeULX);
		obj.relativeULY=Math.lerp(relativeULY, relativeRDY, obj.relativeULY);
		obj.relativeRDX=Math.lerp(relativeULX, relativeRDX, obj.relativeRDX);
		obj.relativeRDY=Math.lerp(relativeULY, relativeRDY, obj.relativeRDY);
		child.add(obj);
	}
	@Override
	public void render(Renderer renderer) {
		//TODO render parent component
		int size=child.size();
		for(int i=0;i<size;++i) {
			child.get(i).render(renderer);
		}
	}
	//TODO Event listeners
}
