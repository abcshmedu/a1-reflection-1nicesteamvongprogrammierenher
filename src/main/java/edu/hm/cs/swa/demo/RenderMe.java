package edu.hm.cs.swa.demo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that tells a Renderer to render fields or methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RenderMe {

    /**
     * Used to declare an external Renderer.
     * @return Rendered String by the external Renderer.
     */
    String with() default "";
}