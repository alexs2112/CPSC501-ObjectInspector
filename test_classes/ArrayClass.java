package test_classes;

public class ArrayClass {
    public ArrayClass() { }
    public ArrayClass(String[] a) { strings = a; }
    public ArrayClass(int[] a) { ints = a; }

    public int[] TestMethod(int[] b) { return b; }
    public boolean[][] TestMethod2(boolean[][] c) { return c; }

    public int[] ints;
    public String[] strings;
}
