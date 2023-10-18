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
        InspectorOutput o = i.inspectOne(c, false);
        for (InspectorMethod m : o.methods) {
            if (m.name.equals("TestMethod")) {
                assertEquals(m.parameters[0], "Array[int]");
                assertEquals(m.returnType, "Array[int]");
            } else if (m.name.equals("TestMethod2")) {
                assertEquals(m.parameters[0], "Array[Array[boolean]]");
                assertEquals(m.returnType, "Array[Array[boolean]]");
            }
        }
    }

    @Test
    public void TestPopulatedField() {
        ArrayClass c = new ArrayClass(new int[] {0, 1, 2, 3, 4});
        InspectorOutput o = i.inspectOne(c, false);
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
        InspectorOutput o = i.inspectOne(c, false);
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
        InspectorOutput o = i.inspectOne(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("strings")) {
                assertEquals(f.type, "Array[java.lang.String]");
                assertEquals(f.value, "[null, null, null](len=3)");
            }
        }
    }

    @Test
    public void TestDoubleArray() {
        ArrayClass c = new ArrayClass();
        c.doubleArray = new int[3][2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                c.doubleArray[i][j] = i + j;
            }
        }
        InspectorOutput o = i.inspectOne(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("doubleArray")) {
                assertEquals(f.type, "Array[Array[int]]");
                assertEquals(f.value, "[[0, 1](len=2), [1, 2](len=2), [2, 3](len=2)](len=3)");
            }
        }
    }

    @Test
    public void TestTripleArray() {
        ArrayClass c = new ArrayClass();
        c.tripleArray = new int[3][2][2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    c.tripleArray[i][j][k] = i + j + k;
                }
            }
        }

        InspectorOutput o = i.inspectOne(c, false);
        for (InspectorField f : o.fields) {
            if (f.name.equals("tripleArray")) {
                assertEquals(f.type, "Array[Array[Array[int]]]");
                assertEquals(f.value, "[[[0, 1](len=2), [1, 2](len=2)](len=2), [[1, 2](len=2), [2, 3](len=2)](len=2), [[2, 3](len=2), [3, 4](len=2)](len=2)](len=3)");
            }
        }
    }

    @Test
    public void TestSingleArrayObject() {
        int[] c = new int[] { 1, 2, 3, 4, 5 };
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.fields[0].name, "length");
        assertEquals(o.fields[0].value, "5");
        for (int i = 1; i < 6; i++) {
            assertEquals(o.fields[i].name, "[" + Integer.toString(i - 1) +"]");
            assertEquals(o.fields[i].value, Integer.toString(i));
        }
    }

    @Test
    public void TestDoubleArrayObject() {
        int[][] c = new int[][] { 
            new int[] {0, 1, 2},
            new int[] {3, 4, 5},
            new int[] {6, 7, 8}
        };
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.fields[0].name, "length");
        assertEquals(o.fields[0].value, "3");
        assertEquals(o.fields[1].name, "[0]");
        assertEquals(o.fields[1].value, "[0, 1, 2](len=3)");
        assertEquals(o.fields[2].name, "[1]");
        assertEquals(o.fields[2].value, "[3, 4, 5](len=3)");
        assertEquals(o.fields[3].name, "[2]");
        assertEquals(o.fields[3].value, "[6, 7, 8](len=3)");
    }
}
