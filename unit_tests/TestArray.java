package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import inspector.*;
import test_classes.*;

public class TestArray {
    private Inspector i;

    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void Test() {
        ArrayClass c = new ArrayClass();
        InspectorOutput o = i.inspect(c, false);
        for (InspectorConstructor ic : o.constructors) {
            if (ic.parameters != null) {
                assertEquals(ic.parameters[0], "Array[java.lang.String]");
            }
        }

        for (int i = 0; i < 2; i++) {
            InspectorMethod m = o.methods[i];
            if (m.parameters[0].equals("Array[int]")) {
                assertEquals(m.returnType, "Array[int]");
            } else if (m.parameters[0].equals("Array[Array[boolean]]")) {
                assertEquals(m.returnType, "Array[Array[boolean]]");
            } else {
                fail();
            }
        }
    }

    @Test
    public void TestPopulatedField() {
        ArrayClass c = new ArrayClass(new String[] {"a", "b", "c", "d", "e"});
        InspectorOutput o = i.inspect(c, false);
        assertEquals(o.fields[0].type, "Array[java.lang.String]");
        assertEquals(o.fields[0].value, "[a, b, c, d, e](len=5)");
    }

    @Test
    public void TestEmptyField() {
        ArrayClass c = new ArrayClass();
        InspectorOutput o = i.inspect(c, false);
        assertEquals(o.fields[0].type, "Array[java.lang.String]");
        assertEquals(o.fields[0].value, "null");
    }

    @Test
    public void TestPopulatedNull() {
        ArrayClass c = new ArrayClass(new String[3]);
        InspectorOutput o = i.inspect(c, false);
        assertEquals(o.fields[0].type, "Array[java.lang.String]");
        assertEquals(o.fields[0].value, "[null, null, null](len=3)");
    }
}
