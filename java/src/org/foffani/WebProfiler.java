package org.foffani;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.json.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;

public class WebProfiler {

    private static final String WEBDRIVER_SERVER_URL = "http://localhost:9515/";

    private WebDriver driver;

    public void testGoogleSearch() throws Exception {
        driver.get("http://www.elpais.com");
        WebElement element = driver.findElement(By.id("boton_buscador"));
        element.click();
        element = driver.findElement(By.name("qt"));
        element.sendKeys("psd2");
        element.sendKeys(Keys.RETURN);
        element = driver.findElement(By.linkText("Digitalización y regulación financiera"));
        element.click();
    }

    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/Users/hernan/Documents/eurobits/webprofiler/chromedriver");

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        // logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        driver = new ChromeDriver(caps);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        // new Augmenter().augment(new RemoteWebDriver(new URL(WEBDRIVER_SERVER_URL), caps));
        Capabilities actualCaps = ((HasCapabilities) driver).getCapabilities();
        System.out.println("Actual caps: " + actualCaps);
    }

    public void showLogs() throws Exception {
        Logs logs = driver.manage().logs();
        System.out.println("Log types: " + logs.getAvailableLogTypes());
        printLog(LogType.PERFORMANCE);
    }

    public void tearDown() {
        driver.quit();
    }

    void printLog(String type) {
        for (LogEntry logEntry : driver.manage().logs().get(LogType.PERFORMANCE).getAll())
        {
            System.out.println(logEntry);
        }

        // List<LogEntry> entries = driver.manage().logs().get(type).getAll();
        LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
        Iterator<LogEntry> iterentries = entries.iterator();
        while (iterentries.hasNext()) {
            LogEntry entry = iterentries.next();
            System.out.println(entry.getMessage());
        }
        // System.out.println(entries.size() + " " + type + " log entries found");
        // for (LogEntry entry : entries) {
        //     System.out.println(
        //             new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        // }
    }

}