package org.foffani;

public class Main {

    public static void main(String[] args) throws Exception {
        WebProfiler wp = new WebProfiler();
        wp.setUp();
        try {
            wp.testGoogleSearch();
            wp.showLogs();
        } finally {
            wp.tearDown();
        }
    }
}
