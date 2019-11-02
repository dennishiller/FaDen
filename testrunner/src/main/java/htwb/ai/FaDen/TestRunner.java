package htwb.ai.FaDen;

import htwb.ai.MyTest;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class TestRunner {

    private static Options options = new Options();
    private static CommandLineParser parser = new DefaultParser();
    private static CommandLine cmd;
    private static Class clazz;
    private static Method[] relevantMethods;
    private static Object testingInstance;

    public static void main(String[] args) {

        String className = "";

        try{
            className = getClassNameFromUser(args);
        } catch (IllegalArgumentException e) {
            System.out.println("ClassName was not provided");
            System.exit(1);
        }

        //try to load class
        try {
            clazz = loadClass(className); //TODO ohne dingsda auch noch angeben
        } catch (ClassNotFoundException e) {
            System.out.println("Class provided was not found");
        }

        Method[] methods = clazz.getMethods(); // alle Methoden holen -> jetzt filtern

        System.out.println("---------------");
        System.out.println("     ERRORS");
        System.out.println("---------------");

        errorPrintIrrelevantMethods(methods);
        relevantMethods = getAllRelevantMethods(methods);

        try {
            testingInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("---------------");
        System.out.println("     TESTING");
        System.out.println("---------------");

        invokeAndEvaluateMethods();
    }

    protected static Class loadClass(String className) throws ClassNotFoundException{
        return Class.forName(className);
    }

    protected static String getClassNameFromUser(String[] args) throws IllegalArgumentException {
        Option classOption = Option.builder()
                .longOpt("c")
                .argName("className")
                .hasArg()
                .desc("use given class to test")
                .build();

        options.addOption(classOption);

        if (args.length >= 3) throw new IllegalArgumentException("too many arguments");

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }

        if(cmd.hasOption("c")) {
            return cmd.getOptionValue("c");
        }

        throw new IllegalArgumentException(); //throw something if cmd doesn't return value
    }

    protected static void errorPrintIrrelevantMethods(Method[] methods) {
        Arrays.stream(methods)
                .forEach(method -> {
                    if (method.isAnnotationPresent(MyTest.class)) {
                        if(method.getParameterCount() > 0) System.out.printf("Method '%s': can't be tested because there were params %n", method.getName());
                        else if(!method.getReturnType().equals(Boolean.TYPE)) System.out.printf("Method '%s': can't be tested because the return type isn't boolean %n", method.getName());
                        else if(Modifier.isStatic(method.getModifiers())) System.out.printf("Method '%s': can't be tested because it is static %n", method.getName());
                    }
                });
    }


    protected static Method[] getAllRelevantMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class)) //richtig annotiert
                .filter(method -> method.getParameterCount() == 0) //keine Uebergabeparameter
                .filter(method -> method.getReturnType().equals(Boolean.TYPE)) //Boolean Rueckgabewerte
                .filter(method -> !Modifier.isStatic(method.getModifiers())) //non static methods
                .toArray(Method[]::new);
    }

    protected static void invokeAndEvaluateMethods() {
        boolean result;
        for (Method method : relevantMethods) {
            try {
                result = (boolean) method.invoke(testingInstance);
                evaluateMethod(method, result);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.printf("Result for '%s': error due to %s%n", method.getName(), e.getClass().getSimpleName());
            }
        }
    }

    protected static void evaluateMethod(Method method, boolean result) {
        if(result) System.out.printf("Result for '%s': passed%n", method.getName());
        else System.out.printf("Result for '%s': failed%n", method.getName());
    }

    protected static String returnsHelloWorld() {
        return "Hello World!";
    }
}