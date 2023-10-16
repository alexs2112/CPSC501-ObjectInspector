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
    public void TestSame() {
        FieldClass a = new FieldClass();
        HashClass c = new HashClass(a, a);
        InspectorOutput o = i.inspect(c, false);

        assertEquals(o.fields[0].value, "test_classes.FieldClass@" + Integer.toString(a.hashCode()));
        assertEquals(o.fields[0].value, o.fields[1].value);
    }

    @Test
    public void TestDifferent() {
        FieldClass a = new FieldClass();
        FieldClass b = new FieldClass();
        HashClass c = new HashClass(a, b);
        InspectorOutput o = i.inspect(c, false);

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
}
