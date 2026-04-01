package framework.base;

import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        String requestedEnv = getSystemPropertyOrDefault("env", env);
        System.setProperty("env", requestedEnv);

        ConfigReader configReader = ConfigReader.getInstance();
        String requestedBrowser = getSystemPropertyOrDefault("browser", browser);
        if (requestedBrowser == null || requestedBrowser.isBlank()) {
            requestedBrowser = configReader.getBrowser();
        }

        WebDriver driver = DriverFactory.createDriver(requestedBrowser);
        driver.manage().window().maximize();

        String gridUrl = System.getProperty("grid.url", "");
        if (gridUrl == null || gridUrl.isBlank()) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        }

        driver.get(configReader.getBaseUrl());
        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                attachScreenshotOnFailure(getDriver());
            }
        } finally {
            if (getDriver() != null) {
                getDriver().quit();
            }
            tlDriver.remove();
        }
    }

    /**
     * Attaches a PNG screenshot to Allure when the test fails.
     */
    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotOnFailure(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Screenshot on Failure", "image/png", new ByteArrayInputStream(screenshot), ".png");
        return screenshot;
    }

    private String getSystemPropertyOrDefault(String key, String fallbackValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            return fallbackValue;
        }
        return value;
    }
}