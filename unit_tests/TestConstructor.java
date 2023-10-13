package unit_tests;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import inspector.Inspector;
import test_classes.*;

public class TestConstructor {
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
        assertEquals(o.get(20), "    Constructor 1");
        assertEquals(o.get(22), "            <No Parameters>");
        assertEquals(o.get(23), "        Modifiers: public");
    }
}
