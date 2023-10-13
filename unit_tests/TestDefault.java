package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import inspector.Inspector;
import test_classes.*;

public class TestDefault {
    @Test
    public void Test() {
        Inspector i = new Inspector(true);
        DefaultClass c = new DefaultClass();
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(0), "Declaring Class: test_classes.DefaultClass");
        assertEquals(o.get(1), "Superclass: test_classes.BaseClass");
        assertEquals(o.get(3), "    test_classes.DefaultInterface");
    }
}
