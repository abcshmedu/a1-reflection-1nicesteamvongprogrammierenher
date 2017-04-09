package edu.hm.cs.swa.demo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RendererTest {
    private SomeClass toRender;
    private Renderer renderer;

    @Before
    public void setUp() {
        toRender = new SomeClass(5);
        renderer = new Renderer(toRender);
    }

    @Test
    public void testRendering() throws Exception {
        final String expected = "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
        +"foo (Type int): 5\n"
        +"array (Type int[]): [1, 2, 3, ]\n"
        +"date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n";

        final String result = renderer.render();

        assertTrue(result.equals(expected));
    }
}