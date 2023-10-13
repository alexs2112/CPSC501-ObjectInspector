package test_classes;

public class DefaultClass extends BaseClass implements DefaultInterface {

    public DefaultClass() { }
    public DefaultClass(int A) { }

    @Override
    public void methodOne() { }

    @Override
    public String methodTwo() {
        return "Hello, World!";
    }
}
