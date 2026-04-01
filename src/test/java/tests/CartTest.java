package tests;

import framework.base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Gio hang")
public class CartTest extends BaseTest {

    @Test
    @Story("UC-010: Tai trang chinh")
    @Description("Trang SauceDemo phai tai thanh cong")
    @Severity(SeverityLevel.CRITICAL)
    public void testCart_PageLoad() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Xac nhan URL dung domain", () ->
            Assert.assertTrue(getDriver().getCurrentUrl().contains("saucedemo"))
        );
    }

    @Test
    @Story("UC-011: Title khong null")
    @Description("Tieu de trang khong duoc null")
    @Severity(SeverityLevel.NORMAL)
    public void testCart_TitleNotNull() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Kiem tra title khong null", () ->
            Assert.assertNotNull(getDriver().getTitle())
        );
    }

    @Test
    @Story("UC-012: Page source co noi dung")
    @Description("Page source khong duoc rong")
    @Severity(SeverityLevel.NORMAL)
    public void testCart_SourceNotEmpty() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Xac nhan page source khong rong", () ->
            Assert.assertFalse(getDriver().getPageSource().isEmpty())
        );
    }

    @Test
    @Story("UC-013: HTTPS")
    @Description("URL phai bat dau bang https")
    @Severity(SeverityLevel.CRITICAL)
    public void testCart_HttpsUrl() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Xac nhan URL dung HTTPS", () ->
            Assert.assertTrue(getDriver().getCurrentUrl().startsWith("https"))
        );
    }
}