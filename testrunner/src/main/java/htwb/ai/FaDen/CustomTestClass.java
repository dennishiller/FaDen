package htwb.ai.FaDen;

import htwb.ai.MyTest;

public class CustomTestClass {

    @MyTest
    public boolean passTest() { return true; }

    @MyTest
    public boolean failTest() { return false; }

    @MyTest
    public boolean errorTest() { throw new ArithmeticException(); }

    @MyTest
    public int wrongReturnType() { return 5; }

    @MyTest
    private boolean notPublic() { return true; }

    @MyTest
    public boolean hasParams(boolean var) { return false; }

    @MyTest
    public static boolean staticMethod(boolean var) { return false; }

    public boolean noAnnotation() { return false; }
}