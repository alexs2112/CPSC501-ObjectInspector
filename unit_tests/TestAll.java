package unit_tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestHeader.class, TestMethod.class, TestConstructor.class, TestField.class })
public class TestAll { }
