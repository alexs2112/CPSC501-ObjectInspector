package inspector;

import java.util.ArrayList;

public class Inspector {
    private boolean testing;
    private ArrayList<String> output;

    public Inspector() { /* Default Constructor */ }
    public Inspector(boolean testing) { this.testing = testing; }

    public ArrayList<String> inspect(Object obj, boolean recursive) {
        // Clear the output at the start of a new test
        output = new ArrayList<String>();

        Class declaringClass = getDeclaringClass(obj);
        get_header(declaringClass);

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
    private void get_header(Class declaringClass) {
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
}
