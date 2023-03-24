package space.coffos.lija.api.element;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ElementStructure {

    String name() default "ElementName";

    int keyCode() default Keyboard.KEY_NONE;

    boolean toggled() default false;

    Category category();

    String description() default "";

    String clientType() default "All";
}