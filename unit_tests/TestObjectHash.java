package unit_tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import inspector.*;
import test_classes.*;

public class TestObjectHash {
    private Inspector i;

    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void TestHeader() {
        FieldClass a = new FieldClass();
        String hashCode = Integer.toString(a.hashCode());
        InspectorOutput o = i.inspectOne(a, false);
        assertEquals(o.objectHash, hashCode);
    }

    @Test
    public void TestSame() {
        FieldClass a = new FieldClass();
        HashClass c = new HashClass(a, a);
        InspectorOutput o = i.inspectOne(c, false);

        assertEquals(o.fields[0].value, "test_classes.FieldClass@" + Integer.toString(a.hashCode()));
        assertEquals(o.fields[0].value, o.fields[1].value);
    }

    @Test
    public void TestDifferent() {
        FieldClass a = new FieldClass();
        FieldClass b = new FieldClass();
        HashClass c = new HashClass(a, b);
        InspectorOutput o = i.inspectOne(c, false);

        for (int i = 0; i < 2; i++) {
            if (o.fields[i].value.equals("test_classes.FieldClass@" + Integer.toString(a.hashCode()))) {
                assertEquals(o.fields[(i + 1) % 2].value, "test_classes.FieldClass@" + Integer.toString(b.hashCode()));
            } else if (o.fields[i].value.equals("test_classes.FieldClass@" + Integer.toString(b.hashCode()))) {
                assertEquals(o.fields[(i + 1) % 2].value, "test_classes.FieldClass@" + Integer.toString(a.hashCode()));
            } else {
                fail();
            }
        }

        assertFalse(o.fields[0].value.equals(o.fields[1].value));
    }

    @Test
    public void TestAllHashes() {
        FieldClass a = new FieldClass();
        FieldClass b = new FieldClass();
        HashClass c = new HashClass(a, b);
        String aHash = Integer.toString(a.hashCode());
        String bHash = Integer.toString(b.hashCode());
        InspectorOutput aOut = i.inspectOne(a, false);
        InspectorOutput bOut = i.inspectOne(b, false);
        InspectorOutput cOut = i.inspectOne(c, false);

        for (InspectorField f : cOut.fields) {
            if (f.name.equals("A")) {
                String code = f.value.split("@")[1];
                assertEquals(aOut.objectHash, code);
            } else if (f.name.equals("B")) {
                String code = f.value.split("@")[1];
                assertEquals(bOut.objectHash, code);
            } else {
                fail();
            }
        }

    }
}
