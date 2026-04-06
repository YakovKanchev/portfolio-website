package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import utils.BrowserTool;

public abstract class BaseTest {

    protected WebDriver driver;
    protected WebApp webApp;
    private BrowserTool browserTool;



    @BeforeMethod
    public void setup() {
        browserTool = new BrowserTool();
        driver = browserTool.setupBrowser();
        driver.manage().window().maximize();
        webApp = new WebApp(driver);
    }

   @AfterMethod
   public void tearDown() {
       if (driver != null) {
           browserTool.quitBrowser(driver);
       }
       driver = null;
   }
}
