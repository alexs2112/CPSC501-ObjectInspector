package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import inspector.*;
import test_classes.*;

public class TestMethod {
    private Inspector i;
    @Before
    public void setup() {
        i = new Inspector(true);
    }

    /* This tests can, and should, be cleaned up */
    @Test
    public void Test() {
        DefaultClass c = new DefaultClass();
        InspectorOutput o = i.inspect(c, false);
        for (InspectorMethod m : o.methods) {
            if (m.name.equals("methodOne")) {
                assertNull(m.exceptions);
                assertNull(m.parameters);
                assertEquals(m.returnType, "void");
                assertEquals(m.modifiers, "public");
            } else {
                assertEquals(m.exceptions[0], "java.lang.RuntimeException");
                assertEquals(m.parameters[0], "int");
                assertEquals(m.returnType, "java.lang.String");
                assertEquals(m.modifiers, "public");
            }
        }
    }
}
