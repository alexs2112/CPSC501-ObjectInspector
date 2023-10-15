package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import inspector.*;
import test_classes.*;

public class TestConstructor {
    private Inspector i;
    @Before
    public void setup() {
        i = new Inspector(true);
    }

    @Test
    public void TestConstructors() {
        DefaultClass c = new DefaultClass();
        InspectorOutput o = i.inspect(c, false);
        for (InspectorConstructor ic : o.constructors) {
            if (ic.parameters == null) {
                assertEquals(ic.modifiers, "public");
            } else if (ic.parameters.length == 1) {
                assertEquals(ic.parameters[0], "int");
                assertEquals(ic.modifiers, "private");
            }
        }
    }

    @Test
    public void TestNoConstructors() {
        BlankClass c = new BlankClass();
        InspectorOutput o = i.inspect(c, false);
        assertEquals(o.constructors.length, 1);
        assertEquals(o.constructors[0].modifiers, "public");
    }
}
