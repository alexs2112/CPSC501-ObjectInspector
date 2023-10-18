package inspector;

import java.lang.reflect.*;
import java.util.ArrayList;

public class Inspector {
    private boolean testing;
    private boolean recursive;
    private ArrayList<InspectorOutput> outputs;
    private ArrayList<Object> objectQueue;
    private ArrayList<Class> classQueue;

    public Inspector() { /* Default Constructor */ }
    public Inspector(boolean testing) { this.testing = testing; }

    private void initializeLists() {
        outputs = new ArrayList<InspectorOutput>();
        objectQueue = new ArrayList<Object>();
        classQueue = new ArrayList<Class>();
    }

    public InspectorOutput[] inspect(Object obj, boolean recursive) {
        this.recursive = recursive;
        initializeLists();

        addObject(obj, true);

        while (!objectQueue.isEmpty()) {
            // Pop the first object from the object queue and inspect it
            Object o = objectQueue.get(0);
            objectQueue.remove(0);
            outputs.add(inspectObject(o));
        }
        while (!classQueue.isEmpty()) {
            // Pop the first class from the class queue and inspect it
            Class c = classQueue.get(0);
            classQueue.remove(0);
            outputs.add(inspectClass(c));
        }

        if (!testing) {
            System.out.println("****************************************************");
            for (InspectorOutput o : outputs) {
                o.print();
            }
            System.out.println("****************************************************");
        }

        InspectorOutput[] output = new InspectorOutput[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            output[i] = outputs.get(i);
        }
        return output;
    }

    /* Return only the first object of inspect, ie the object provided in the params */
    public InspectorOutput inspectOne(Object obj, boolean recursive) {
        return inspect(obj, recursive)[0];
    }

    /* Inspect a given object, returning the inspector output */
    private InspectorOutput inspectObject(Object obj) {
        InspectorOutput output = new InspectorOutput();

        Class declaringClass = getDeclaringClass(obj, output);
        getHeader(declaringClass, obj, output);
        getMethods(declaringClass, output);
        getConstructors(declaringClass, output);
        getFields(declaringClass, obj, output);
        
        if (declaringClass.isArray()) {
            getArrayFields(declaringClass, obj, output);
        }
        return output;
    }

    /* Inspect a given class by instantiating the object and inspecting it */
    private InspectorOutput inspectClass(Class c) {
        InspectorOutput output = new InspectorOutput();

        output.declaringClass = getClassName(c);
        getHeader(c, output);
        getMethods(c, output);
        getConstructors(c, output);

        // If setting the temp instance fails, leave it as null (its an interface, don't need to handle the exceptions)
        Object tempInstance = null;
        try {
            tempInstance = Class.forName(c.getName()).newInstance();
        } catch(ClassNotFoundException e) {
        } catch(InstantiationException e) {
        } catch(IllegalAccessException e) {
        }

        getFields(c, tempInstance, output);

        return output;
    }

    /* Get the declaring class of the object */
    private Class getDeclaringClass(Object obj, InspectorOutput output) {
        Class declaringClass = obj.getClass();
        output.declaringClass = getClassName(declaringClass);
        return declaringClass;
    }

    /* Get the superclass and interfaces the declaringClass implements */
    private void getHeader(Class declaringClass, InspectorOutput output) { getHeader(declaringClass, null, output); }
    private void getHeader(Class declaringClass, Object obj, InspectorOutput output) {
        if (obj != null) { output.objectHash = Integer.toString(obj.hashCode()); }
        Class superclass = declaringClass.getSuperclass();
        if (superclass != null) {
            output.superclass = superclass.getName();
            addClass(superclass);
        }

        Class[] interfaces = declaringClass.getInterfaces();
        if (interfaces.length > 0) {
            output.interfaces = new String[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                output.interfaces[i] = interfaces[i].getName();
                addClass(interfaces[i]);
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
                    parameters[j] = getClassName(parameterTypes[j]);
                }
                inspectorMethod.parameters = parameters;
            }

            inspectorMethod.returnType = getClassName(m.getReturnType());
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
                    parameters[j] = getClassName(parameterTypes[j]);
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
            Object value;
            try {
                value = f.get(obj);
            } catch (NullPointerException e) {
                // I honestly don't know why f.get() throws an exception if the value is null
                value = null;
            } catch (IllegalAccessException e) {
                // Private variables are not set in temp
                continue;
            }

            if (f.getType().isArray()) {
                temp.add(parseArrayField(f, value));
            } else {
                temp.add(parseNormalField(f, value));
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

    /* Get the values of a given non-array field */
    private InspectorField parseNormalField(Field field, Object value) {
        String mod = Modifier.toString(field.getModifiers());
        String type = field.getType().getName();
        String name = field.getName();

        String valueString;
        if (value == null) {
            valueString = "null";
        } else {
            valueString = getObjectName(field.getType(), value);
        }

        return new InspectorField(mod, type, name, valueString);
    }

    /* Get the array values of a given field */
    private InspectorField parseArrayField(Field field, Object value) {
        Class c = field.getType();
        String mod = Modifier.toString(c.getModifiers());
        String type = getClassName(c);
        String name = field.getName();
        String valueString;
        if (value == null) {
            valueString = "null";
        } else {
            valueString = getArrayString(c.getComponentType(), value);
        }

        return new InspectorField(mod, type, name, valueString);
    }

    private String getArrayString(Class componentType, Object array) {
        int length = Array.getLength(array);
        String out = "[";
        for (int i = 0; i < length; i++) {
            Object o = Array.get(array, i);
            if (o == null) { out += "null"; }
            else {
                if (componentType.isArray()) { out += getArrayString(componentType.getComponentType(), o); }
                else { out += getObjectName(componentType, o); }
            }

            if (i == length - 1) {
                out += "](len=" + Integer.toString(length) + ")";
            } else {
                out += ", ";
            }
        }
        return out;
    }

    /* If a value of a field is a reference to an object, return the hashcode of that object */
    private String getObjectName(Class type, Object value) {
        if (type.isPrimitive()) { return value.toString(); }
        
        return value.getClass().getName().toString() + "@" + Integer.toString(value.hashCode());
    }

    private void getArrayFields(Class declaringClass, Object obj, InspectorOutput output) {
        int length = Array.getLength(obj);
        InspectorField lengthField = new InspectorField(null, "int", "length", Integer.toString(length));
        
        output.fields = new InspectorField[length + 1];
        output.fields[0] = lengthField;
        
        for (int i = 0; i < length; i++) {
            String text;
            Object o = Array.get(obj, i);
            if (o == null) { text = "null"; }
            else {
                if (declaringClass.getComponentType().isArray()) { text = getArrayString(declaringClass.getComponentType().getComponentType(), o); }
                else { text = getObjectName(declaringClass.getComponentType(), o); }
            }
            InspectorField valueField = new InspectorField(null, getClassName(declaringClass.getComponentType()), "[" + Integer.toString(i) + "]", text);
            output.fields[i + 1] = valueField;
        }
    }

    /* Get the classname of a class, parsing it if its an array */
    private String getClassName(Class c) {
        if (c.isArray()) {
            Class e = c.getComponentType();
            String name;
            if (e.isArray()) name = getClassName(e);
            else name = e.getName();
            return "Array[" + name + "]";
        } else {
            return c.getName();
        }
    }

    /* Add an object to inspect to the object stack if valid */
    private void addObject(Object obj) { addObject(obj, false); }
    private void addObject(Object obj, boolean force) {
        if (force) { objectQueue.add(obj); return; }
        if (!recursive) { return; }
        for (InspectorOutput o : outputs) {
            if (o.objectHash.equals(Integer.toString(obj.hashCode()))) { return; }
        }
        objectQueue.add(obj);
    }

    /* Add a class to inspect to the class stack if valid */
    private void addClass(Class c) {
        for (InspectorOutput o : outputs) {
            // Classes have a null object hash
            if (o.objectHash != null) { continue; }
            if (o.declaringClass.equals(c.getName())) { return; }
        }
        classQueue.add(c);
    }
}
