package test_classes;

public class DefaultClass extends BaseClass implements DefaultInterface {

    @Override
    public void methodOne() { }

    @Override
    public String methodTwo() {
        return "Hello, World!";
    }
}
