package edu.hm.renderer;

public class ArrayRenderer {
    private static final String INT_ARRAY_TEMPLATE = "(Type int[]): [%s]\n";

    public String render(int[] array) {
        final StringBuilder arrayDataBuilder = new StringBuilder();

        for (int integer : array) {
            arrayDataBuilder.append(integer);
            arrayDataBuilder.append(", ");
        }

        return String.format(INT_ARRAY_TEMPLATE, arrayDataBuilder.toString());
    }
}
