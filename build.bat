javac inspector/*.java
javac test_classes/*.java
javac unit_tests/*.java
javac test_driver/*.java
if not exist test_driver\inspector\ mkdir test_driver\inspector\
copy inspector\*.class test_driver\inspector\
