package htwb.ai.FaDen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.cli.MissingArgumentException;
import org.junit.jupiter.api.Test;

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
        assertThrows(org.apache.commons.cli.MissingArgumentException.class, () -> {
            TestRunner.getClassNameFromUser(args); //TODO was machen wir hier am besten
        });
    }

    @Test
    public void getClassNameFromUserMoreThanOneClass() {
        String input = "-c htwb.ai.FaDen.CustomTestClass htwb.ai.FaDen.CustomTestClass";
        String[] args = input.split(" ");
        assertThrows(IllegalArgumentException.class, () -> {
            TestRunner.getClassNameFromUser(args); //TODO klappt auch noch nicht
        });
    }


}
