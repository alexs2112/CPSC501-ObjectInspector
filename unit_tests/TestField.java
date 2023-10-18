package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import inspector.*;
import test_classes.*;

public class TestField {
    private Inspector i;

    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void Test() {
        DefaultClass c = new DefaultClass();
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.fields[0].name, "privateInt");
        assertEquals(o.fields[0].value, "0");
        assertEquals(o.fields[1].name, "protectedInt");
        assertEquals(o.fields[1].value, "0");
        assertEquals(o.fields[4].name, "superProtected");
        assertEquals(o.fields.length, 5);
        for (InspectorField f : o.fields) {
            if (f.name.equals("superPrivate")) {
                fail("Reading private field from superclass");
            }
        }
    }

    @Test
    public void TestBlank() {
        BlankClass c = new BlankClass();
        InspectorOutput o = i.inspectOne(c, false);
        assertNull(o.fields);
    }

    @Test
    public void TestChangeValues() {
        DefaultClass c = new DefaultClass(1, 2, 3);
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.fields[0].value, "1");
        assertEquals(o.fields[1].value, "2");
        assertEquals(o.fields[2].value, "3");
    }

    @Test
    public void TestSuperclassValues() {
        DefaultClass c = new DefaultClass();
        InspectorOutput o = i.inspectOne(c, false);
        assertEquals(o.fields[3].value, "4");
        assertEquals(o.fields[4].value, "5");
    }
}
