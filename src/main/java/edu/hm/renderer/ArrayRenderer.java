package edu.hm.renderer;

/**
 * Used to render Arrays via @RenderMe annotation.
 */
public class ArrayRenderer {
    private static final String INT_ARRAY_TEMPLATE = "(Type int[]): [%s]\n";

    /**
     * Renders an array.
     * @param arrayToBeRendered The array to be rendered.
     * @return The rendered String.
     */
    public String render(int[] arrayToBeRendered) {
        final StringBuilder arrayDataBuilder = new StringBuilder();

        for (int integer : arrayToBeRendered) {
            arrayDataBuilder.append(integer);
            arrayDataBuilder.append(", ");
        }

        return String.format(INT_ARRAY_TEMPLATE, arrayDataBuilder.toString());
    }
}
