package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl != null && !gridUrl.isBlank()) {
            System.out.println("[Driver] REMOTE on Grid: " + browser + " -> " + gridUrl);
            return createRemoteDriver(browser, gridUrl);
        }
        return createLocalDriver(browser);
    }

    public static WebDriver createLocalDriver(String browser) {
        String normalizedBrowser = browser == null ? "chrome" : browser.trim().toLowerCase(Locale.ROOT);
        boolean isCI = System.getenv("CI") != null;

        switch (normalizedBrowser) {
            case "firefox":
                return createFirefoxDriver(isCI);
            case "chrome":
            default:
                return createChromeDriver(isCI);
        }
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        AbstractDriverOptions<?> options;
        if (browser != null && browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            options = firefoxOptions;
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
            options = chromeOptions;
        }

        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException exception) {
            throw new RuntimeException("[Driver] Grid URL khong hop le: " + gridUrl, exception);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[Driver] Chrome HEADLESS");
        } else {
            System.out.println("[Driver] Chrome LOCAL");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
            System.out.println("[Driver] Firefox HEADLESS");
        } else {
            System.out.println("[Driver] Firefox LOCAL");
        }

        return new FirefoxDriver(options);
    }
}