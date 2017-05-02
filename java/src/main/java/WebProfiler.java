
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

    private WebDriver driver;

    public void testElPais() {
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
        System.setProperty("webdriver.chrome.driver", "/Users/hernan/Documents/Proyectos/webprofiler/chromedriver");

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        driver = new ChromeDriver(caps);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void cleanUp() {
        driver.quit();
    }

    public void showLogs() throws Exception {
        LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry entry : entries) {
            System.out.println(entry.getMessage());
        }
    }

}