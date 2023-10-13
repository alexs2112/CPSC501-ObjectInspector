package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import inspector.Inspector;
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
        ArrayList<String> o = i.inspect(c, false);
        assertEquals(o.get(5), "    methodTwo");
        assertEquals(o.get(7), "            <No Exceptions>");
        assertEquals(o.get(9), "            <No Parameters>");
        assertEquals(o.get(10), "        Return Type: java.lang.String");
        assertEquals(o.get(11), "        Modifiers: public");
    }
}
