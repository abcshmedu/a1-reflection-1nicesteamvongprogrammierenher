package edu.hm.cs.swa.demo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized Tests.
 */
@RunWith(Parameterized.class)
public class RendererTestParameterized {

    private static final int RANDOM_1 = 4357345;
    private static final int RANDOM_2 = 9425;
    private static final int RANDOM_3 = 1000000001;
    private static final int ZERO = 0;

    private String expectedResult;
    private int fooValue;

    /**
     * constructor for RendererTestParameterized.
     *
     * @param valueForSomeClass value injected into SomeClass instance.
     * @param expectedResult the result that is expected.
     * @see Renderer
     */
    public RendererTestParameterized(int valueForSomeClass, String expectedResult) {
        this.fooValue = valueForSomeClass;
        this.expectedResult = expectedResult;
    }

    /**
     * all the differenct testcases
     * @return collection of test cases
     */
    @Parameterized.Parameters
    public static Collection testCollection() {
        return Arrays.asList(new Object[][]{

                {RANDOM_1, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + RANDOM_1 + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"},

                {RANDOM_3, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + RANDOM_3 + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"},

                {RANDOM_2, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + RANDOM_2 + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"},

                {ZERO, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + ZERO + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"},

                {Integer.MAX_VALUE, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + Integer.MAX_VALUE + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"},

                {Integer.MIN_VALUE, "Instance of edu.hm.cs.swa.demo.SomeClass:\n"
                        + "foo (Type int): " + Integer.MIN_VALUE + "\n"
                        + "array (Type int[]): [1, 2, 3, ]\n"
                        + "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970\n"}
        });
    }

    /**
     * used for testing.
     *
     * @throws InstantiationException instantiation failed
     * @throws InvocationTargetException invocation failed
     * @throws IllegalArgumentException illegal argument
     * @throws IllegalAccessException illegal access
     * @throws SecurityException accidentally deleted your hard drive
     * @throws NoSuchMethodException method not found
     * @throws ClassNotFoundException class not found
     */
    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        final SomeClass someClass = new SomeClass(fooValue);
        final String actual = new Renderer(someClass).render();
        assertTrue(actual.equals(expectedResult));
    }
}