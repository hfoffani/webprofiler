
import java.io.*;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Main {

    @Parameter(names = "-verbose", description = "Shows commands line by line")
    boolean verbose = false;

    @Parameter(names = "-driver", description = "Path to chromedriver")
    String driverpath = "./chromedriver";

    @Parameter(names = "-input", description = "Input file name")
    private String input = "-";

    public static void main(String[] argv) throws Exception {

        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    private void run() throws Exception {

        WebProfiler wp = new WebProfiler();
        wp.setUp();
        try {
            if (!input.equals("-")) {
                System.setIn(new FileInputStream(new File(input)));
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.lines()
                    .forEach(wp::RunCommand);
            wp.showLogs();
        } finally {
            wp.cleanUp();
        }
    }
}


