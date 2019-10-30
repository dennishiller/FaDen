package htwb.ai.FaDen;

import org.apache.commons.cli.*;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{

    private static Options options = new Options();
    private static CommandLineParser parser = new DefaultParser();
    private static CommandLine cmd;

    public static void main(String[] args) {
        String className = getClassNameFromUser(args);
        if(className == null) {
            System.out.println("ClassName not provided"); //TODO einzig und alleine noch -c und kein Args wirft noch Fehler
        }



        System.out.println( returnsHelloWorld() );
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
    
    public static String returnsHelloWorld() {
        return "Hello World!";
    }
}
