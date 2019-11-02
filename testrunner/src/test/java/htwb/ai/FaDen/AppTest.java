package htwb.ai.FaDen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class AppTest {


    @Test
    public void returnsHelloWorldShouldReturnHelloWorld() {
        assertEquals( TestRunner.returnsHelloWorld(), "Hello World!" );
    }


    @Test
    public void getClassValidClass() {
        String clazz = "htwb.ai.FaDen.CustomTestClass";
        Class loadedClass = null;
        try{
            loadedClass = TestRunner.loadClass(clazz);
        } catch (Exception e) {}
        assertEquals(CustomTestClass.class, loadedClass);
    }

    @Test
    public void getClassInvalidClass() {
        String clazz = "htwb.ai.FaDen.XDXDXDXD";
        Class loadedClass = null;
        assertThrows(ClassNotFoundException.class, () -> {
            TestRunner.loadClass(clazz);
        });
    }

    @Test
    public void getClassNameFromUserValid() {
        String input = "-c htwb.ai.FaDen.CustomTestClass";
        String[] args = input.split(" ");
        assertEquals("htwb.ai.FaDen.CustomTestClass", TestRunner.getClassNameFromUser(args));
    }

    @Test
    public void getClassNameFromUserNoClass() {
        String input = "-c";
        String[] args = input.split(" ");
        assertThrows(IllegalArgumentException.class, () -> {
            TestRunner.getClassNameFromUser(args);
        });
    }

    @Test
    public void getClassNameFromUserNoArguments() {
        String input = "";
        String[] args = input.split(" ");
        assertThrows(IllegalArgumentException.class, () -> {
            TestRunner.getClassNameFromUser(args);
        });
    }

    @Test
    public void getClassNameFromUserMoreThanOneClass() {
        String input = "-c htwb.ai.FaDen.CustomTestClass htwb.ai.FaDen.CustomTestClass";
        String[] args = input.split(" ");
        assertThrows(IllegalArgumentException.class, () -> {
            TestRunner.getClassNameFromUser(args);
        });
    }

    @Test
    public void getAllRelevantMethodsValidCount() {
        Class clazz = CustomTestClass.class;
        Method[] methods = clazz.getMethods();

        int relevantMethods = 3;
        assertEquals(relevantMethods, TestRunner.getAllRelevantMethods(methods).length);
    }
}