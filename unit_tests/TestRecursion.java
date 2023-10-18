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
}
