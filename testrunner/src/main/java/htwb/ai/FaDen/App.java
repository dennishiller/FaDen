package htwb.ai.FaDen;

import htwb.ai.MyTest;
import org.apache.commons.cli.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class App {

    private static Options options = new Options();
    private static CommandLineParser parser = new DefaultParser();
    private static CommandLine cmd;
    private static Class clazz;
    private static Method[] relevantMethods;
    private static Object testingInstance;

    public static void main(String[] args) {
        String className = getClassNameFromUser(args);
        if(className == null) {
            System.out.println("ClassName not provided"); //TODO einzig und alleine noch -c und kein Args wirft noch Fehler
        }

        //try to load class
        try {
            clazz = Class.forName("htwb.ai.FaDen." + className);
            Method[] methods = clazz.getMethods(); // alle Methoden holen -> jetzt filtern
            errorPrintIrrelevantMethods(methods);
            relevantMethods = getAllRelevantMethods(methods);
        } catch (ClassNotFoundException e) {
            System.out.println("Class provided not found");
        }

        try {
            testingInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        boolean result;
        for (Method method : relevantMethods) {
            try {
                result = (boolean) method.invoke(testingInstance);
                if(result) System.out.printf("Result for '%s': passed%n", method.getName());
                else System.out.printf("Result for '%s': failed%n", method.getName());
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.printf("Result for '%s': error due to %s%n", method.getName(), e.getMessage());
            }
        }

        // System.out.println( returnsHelloWorld() );
    }

    private static String getClassNameFromUser(String[] args) {
        Option classOption = Option.builder()
                .longOpt("c")
                .argName("className")
                .hasArg()
                .desc("use given class to test")
                .build();

        options.addOption(classOption);

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(cmd.hasOption("c")) {
            return cmd.getOptionValue("c");
        }
        return null;
    }

    private static void errorPrintIrrelevantMethods(Method[] methods) {
        Arrays.stream(methods)
                .forEach(method -> {
                    if (method.isAnnotationPresent(MyTest.class)) {
                        if(method.getParameterCount() > 0) System.out.printf("Method '%s': can't be tested because there were params %n", method.getName());
                        else if(!method.getReturnType().equals(Boolean.TYPE)) System.out.printf("Method '%s': can't be tested because the return type isn't boolean %n", method.getName());
                        else if(Modifier.isStatic(method.getModifiers())) System.out.printf("Method '%s': can't be tested because it is static %n", method.getName());
                    }
                });
    }

    private static Method[] getAllRelevantMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyTest.class)) //richtig annotiert
                .filter(method -> method.getParameterCount() == 0) //keine Uebergabeparameter
                .filter(method -> method.getReturnType().equals(Boolean.TYPE)) //Boolean Rueckgabewerte
                .filter(method -> !Modifier.isStatic(method.getModifiers())) //non static methods
                .toArray(Method[]::new);
    }
    
    public static String returnsHelloWorld() {
        return "Hello World!";
    }
}
