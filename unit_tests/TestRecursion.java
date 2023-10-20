package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import inspector.*;
import test_classes.*;

public class TestRecursion {
    private Inspector i;

    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void TestOutputSuperclassInterface() {
        DefaultClass c = new DefaultClass(1, 2, 3);
        InspectorOutput[] o = i.inspect(c, false);
        assertEquals(o.length, 4);
        assertEquals(o[0].declaringClass, "test_classes.DefaultClass");
        assertEquals(o[1].declaringClass, "test_classes.BaseClass");
        assertEquals(o[2].declaringClass, "test_classes.DefaultInterface");
        assertEquals(o[3].declaringClass, "java.lang.Object");
    }

    @Test
    public void TestMultipleObjects() {
        BlankClass a = new BlankClass();
        BlankClass b = new BlankClass();
        HashClass c = new HashClass(a, b);

        InspectorOutput[] o = i.inspect(c, true);
        assertEquals(o.length, 4); // c, a, b, java.lang.Object
        String[] hashes = new String[] {
            o[0].fields[0].value.split("@")[1],
            o[0].fields[1].value.split("@")[1]
        };
        for (String h : hashes) {
            boolean passed = false;
            for (InspectorOutput io : o) {
                if (h.equals(io.objectHash)) {
                    passed = true;
                    break;
                }
            }
            if (!passed) { fail(); }
        }
    }

    @Test
    public void TestSameObject() {
        BlankClass a = new BlankClass();
        HashClass c = new HashClass(a, a);

        InspectorOutput[] o = i.inspect(c, true);
        assertEquals(o.length, 3); // c, a, java.lang.Object
    }

    @Test
    public void TestSingleNest() {
        BlankClass a = new BlankClass();
        DefaultClass b = new DefaultClass(1, 2, 3);
        HashClass c = new HashClass(a, b);

        InspectorOutput[] o = i.inspect(c, true);
        assertEquals(o.length, 6); // c, a, b, java.lang.Object, BaseClass, DefaultInterface

        for (String s : new String[] {
            "test_classes.HashClass",
            "test_classes.BlankClass",
            "test_classes.DefaultClass",
            "java.lang.Object",
            "test_classes.BaseClass",
            "test_classes.DefaultInterface"
        }) {
            boolean passed = false;
            for (InspectorOutput io : o) {
                if (io.declaringClass.equals(s)) { passed = true; break; }
            }
            if (!passed) { fail(); }
        }
    }

    @Test
    public void TestDoubleNest() {
        BlankClass a = new BlankClass();
        DefaultClass b = new DefaultClass(1, 2, 3);
        HashClass c = new HashClass(a, b);
        HashClass d = new HashClass(a, c);

        InspectorOutput[] o = i.inspect(d, true);
        assertEquals(o.length, 7); // d, a, c, b, java.lang.Object, BaseClass, DefaultInterface
    }
}
