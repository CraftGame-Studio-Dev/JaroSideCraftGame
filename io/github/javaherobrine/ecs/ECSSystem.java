package io.github.javaherobrine.ecs;
public interface ECSSystem {
	void init();
	void tick(ECSContext context);
	void release();
}
