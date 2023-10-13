# Object Inspector
A simple object inspector for CPSC 501

Instructions and grading rubric can be found at [Assignment2.pdf](Assignment2.pdf)

Unit tests are using the junit testing framework. These can be run by calling `java org.junit.runner.JUnitCore unit_tests.TestDefault` from the project root directory.

An automated test driver has been provided as part of the assignment. This is located in `test_driver/`. The test driver can be executed by running `java Asst2TestDriver inspector.Inspector` in the `test_driver/` directory.

An additional `build.bat` file has been created to help compile the program. This will also copy the `Inspector` class file into the `test_driver` to allow it to run with the most up-to-date version.
