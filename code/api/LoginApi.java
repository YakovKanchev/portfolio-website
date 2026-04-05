package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginApi {
    private static final String BASE_URI = "https://api.inv.bg";
    private static final String BASE_PATH = "v3";
    private static final String RESOURCE_PATH = "/login/token";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Create new gson instance


    public Response login(String email, String password, String domain){
        LoginDTO loginDTO = new LoginDTO(email, password, domain);
        return  RestAssured.given()
                .log().all() //Log everything (url, headers, body)
                .contentType(ContentType.JSON) //Sets content-type: application/json
                .accept(ContentType.JSON) //Sets accept: application/json
                .headers("User-Agent", "Mozilla Firefox")
                .baseUri(BASE_URI) //Sets base uri
                .basePath(BASE_PATH) // Set base path
                .when()
                .body(gson.toJson(loginDTO))
                .post(RESOURCE_PATH).prettyPeek();
    }




    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Create new gson instance
        LoginDTO alexCredentials = new LoginDTO("yakovkanchev@gmail.com", "qwerty123", "yk97"); // Create instance of login dto
        String alexJson = gson.toJson(alexCredentials); //Serialize dto to json
        System.out.println(alexJson); // print the result json
        LoginApi loginApi = new LoginApi();
        Response response = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97");



    }
}
