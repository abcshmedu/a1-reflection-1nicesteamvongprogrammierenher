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

        final StringBuilder renderedStringBuilder = new StringBuilder();

        final String renderedInstanceName = renderInstanceName(objectToRender);
        renderedStringBuilder.append(renderedInstanceName);

        final String renderedFields = renderFields(objectToRender);
        renderedStringBuilder.append(renderedFields);

        final String renderedMethods = renderMethods(objectToRender);
        renderedStringBuilder.append(renderedMethods);

        return renderedStringBuilder.toString();
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
        final Field[] declaredFields = classOfObjectToRender.getDeclaredFields();
        final StringBuilder renderedFieldsBuilder = new StringBuilder();

        for (final Field field : declaredFields) {
            if (field.isAnnotationPresent(RenderMe.class)) {
                final String renderedField = renderField(field);
                renderedFieldsBuilder.append(renderedField);
            }
        }

        return renderedFieldsBuilder.toString();
    }

    private String renderField(Field field)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        final RenderMe annotation = field.getAnnotation(RenderMe.class);
        final String rendererToUse = annotation.with();

        return rendererToUse.equals("") ?
                useStandardFieldRenderer(field) :
                useExternalFieldRenderer(rendererToUse, field);
    }

    private String useStandardFieldRenderer(Field field) throws IllegalAccessException {
        final StringBuilder builder = new StringBuilder();

        field.setAccessible(true);
        builder.append(field.getName());
        builder.append(" (Type ");
        builder.append(getTypeOfField(field));
        builder.append("): ");
        builder.append(field.get(objectToRender).toString());
        builder.append('\n');

        return builder.toString();
    }

    private String getTypeOfField(Field field) {
        return field.getType().isPrimitive() ?
                field.getType().getSimpleName() :
                field.getType().getCanonicalName();
    }

    private String useExternalFieldRenderer(final String externalRenderer, final Field field)
            throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {

        final Class rendererClass = Class.forName(externalRenderer);
        final Object rendererObject = rendererClass.newInstance();
        final Class[] parameterArray = {field.getType()};

        final Method externalRenderMethod = rendererClass.getMethod("render", parameterArray);
        final String renderedField = (String) externalRenderMethod.invoke(rendererObject, field.get(objectToRender));

        return field.getName() + ' ' + renderedField;
    }

    private String renderMethods(Object objectToRender) {
        final Class classOfObjectToRender = objectToRender.getClass();
        final Method[] methodsToRender = classOfObjectToRender.getDeclaredMethods();

        final StringBuilder builder = new StringBuilder();

        for (final Method method : methodsToRender) {
            if (method.isAnnotationPresent(RenderMe.class) & !methodReturnsVoid(method)) {
                builder.append(renderMethod(method));
            }
        }

        return builder.toString();
    }

    private String renderMethod(Method method) {
        return method.getName() + " returns: " + method.getReturnType() + '\n';
    }

    private boolean methodReturnsVoid(Method method) {
        return method.getReturnType().toString().equals("void");
    }
}
