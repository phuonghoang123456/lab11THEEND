package tests;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleParallelTest extends BaseTest {
    @Test
    public void testLogin_Success() {
        Assert.assertTrue(getDriver().getCurrentUrl().contains("saucedemo"), "URL should contain saucedemo");
    }

    @Test
    public void testLogin_Fail_TakeScreenshot() {
        boolean intentionalFail = Boolean.parseBoolean(System.getProperty("intentional.fail", "false"));
        Assert.assertTrue(!intentionalFail, "Intentional fail");
    }

    @Test
    public void testTitle_Check() {
        Assert.assertFalse(getDriver().getTitle().isBlank(), "Title should not be empty");
    }
}
