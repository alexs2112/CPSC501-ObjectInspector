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
    public void TestMethods() {
        ArrayClass c = new ArrayClass();
        InspectorOutput o = i.inspect(c, false);
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
        ArrayClass c = new ArrayClass(new int[] {0, 1, 2, 3, 4});
        InspectorOutput o = i.inspect(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("ints")) {
                assertEquals(f.type, "Array[int]");
                assertEquals(f.value, "[0, 1, 2, 3, 4](len=5)");
            }
        }
    }

    @Test
    public void TestEmptyField() {
        ArrayClass c = new ArrayClass();
        InspectorOutput o = i.inspect(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("strings")) {
                assertEquals(f.type, "Array[java.lang.String]");
                assertEquals(f.value, "null");
            }
        }
    }

    @Test
    public void TestPopulatedNull() {
        ArrayClass c = new ArrayClass(new String[3]);
        InspectorOutput o = i.inspect(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("strings")) {
                assertEquals(f.type, "Array[java.lang.String]");
                assertEquals(f.value, "[null, null, null](len=3)");
            }
        }
    }
}
