package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import inspector.*;
import test_classes.*;

public class TestHeader {
    private Inspector i;
    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void Test() {
        DefaultClass c = new DefaultClass();
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.declaringClass, "test_classes.DefaultClass");
        assertEquals(o.superclass, "test_classes.BaseClass");
        assertEquals(o.interfaces[0], "test_classes.DefaultInterface");
    }

    @Test
    public void TestBlank() {
        BlankClass c = new BlankClass();
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.declaringClass, "test_classes.BlankClass");
        assertEquals(o.superclass, "java.lang.Object");
        assertNull(o.interfaces);
    }

    @Test
    public void TestObject() {
        Object c = new Object();
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.declaringClass, "java.lang.Object");
        assertNull(o.superclass);
        assertNull(o.interfaces);
    }

    @Test
    public void TestString() {
        String c = "Hello, World!";
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.declaringClass, "java.lang.String");
        assertEquals(o.superclass, "java.lang.Object");
        assertEquals(o.interfaces, new String[] { "java.io.Serializable", "java.lang.Comparable", "java.lang.CharSequence" });
    }

    @Test
    public void TestPrimitive() {
        Integer c = new Integer(0);
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.declaringClass, "java.lang.Integer");
        assertEquals(o.superclass, "java.lang.Number");
        assertEquals(o.interfaces[0], "java.lang.Comparable");
    }
}
