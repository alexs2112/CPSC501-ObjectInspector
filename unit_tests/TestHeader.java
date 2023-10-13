package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import inspector.Inspector;
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
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: test_classes.DefaultClass");
        assertEquals(o.get(1), "Superclass: test_classes.BaseClass");
        assertEquals(o.get(3), "    test_classes.DefaultInterface");
    }

    @Test
    public void TestBlank() {
        BlankClass c = new BlankClass();
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: test_classes.BlankClass");
        assertEquals(o.get(1), "Superclass: java.lang.Object");
        assertEquals(o.get(3), "    <No Interfaces>");
    }

    @Test
    public void TestObject() {
        Object c = new Object();
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: java.lang.Object");
        assertEquals(o.get(1), "Superclass: <No Superclass>");
        assertEquals(o.get(3), "    <No Interfaces>");
    }

    @Test
    public void TestString() {
        String c = "Hello, World!";
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: java.lang.String");
        assertEquals(o.get(1), "Superclass: java.lang.Object");
        assertEquals(o.get(3), "    java.io.Serializable");
        assertEquals(o.get(4), "    java.lang.Comparable");
        assertEquals(o.get(5), "    java.lang.CharSequence");
    }

    @Test
    public void TestPrimitive() {
        Integer c = new Integer(0);
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: java.lang.Integer");
        assertEquals(o.get(1), "Superclass: java.lang.Number");
        assertEquals(o.get(3), "    java.lang.Comparable");
    }
}
