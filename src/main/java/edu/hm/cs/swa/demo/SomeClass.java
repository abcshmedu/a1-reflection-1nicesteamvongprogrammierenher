package edu.hm.cs.swa.demo;

import java.util.Date;

/**
 * Copied from script.
 */
//CHECKSTYLE:OFF
public class SomeClass {
    @RenderMe
    private int foo;
    @RenderMe(with = "edu.hm.renderer.ArrayRenderer")
    int[] array = {1, 2, 3,};
    @RenderMe
    private Date date = new Date(123456789);

    public SomeClass(int foo) {
        this.foo = foo;
    }

    @RenderMe
    public void foo() {
    }
}
