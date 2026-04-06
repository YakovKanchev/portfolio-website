package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserTool {

    public WebDriver setupBrowser() {
        return new ChromeDriver();
    }

    public void quitBrowser(WebDriver driver) {
        driver.quit();
    }
}
