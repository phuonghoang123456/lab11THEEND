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

@Feature("Xac thuc nguoi dung")
public class LoginTest extends BaseTest {

    @Test
    @Story("UC-001: Dang nhap hop le")
    @Description("Kiem tra URL sau khi mo trang SauceDemo")
    @Severity(SeverityLevel.CRITICAL)
    public void testLogin_ValidUser() {
        Allure.step("Mo trang dang nhap SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Kiem tra URL chua 'saucedemo'", () ->
            Assert.assertTrue(getDriver().getCurrentUrl().contains("saucedemo"), "URL khong dung")
        );
    }

    @Test
    @Story("UC-002: Kiem tra tieu de trang")
    @Description("Tieu de trang khong duoc rong")
    @Severity(SeverityLevel.NORMAL)
    public void testLogin_PageTitle() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Kiem tra title khong rong", () ->
            Assert.assertFalse(getDriver().getTitle().isEmpty(), "Title rong")
        );
    }

    @Test
    @Story("UC-003: URL khong null")
    @Description("getCurrentUrl() phai tra ve gia tri khac null")
    @Severity(SeverityLevel.MINOR)
    public void testLogin_UrlNotEmpty() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Xac nhan URL khong null", () ->
            Assert.assertNotNull(getDriver().getCurrentUrl())
        );
    }

    @Test
    @Story("UC-004: Trang co body HTML")
    @Description("Phan tu body phai ton tai tren trang")
    @Severity(SeverityLevel.MINOR)
    public void testLogin_BodyPresent() {
        Allure.step("Mo trang SauceDemo", () ->
            getDriver().get("https://www.saucedemo.com")
        );
        Allure.step("Tim the body", () ->
            Assert.assertNotNull(getDriver().findElement(org.openqa.selenium.By.tagName("body")))
        );
    }
}