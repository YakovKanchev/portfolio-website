package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebTool {

    private WebDriver driver;

    public WebTool(WebDriver driver) {
        this.driver = driver;
    }

    public void goToPage(String url) {
        driver.get(url);
    }

    public void clickWebElement(WebElement webElement) {
        webElement.click();
    }

    public void typeText(WebElement webElement, String text) {
        webElement.sendKeys(text);
    }
}
