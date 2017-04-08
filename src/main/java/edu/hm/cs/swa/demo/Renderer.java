package edu.hm.cs.swa.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Renderer {

    private final Object objectToRender;

    public Renderer(Object objectToRender) {
        if(objectToRender == null) throw new IllegalArgumentException("");

        this.objectToRender = objectToRender;
    }

    public String render()
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        final StringBuilder renderedObjectBuilder = new StringBuilder();

        final String renderedInstanceName = renderInstanceName(objectToRender);
        renderedObjectBuilder.append(renderedInstanceName);

        final String renderedFields = renderFields(objectToRender);
        renderedObjectBuilder.append(renderedFields);

        final String renderedMethods = renderMethods(objectToRender);
        renderedObjectBuilder.append(renderedMethods);

        return renderedObjectBuilder.toString();
    }

    private String renderInstanceName(Object objectToRender) {
        final Class classOfObjectToRender = objectToRender.getClass();
        return "Instance of " + classOfObjectToRender.getCanonicalName() + ":\n";
    }

    private String renderFields(Object objectToRender)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        final Class classOfObjectToRender = objectToRender.getClass();
        final Field[] fieldsToRender = classOfObjectToRender.getDeclaredFields();
        final StringBuilder builder = new StringBuilder();

        for (final Field field : fieldsToRender) {
            if (field.isAnnotationPresent(RenderMe.class)) {
                builder.append(renderField(field));
            }
        }

        return builder.toString();
    }

    private String renderField(Field field) throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        final StringBuilder builder = new StringBuilder();

        final RenderMe annotation = field.getAnnotation(RenderMe.class);
        final String rendererToUse = annotation.with();

        if (rendererToUse.equals("")) {
            field.setAccessible(true);
            builder.append(field.getName());
            builder.append(" (Type ");
            if (field.getType().isPrimitive()) {
                builder.append(field.getType().getSimpleName());
            } else {
                builder.append(field.getType().getCanonicalName());
            }
            builder.append("): ");
            builder.append(field.get(objectToRender).toString());
            builder.append('\n');
        } else {
            builder.append(renderViaReflection(rendererToUse, field));
        }

        return builder.toString();
    }

    private String renderMethods(Object objectToRender) {
        final Class classOfObjectToRender = objectToRender.getClass();
        final Method[] methods = classOfObjectToRender.getDeclaredMethods();

        final StringBuilder builder = new StringBuilder();

        for (final Method method : methods) {
            if (method.isAnnotationPresent(RenderMe.class)) {
                if (!method.getReturnType().toString().equals("void")) {
                    builder.append(method.getName());
                    builder.append(" returns: ");

                    builder.append(method.getReturnType());
                    builder.append('\n');
                }
            }
        }

        return builder.toString();
    }

    private String renderViaReflection(final String canonicalName, final Field field)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {

        final StringBuilder builder = new StringBuilder();
        final Class renderer = Class.forName(canonicalName);
        final Object rendererObject = renderer.newInstance();
        final Class clazz = field.getType();
        final Class[] parameterArray = new Class[1];
        parameterArray[0] = clazz;

        builder.append(field.getName());
        builder.append(' ');
        builder.append(renderer.getMethod("render", parameterArray)
                .invoke(rendererObject, field.get(objectToRender)));

        return builder.toString();
    }
}
