package inspector;

import java.util.ArrayList;
import java.lang.reflect.*;

public class Inspector {
    private boolean testing;
    private ArrayList<String> output;

    public Inspector() { /* Default Constructor */ }
    public Inspector(boolean testing) { this.testing = testing; }

    public ArrayList<String> inspect(Object obj, boolean recursive) {
        // Clear the output at the start of a new test
        output = new ArrayList<String>();

        Class declaringClass = getDeclaringClass(obj);
        getHeader(declaringClass);
        getMethods(declaringClass);

        if (!testing) {
            for (String s : output) { System.out.println(s); }
        }

        return output;
    }

    /* Get the declaring class of the object */
    private Class getDeclaringClass(Object obj) {
        String text;
        Class declaringClass = obj.getClass();
        text = declaringClass.getName();
        output.add("Declaring Class: " + text);
        return declaringClass;
    }

    /* Get the superclass and interfaces the declaringClass implements */
    private void getHeader(Class declaringClass) {
        String text;
        Class superclass = declaringClass.getSuperclass();
        if (superclass == null) { text = "<No Superclass>"; }
        else { text = superclass.getName(); }
        output.add("Superclass: " + text);

        Class[] interfaces = declaringClass.getInterfaces();
        output.add("Interfaces:");
        if (interfaces.length > 0) {
            for (Class i : interfaces) {
                output.add("    " + i.getName());
            }
        } else {
            output.add("    <No Interfaces>");
        }
    }

    /* Iterate over all methods that the declaringClass *declares* */
    private void getMethods(Class declaringClass) {
        output.add("Methods:");
        for (Method m : declaringClass.getDeclaredMethods()) {
            output.add("    " + m.getName());
            output.add("        Exceptions:");

            if (m.getExceptionTypes().length > 0) {
                for (Class e : m.getExceptionTypes()) {
                    output.add("            " + e.getName());
                }
            } else {
                output.add("            <No Exceptions>");
            }

            output.add("        Parameter Types:");
            if (m.getParameterTypes().length > 0) {
                for (Class p : m.getParameterTypes()) {
                    output.add("            " + p.getName());
                }
            } else {
                output.add("            <No Parameters>");
            }

            output.add("        Return Type: " + m.getReturnType().getName());

            int mod = m.getModifiers();
            output.add("        Modifiers:" + Modifier.toString(mod));
        }
    }
}
