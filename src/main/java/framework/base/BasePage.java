package framework.base;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks only after the element becomes clickable so we avoid timing issues that often happen
     * when calling element.click() directly on a still-rendering or blocked element.
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Waits for the field to become visible, clears old content, then types the new value so stale
     * or prefilled text does not get mixed with the intended input.
     */
    protected void waitAndType(WebElement element, String text) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    /**
     * Waits until the element is visible and trims the returned text so surrounding spaces or line
     * breaks do not create false-negative assertions.
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Returns false instead of failing when the element is missing or becomes stale, which commonly
     * happens after the DOM re-renders and the old element reference is no longer valid.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException exception) {
            return false;
        }
    }

    /**
     * Scrolls the page until the target element enters the viewport, which is useful when the
     * element exists in the DOM but is currently outside the visible area.
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Waits until the browser reports document.readyState as complete, which is especially useful
     * right after a page navigation before interacting with newly loaded content.
     */
    protected void waitForPageLoad() {
        wait.until(webDriver -> "complete".equals(
            ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
        ));
    }

    /**
     * Waits for the element to be visible and returns the requested attribute, for example reading
     * the current value attribute from an input field after typing.
     */
    protected String getAttribute(WebElement element, String attr) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attr);
    }
}
