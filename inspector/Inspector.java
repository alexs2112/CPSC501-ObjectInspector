package inspector;

import java.lang.reflect.*;

public class Inspector {
    private boolean testing;

    public Inspector() { /* Default Constructor */ }
    public Inspector(boolean testing) { this.testing = testing; }

    public InspectorOutput inspect(Object obj, boolean recursive) {
        InspectorOutput output = new InspectorOutput();

        Class declaringClass = getDeclaringClass(obj, output);
        getHeader(declaringClass, output);
        getMethods(declaringClass, output);
        getConstructors(declaringClass, output);

        if (!testing) { output.print(); }

        return output;
    }

    /* Get the declaring class of the object */
    private Class getDeclaringClass(Object obj, InspectorOutput output) {
        Class declaringClass = obj.getClass();
        output.declaringClass = declaringClass.getName();
        return declaringClass;
    }

    /* Get the superclass and interfaces the declaringClass implements */
    private void getHeader(Class declaringClass, InspectorOutput output) {
        Class superclass = declaringClass.getSuperclass();
        if (superclass != null) { output.superclass = superclass.getName(); }

        Class[] interfaces = declaringClass.getInterfaces();
        if (interfaces.length > 0) {
            output.interfaces = new String[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                output.interfaces[i] = interfaces[i].getName();
            }
        }
    }

    /* Iterate over all methods that the declaringClass *declares* */
    private void getMethods(Class declaringClass, InspectorOutput output) {
        Method[] methods = declaringClass.getDeclaredMethods();
        if (methods.length == 0) { return; }

        output.methods = new InspectorMethod[methods.length];
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            InspectorMethod inspectorMethod = new InspectorMethod();
            inspectorMethod.name = m.getName();

            Class[] exceptionTypes = m.getExceptionTypes();
            if (exceptionTypes.length > 0) {
                String[] exceptions = new String[exceptionTypes.length];
                for (int j = 0; j < exceptionTypes.length; j++) {
                    exceptions[j] = exceptionTypes[j].getName();
                }
                inspectorMethod.exceptions = exceptions;
            }

            Class[] parameterTypes = m.getParameterTypes();
            if (parameterTypes.length > 0) {
                String[] parameters = new String[parameterTypes.length];
                for (int j = 0; j < parameterTypes.length; j++) {
                    parameters[j] = parameterTypes[j].getName();
                }
                inspectorMethod.parameters = parameters;
            }

            inspectorMethod.returnType = m.getReturnType().getName();
            int mod = m.getModifiers();
            inspectorMethod.modifiers = Modifier.toString(mod);

            output.methods[i] = inspectorMethod;
        }
    }

    /* Iterate over all methods that the declaringClass *declares* */
    private void getConstructors(Class declaringClass, InspectorOutput output) {
        Constructor[] constructors = declaringClass.getDeclaredConstructors();
        if (constructors.length == 0) { return; }

        output.constructors = new InspectorConstructor[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            Constructor c = constructors[i];
            InspectorConstructor inspectorConstructor = new InspectorConstructor();

            Class[] parameterTypes = c.getParameterTypes();
            if (parameterTypes.length > 0) {
                String[] parameters = new String[parameterTypes.length];
                for (int j = 0; j < parameterTypes.length; j++) {
                    parameters[j] = parameterTypes[j].getName();
                }
                inspectorConstructor.parameters = parameters;
            }

            int mod = c.getModifiers();
            inspectorConstructor.modifiers = Modifier.toString(mod);

            output.constructors[i] = inspectorConstructor;
        }
    }
}
