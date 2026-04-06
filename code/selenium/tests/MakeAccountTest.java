import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;



public class MakeAccountTest extends BaseTest {

    @Test
    public void loginPageObjectTest() {
        webApp.mainPage().goMainPage();
        webApp.mainPage().clickMyAccountDropdown();
        webApp.mainPage().clickRegisterButton();
        webApp.registerPage().typeTextInFirstNameInputField("Yakov");
        webApp.registerPage().typeTextInLastNameInputField("Kanchev");
        webApp.registerPage().typeTextInEmailInputField();
        webApp.registerPage().typeTextInPasswordInputField("random123");
        webApp.registerPage().clickPrivacyPolicySlider();
        webApp.registerPage().clickContinueButton();
        Assert.assertTrue(webApp.createdAccountSuccessPage().confirmCreatedAccountText().contains("Has Been Created!"));
    }
}
