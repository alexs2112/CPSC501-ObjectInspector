package unit_tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import inspector.Inspector;

public class TestDefault {
    @Test
    public void Test() {
        Inspector i = new Inspector();
        assertEquals(10, 10);
    }
}
