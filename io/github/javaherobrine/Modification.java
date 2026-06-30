package io.github.javaherobrine;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * This annotation tells you that this class can be modified or extended by your
 * modification.
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface Modification {
	String value() default "";
}
