
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;


class WebProfiler {

    private WebDriver driver;
    private WebElement lastelem = null;

    void testElPais() {
        driver.get("http://www.elpais.com");
        WebElement element = driver.findElement(By.id("boton_buscador"));
        element.click();
        element = driver.findElement(By.name("qt"));
        element.sendKeys("psd2");
        element.sendKeys(Keys.RETURN);
        element = driver.findElement(By.linkText("Digitalización y regulación financiera"));
        element.click();
    }

    void setUp(String chromedriverpath) throws Exception {
        System.setProperty("webdriver.chrome.driver", chromedriverpath);

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        caps.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(caps);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    void cleanUp() {
        driver.quit();
    }

    void showLogs() throws Exception {
        processLogs()
                .forEach(System.out::println);
    }

    void RunCommand(String commandline) {
        commandline = commandline.trim();
        if (commandline.charAt(0) == '#') {
            return;
        }
        System.err.println("Run: "+ commandline);
        Pattern pattern = Pattern.compile("([^ \t]*)[ \t]*(.*)");
        Matcher matcher = pattern.matcher(commandline);
        if (matcher.matches()) {
            String comm = matcher.group(1);
            String args = matcher.group(2);
            switch (comm.toLowerCase()) {
                case "go":
                    driver.get(args);
                    break;
                case "findid":
                    lastelem = driver.findElement(By.id(args));
                    break;
                case "findname":
                    lastelem = driver.findElement(By.name(args));
                    break;
                case "findtext":
                    lastelem = driver.findElement(By.linkText(args));
                    break;
                case "click":
                    lastelem.click();
                    break;
                case "type":
                    lastelem.sendKeys(expandedString(args));
                    break;
            }
        }
    }

    private String expandedString(String input) {
        StringBuilder builder = new StringBuilder();
        boolean escaped = false;

        for (char c : input.toCharArray()) {
            if (escaped) {
                if (c == '\\') {
                    builder.append(c);
                } else if (c == 'n') {
                    builder.append('\n');
                }
                escaped = false;
            } else {
                if (c == '\\')
                    escaped = true;
                else
                    builder.append(c);
            }
        }
        return builder.toString();
    }

    private Stream<String> processLogs() throws Exception {
        HashMap<String, LinkedTreeMap<String, Object>> alllogs = new HashMap<>();
        List<String> allkeys = new LinkedList<>();

        Gson gson = new Gson();
        LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry entry : entries) {
            String msg = entry.getMessage();
            // Map<String, String> full = gson.fromJson(msg, type);
            LinkedTreeMap full = gson.fromJson(msg, LinkedTreeMap.class);
            LinkedTreeMap longentry = (LinkedTreeMap)full.get("message");
            String method = (String)longentry.get("method");
            LinkedTreeMap pars = (LinkedTreeMap)longentry.get("params");
            if (method.equals("Network.responseReceived")) {
                String key = (String)pars.get("requestId");
                LinkedTreeMap resp = (LinkedTreeMap)pars.get("response");
                if (resp.containsKey("requestHeadersText")) {
                    String rqmethod = (String)resp.get("requestHeadersText");
                    int idx = rqmethod.indexOf("\r\n", 0);
                    rqmethod = rqmethod.substring(0, idx);
                    LinkedTreeMap<String, Object> log = new LinkedTreeMap<>();
                    log.put("rqmethod", rqmethod);
                    log.put("url", resp.get("url"));
                    log.put("requestId", key);
                    log.put("starts", ((LinkedTreeMap)resp.get("timing")).get("requestTime"));
                    allkeys.add(key);
                    alllogs.put(key, log);
                }
            } else if (method.equals("Network.loadingFinished")) {
                String key = (String)pars.get("requestId");
                if (alllogs.containsKey(key)) {
                    LinkedTreeMap<String, Object> log = alllogs.get(key);
                    double ends = ((Number)pars.get("timestamp")).doubleValue();
                    double starts = ((Number)log.get("starts")).doubleValue();
                    log.put("ends", ends);
                    log.put("time", (ends - starts) * 1000);
                }
            }
        }

        return allkeys
                .stream()
                .map(k -> gson.toJson(alllogs.get(k)));
    }
}