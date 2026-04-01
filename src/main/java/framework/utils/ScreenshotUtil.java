package framework.utils;

import framework.config.ConfigReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String testName) {
        Path screenshotDirectory = getScreenshotDirectory();
        String fileName = testName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMATTER) + ".png";
        Path destination = screenshotDirectory.resolve(fileName);

        try {
            Files.createDirectories(screenshotDirectory);
            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save screenshot for test: " + testName, exception);
        }

        return destination.toString();
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private static Path getScreenshotDirectory() {
        return Path.of(ConfigReader.getInstance().getScreenshotPath());
    }
}
