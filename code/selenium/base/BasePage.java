package base;

import org.openqa.selenium.WebDriver;
import utils.WaitTool;
import utils.WebTool;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebTool webTool;
    protected WaitTool waitTool;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        webTool = new WebTool(this.driver);
        waitTool = new WaitTool();
    }
}
