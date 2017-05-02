
public class Main {

    public static void main(String[] args) throws Exception {
        WebProfiler wp = new WebProfiler();
        wp.setUp();
        try {
            wp.testElPais();
            wp.showLogs();
        } finally {
            wp.cleanUp();
        }
    }
}
