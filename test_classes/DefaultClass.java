package test_classes;

public class DefaultClass extends BaseClass implements DefaultInterface {

    public DefaultClass() { }
    private DefaultClass(int A) { }

    @Override
    public void methodOne() { }

    @Override
    public String methodTwo(int a) throws RuntimeException {
        return "Hello, World!";
    }
}
