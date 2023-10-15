package inspector;

import java.lang.reflect.*;
import java.util.ArrayList;

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
        getFields(declaringClass, obj, output);

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

    /* Iterate over *all* fields of the object and print their values */
    private void getFields(Class declaringClass, Object obj, InspectorOutput output) {
        Field[] fs = findFields(declaringClass, false);
        if (fs.length == 0) { return; }

        ArrayList<InspectorField> temp = new ArrayList<InspectorField>();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];   
            String mod = Modifier.toString(f.getModifiers());
            String type = f.getType().getName();
            String name = f.getName();
            try {
                temp.add(new InspectorField(mod, type, name, f.get(obj).toString()));
            } catch (NullPointerException e) {
                // I honestly don't know why f.get() throws an exception if the value is null
                temp.add(new InspectorField(mod, type, name, "null"));
            } catch (IllegalAccessException e) {
                // Private variables are not set in temp
                continue;
            }
        }

        if (temp.size() == 0) { return; }
        InspectorField[] ifs = new InspectorField[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            ifs[i] = temp.get(i);
        }
        output.fields = ifs;
    }

    /* Recursively get all fields of each superclass and return the ones that are public and protected */
    private Field[] findFields(Class c, boolean isSuperclass) {
        Field[] fs = c.getDeclaredFields();

        // protected and private fields are, for some reason, throwing an 
        // IllegalAccessException even when they should be accessible ???
        // Make them accessible because they should already be
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (Modifier.isProtected(f.getModifiers())) {
                f.setAccessible(true);
            }
            if (!isSuperclass && Modifier.isPrivate(f.getModifiers())) {
                f.setAccessible(true);
            }
        }

        if (c.getSuperclass() == null) { return fs; }

        return concat(fs, findFields(c.getSuperclass(), true));
    }

    /* Apparently concatenating two arrays isn't just a default java function... */
    private Field[] concat(Field[] a, Field[] b) {
        int alen = a.length;
        if (alen == 0) { return b; }
        
        int blen = b.length;
        if (blen == 0) { return a; }

        int len = alen + blen;
        Field[] f = new Field[len];
        for (int i = 0; i < alen; i++) {
            f[i] = a[i];
        }
        for (int i = 0; i < blen; i++) {
            f[i+alen] = b[i];
        }
        return f;
    }
}
