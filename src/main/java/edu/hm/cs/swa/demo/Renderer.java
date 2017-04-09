package edu.hm.cs.swa.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Standard Renderer for the @RenderMe annotation.
 * Renders Objects into Strings.
 */
public class Renderer {

    private final Object objectToRender;

    /**
     * Standard Constructor.
     * @param objectToRender The object that is to be rendered.
     */
    public Renderer(Object objectToRender) {
        if (objectToRender == null) {
            throw new IllegalArgumentException("");
        }

        this.objectToRender = objectToRender;
    }

    /**
     * Renders the object that was given in the constructor.
     * @return Rendered object.
     * @throws ClassNotFoundException class could not be found
     * @throws NoSuchMethodException method could not be found
     * @throws InvocationTargetException method could not be invoked
     * @throws InstantiationException instantiation failed
     * @throws IllegalAccessException access failed
     */
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

    /**
     * Renders the name in of an object in the "Instance of TYPE" format.
     * @param objectToRender object to be rendered.
     * @return Rendered String.
     */
    private String renderInstanceName(Object objectToRender) {
        final Class classOfObjectToRender = objectToRender.getClass();
        return "Instance of " + classOfObjectToRender.getCanonicalName() + ":\n";
    }

    /**
     * Renders all fields of an object that are annotated with @RenderMe
     * @param objectToRender the object which fields are to be rendered.
     * @return Rendered fields.
     * @throws ClassNotFoundException class could not be found
     * @throws NoSuchMethodException method could not be found
     * @throws InvocationTargetException method could not be invoked
     * @throws InstantiationException instantiation failed
     * @throws IllegalAccessException access failed
     */
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

    /**
     * Renders a field using a Renderer.
     * @param field the field to be rendered.
     * @return the rendered field.
     * @throws ClassNotFoundException class could not be found
     * @throws NoSuchMethodException method could not be found
     * @throws InvocationTargetException method could not be invoked
     * @throws InstantiationException instantiation failed
     * @throws IllegalAccessException access failed
     */
    private String renderField(Field field)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        final RenderMe annotation = field.getAnnotation(RenderMe.class);
        final String rendererToUse = annotation.with();

        return rendererToUse.equals("")
                ? useStandardFieldRenderer(field)
                : useExternalFieldRenderer(rendererToUse, field);
    }

    /**
     * Render a field using the standard renderer.
     * @param field The field to be rendered.
     * @return the rendered field.
     * @throws IllegalAccessException access failed
     */
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

    /**
     * Returns the type of a field as a String.
     * @param field The field of which the type is wanted.
     * @return type of tje field as a String.
     */
    private String getTypeOfField(Field field) {
        return field.getType().isPrimitive()
                ? field.getType().getSimpleName()
                : field.getType().getCanonicalName();
    }

    /**
     * Uses an external Renderer to render a field.
     * @param externalRenderer the Renderer to be used.
     * @param field the field to be rendered.
     * @return the rendered field.
     * @throws ClassNotFoundException class could not be found
     * @throws NoSuchMethodException method could not be found
     * @throws InvocationTargetException method could not be invoked
     * @throws InstantiationException instantiation failed
     * @throws IllegalAccessException access failed
     */
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

    /**
     * Renders all methods without arguments that are annotated with @RenderMe and do not return void.
     * @param objectToRender the object which methods are to be rendered.
     * @return the rendered methods.
     */
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

    /**
     * Renders a method.
     * @param method the method to be rendered.
     * @return the rendered Method.
     */
    private String renderMethod(Method method) {
        return method.getName() + " returns: " + method.getReturnType() + '\n';
    }

    /**
     * Checks whether a method returns void.
     * @param method The method in question.
     * @return whether the method returns void.
     */
    private boolean methodReturnsVoid(Method method) {
        return method.getReturnType().toString().equals("void");
    }
}
