package base;

import org.openqa.selenium.WebDriver;
import pages.CreatedAccountSuccessPage;
import pages.MainPage;
import pages.RegisterPage;

public class WebApp {

    WebDriver driver;

    private CreatedAccountSuccessPage createdAccountSuccessPage;

    private MainPage mainPage;

    private RegisterPage registerPage;

    public WebApp(WebDriver driver) {
        this.driver = driver;
    }

    public MainPage mainPage() {
        if(mainPage == null) {
            mainPage= new MainPage(driver);
        }
        return mainPage;
    }

    public RegisterPage registerPage() {
        if(registerPage == null) {
            registerPage = new RegisterPage(driver);
        }
        return registerPage;
    }

    public CreatedAccountSuccessPage createdAccountSuccessPage() {
        if(createdAccountSuccessPage == null) {
            createdAccountSuccessPage = new CreatedAccountSuccessPage(driver);
        }
        return createdAccountSuccessPage;
    }


}
