package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends BasePage {


    @FindBy(xpath = "//i[@class='fa-solid fa-user']/..")
    private WebElement myAccountDropdown;

    @FindBy(xpath = "//a[text()='Register']")
    private WebElement registerButton;


    private static final By REGISTER_BUTTON_BY = By.xpath("//a[text()='Register']");

    private static final String LOGIN_PAGE_URL = "https://auto.pragmatic.bg";

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public void goMainPage() {
        webTool.goToPage(LOGIN_PAGE_URL);
    }

    public void clickMyAccountDropdown() {
        webTool.clickWebElement(myAccountDropdown);
    }

    public void clickRegisterButton() {
        waitTool.waitForElementVisibility(driver, REGISTER_BUTTON_BY, 3);
        webTool.clickWebElement(registerButton);
    }
}




