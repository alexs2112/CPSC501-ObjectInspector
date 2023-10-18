package inspector;

public class InspectorOutput {
    public String declaringClass;
    public String objectHash;
    public String superclass;
    public String[] interfaces;
    public InspectorMethod[] methods;
    public InspectorConstructor[] constructors;
    public InspectorField[] fields;

    public void println(String msg) { println(msg, 0); }
    public void println(String msg, int tab) {
        String o = "";
        for (int i = 0; i < tab; i++) { o += "    "; }
        o += msg;
        System.out.println(o);
    }
    public void print() {
        printHeader();
        printMethods();
        printConstructors();
        printFields();
        System.out.println();
    }

    private void printHeader() {
        if (objectHash != null) {
            println("Object Hash: " + objectHash);
        }
        println("Declaring Class: " + declaringClass);

        if (superclass == null) {
            println("<No Superclass>");
        } else {
            println("Superclass: " + superclass);
        }

        if (interfaces != null) {
            println("Interfaces:");
            for (String s : interfaces)
                println(s, 1);
        } else {
            println("<No Interfaces>");
        }
    }

    private void printMethods() {
        if (methods != null) {
            println("Methods:");
            for (InspectorMethod m : methods) {
                println(m.name, 1);
                
                if (m.exceptions != null) {
                    println("Exceptions:", 2);
                    for (String s : m.exceptions)
                        println(s, 3);
                } else {
                    println("<No Exceptions>", 2);
                }

                if (m.parameters != null) {
                    println("Parameters:", 2);
                    for (String s : m.parameters)
                        println(s, 3);
                } else {
                    println("<No Parameters>", 2);
                }

                println("Return Type: " + m.returnType, 2);
                println("Modifiers: " + m.modifiers, 2);
            }
        } else {
            println("<No Methods>");
        }
    }

    private void printConstructors() {
        if (constructors != null) {
            println("Constructors:");
            for (int i = 0; i < constructors.length; i++) {
                InspectorConstructor c = constructors[i];
                println("Constructor " + Integer.toString(i + 1), 1);

                if (c.parameters != null) {
                    println("Parameters:", 2);
                    for (String s : c.parameters)
                        println(s, 3);
                } else {
                    println("<No Parameters>", 2);
                }

                println("Modifiers: " + c.modifiers, 2);
            }
        } else {
            println("<No Constructors>");
        }
    }

    private void printFields() {
        if (fields != null) {
            println("Fields:");
            for (InspectorField f : fields) {
                String s = "";
                if (f.modifiers != null) { s += f.modifiers + " "; }
                if (f.type != null) { s += f.type + " "; }
                if (f.name != null) { s += f.name + " "; }
                if (f.value != null) { s += "= " + f.value; }
                println(s, 1);
            }
        } else {
            println("<No Fields>");
        }
    }
}
