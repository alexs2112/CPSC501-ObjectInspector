package test_classes;

public class ArrayClass {
    public ArrayClass() { }
    public ArrayClass(String[] a) {
        strings = a;
    }
    public int[] TestMethod(int[] b) { return b; }
    public boolean[][] TestMethod2(boolean[][] c) { return c; }

    public String[] strings;
}