package api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


public class LoginApiTest {
    private final LoginApi loginApi = new LoginApi(); //Create instance from Login API wrapper (http client)

    @Test
    @DisplayName("Can login with valid credentials / Obtain token")
    @Tag("api")
    public void canLoginWithValidCredentials(){
        Response response = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97"); //Send POST request for obtain token
        Assertions.assertEquals(200, response.statusCode()); //Check status code
        String token = response.jsonPath().getString("token"); //Extract the value of token field
        Assertions.assertFalse(token.isEmpty(), "The token is blank"); //Check that token is not blank/empty
    }

    @Test
    @DisplayName("Can't login with invalid credentials")
    @Tag("api")
    public void cantLoginWithInvalidCredentials(){
        Response response = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97"); //Send POST request with invalid password
        Assertions.assertEquals(401, response.statusCode()); //Check status code is Unauthorized (401)
        String errorMessage = response.jsonPath().getString("error"); //Extract error field value
        Assertions.assertEquals("Wrong username or password", errorMessage); //Check error text
    }

    @Test
    @DisplayName("Can't login with invalid company")
    @Tag("api")
    public void cantLoginWithInvalidCompany(){
        Response response = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "shadow-fleet"); //Send POST request with invalid password
        Assertions.assertEquals(401, response.statusCode()); //Check status code is Unauthorized (401)
        String errorMessage = response.jsonPath().getString("error"); // Extract error field value
        Assertions.assertEquals("Firm not found", errorMessage); //Check error message
    }

    @Test
    @DisplayName("Can't login with blank credentials")
    @Tag("api")
    public void cantLoginWithBlankCredentials(){
        Response response = loginApi.login(null, null, "yk97"); //Send POST request with invalid password
        Assertions.assertEquals(400, response.statusCode()); //Check status code is Unauthorized (401)
        String errorMessage = response.jsonPath().getString("error"); //Extract error field value
        Assertions.assertEquals("POST argument `email` is missing", errorMessage); //Check error text
    }
}
