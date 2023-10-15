package test_classes;

public class DefaultClass extends BaseClass implements DefaultInterface {
    private int privateInt;
    protected int protectedInt;
    public int publicInt;

    public DefaultClass() { }
    public DefaultClass(int A, int B, int C) { privateInt = A; protectedInt = B; publicInt = C;}
    private DefaultClass(int A) { }

    @Override
    public void methodOne() { }

    @Override
    public String methodTwo(int a) throws RuntimeException {
        return "Hello, World!";
    }
}
