package pages;

import base.BasePage;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage extends BasePage {

    private String prefix = RandomStringUtils.randomAlphabetic(7);
    private String suffix = RandomStringUtils.randomAlphabetic(5);
    private String emailAdressGenerator = prefix + "@" + suffix + ".bg";

    @FindBy(id = "input-firstname")
    private WebElement firstName;

    @FindBy(id = "input-lastname")
    private WebElement lastName;

    @FindBy(id = "input-email")
    private WebElement emailAddress;

    @FindBy(id = "input-password")
    private WebElement password;

    @FindBy(css = "input[name='agree']")
    private WebElement privacyPolicySlider;

    @FindBy(xpath = "//button[@class='btn btn-primary']")
    private WebElement continueButton;


    public RegisterPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public void typeTextInFirstNameInputField(String firstName) {
        webTool.typeText(this.firstName, firstName);
    }

    public void typeTextInLastNameInputField(String lastName) {
        webTool.typeText(this.lastName, lastName);
    }

    public void typeTextInEmailInputField() {
        webTool.typeText(emailAddress, emailAdressGenerator);
    }

    public void typeTextInPasswordInputField(String password) {
        webTool.typeText(this.password, password);
    }

    public void clickPrivacyPolicySlider(){
        webTool.clickWebElement(privacyPolicySlider);
    }

    public void clickContinueButton(){
        if(continueButton.isEnabled() || !continueButton.isSelected()) {
            webTool.clickWebElement(continueButton);
        }
    }

}
