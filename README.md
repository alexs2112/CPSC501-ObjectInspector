# Object Inspector
A simple object inspector for CPSC 501

Instructions and grading rubric can be found at [Assignment2.pdf](Assignment2.pdf)

Unit tests are using the junit testing framework. These can be run by calling `java org.junit.runner.JUnitCore unit_tests.TestDefault` from the project root directory.

An automated test driver has been provided as part of the assignment. This is located in `test_driver/`. The test driver can be executed by running `java Asst2TestDriver inspector.Inspector` in the `test_driver/` directory.

An additional `build.bat` file has been created to help compile the program. This will also copy the `Inspector` class file into the `test_driver` to allow it to run with the most up-to-date version. This can be run by simply calling `build` in the project root directory.

An additional `test.bat` file has been created to help with testing the program. This runs both the `test_driver` and the `junit` tests. This can be run by calling `test` from the project root directory. This will put the output from `test_driver` into `test_driver/script.txt`.
