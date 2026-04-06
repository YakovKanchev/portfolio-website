package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreatedAccountSuccessPage extends BasePage {

    @FindBy(css = "div#content > h1")
    private WebElement textFieldForApproval;

    private static final By SUCCESS_HEADER_TEXT = By.cssSelector("div#content > h1");

    public CreatedAccountSuccessPage (WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public String confirmCreatedAccountText(){

        String expectedText = "Your Account Has Been Created!";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.textToBe(SUCCESS_HEADER_TEXT, expectedText)); // тук си помогнах бая онлайн, не ми се получаваха нещата

        return textFieldForApproval.getText();
    }

}
