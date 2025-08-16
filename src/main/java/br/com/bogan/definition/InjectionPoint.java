package br.com.bogan.definition;

import br.com.bogan.annotations.Qualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Immutable value object describing a single injection site (field or parameter).
 */
public final class InjectionPoint {

    public enum InjectionKind {FIELD, PARAM}

    private final InjectionKind kind;
    private final Class<?> declaringClass;
    private final String memberName;
    private final int position;
    private final Class<?> rawType;
    private final Class<?> typeArgument;
    private final String qualifier;
    private final boolean required;

    private InjectionPoint(InjectionKind kind,
                           Class<?> declaringClass,
                           String memberName,
                           int position,
                           Class<?> rawType,
                           Class<?> typeArgument,
                           String qualifier,
                           boolean required) {
        this.kind = kind;
        this.declaringClass = declaringClass;
        this.memberName = memberName;
        this.position = position;
        this.rawType = rawType;
        this.typeArgument = typeArgument;
        this.qualifier = (qualifier == null || qualifier.isBlank()) ? null : qualifier;
        this.required = required;
    }

    public static InjectionPoint from(Field field) {
        field.setAccessible(true);
        Class<?> raw = field.getType();
        Class<?> arg = extractSingleTypeArgument(field.getGenericType());
        String q = qualifierValue(field.getAnnotations());
        return new InjectionPoint(
                InjectionKind.FIELD,
                field.getDeclaringClass(),
                field.getName(),
                -1,
                raw,
                arg,
                q,
                true
        );
    }

    public static InjectionPoint from(Parameter parameter) {
        Executable exec = parameter.getDeclaringExecutable();
        Class<?> declClass = exec.getDeclaringClass();
        String name = (exec instanceof Constructor) ? "<init>" : ((Method) exec).getName();
        int index = indexOf(exec.getParameters(), parameter);

        Class<?> raw = parameter.getType();
        Class<?> arg = extractSingleTypeArgument(parameter.getParameterizedType());
        String q = qualifierValue(parameter.getAnnotations());

        return new InjectionPoint(
                InjectionKind.PARAM,
                declClass,
                name,
                index,
                raw,
                arg,
                q,
                true
        );
    }

    private static int indexOf(Parameter[] params, Parameter target) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] == target) return i; // identity comparison is reliable here
        }
        return -1; // should not happen
    }

    /**
     * If the Type is parameterized with a single argument (e.g., Provider<T>),
     * returns the Class for T when it is a concrete Class; otherwise returns null.
     */
    private static Class<?> extractSingleTypeArgument(Type genericType) {
        if (genericType instanceof ParameterizedType pt) {
            Type[] args = pt.getActualTypeArguments();
            if (args.length == 1) {
                Type t = args[0];
                // Only handle simple Class<?> for now; leave null for wildcards/type vars
                if (t instanceof Class<?> c) return c;
            }
        }
        return null;
    }

    private static String qualifierValue(Annotation[] annotations) {
        for (Annotation a : annotations) {
            if (a.annotationType() == Qualifier.class) {
                return ((Qualifier) a).value();
            }
        }
        return null;
    }

    public InjectionKind getKind() {
        return kind;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public String getMemberName() {
        return memberName;
    }

    public int getPosition() {
        return position;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public Class<?> getTypeArgument() {
        return typeArgument;
    }

    public String getQualifier() {
        return qualifier;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {
        String pos = (kind == InjectionKind.PARAM ? ("#" + position) : "");
        String arg = (typeArgument != null ? "<" + typeArgument.getSimpleName() + ">" : "");
        String q = (qualifier != null ? " @" + qualifier : "");
        return "InjectionPoint[" + kind + " " + declaringClass.getSimpleName() + "." + memberName + pos +
                " : " + rawType.getSimpleName() + arg + q + ", required=" + required + "]";
    }
}
