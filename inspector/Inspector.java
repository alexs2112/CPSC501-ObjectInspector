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
        addLine("Declaring Class: " + text);
        return declaringClass;
    }

    /* Get the superclass and interfaces the declaringClass implements */
    private void getHeader(Class declaringClass) {
        String text;
        Class superclass = declaringClass.getSuperclass();
        if (superclass == null) { text = "<No Superclass>"; }
        else { text = superclass.getName(); }
        addLine ("Superclass: " + text);

        Class[] interfaces = declaringClass.getInterfaces();
        addLine("Interfaces:");
        if (interfaces.length > 0) {
            for (Class i : interfaces) {
                addLine(i.getName(), 1);
            }
        } else {
            addLine("<No Interfaces>", 1);
        }
    }

    /* Iterate over all methods that the declaringClass *declares* */
    private void getMethods(Class declaringClass) {
        addLine("Methods:");
        for (Method m : declaringClass.getDeclaredMethods()) {
            addLine(m.getName(), 1);
            addLine("Exceptions:", 2);

            if (m.getExceptionTypes().length > 0) {
                for (Class e : m.getExceptionTypes()) {
                    addLine(e.getName(), 3);
                }
            } else {
                addLine("<No Exceptions>", 3);
            }

            addLine("Parameter Types:", 2);
            if (m.getParameterTypes().length > 0) {
                for (Class p : m.getParameterTypes()) {
                    addLine(p.getName(), 3);
                }
            } else {
                addLine("<No Parameters>", 3);
            }

            addLine("Return Type: " + m.getReturnType().getName(), 2);

            int mod = m.getModifiers();
            addLine("Modifiers: " + Modifier.toString(mod), 2);
        }
    }

    /* Helper method for string formatting to make the output human-readable */
    private void addLine(String s) { addLine(s, 0); }
    private void addLine(String s, int tab) {
        String o = "";
        for (int i = 0; i < tab; i++) { o += "    "; }
        o += s;
        output.add(o);
    }
}
