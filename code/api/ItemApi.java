package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ItemApi {
    private static final String BASE_URI = "https://api.inv.bg";
    private static final String BASE_PATH = "v3";
    private static final String RESOURCE_PATH = "/items";
    private String token;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Create new gson instance

    public ItemApi (String token){
        this.token = token;
    }


    public Response createItem(ItemDTO itemDTO){
        return  RestAssured.given()
                .log().all() //Log everything (url, headers, body)
                .contentType(ContentType.JSON) //Sets content-type: application/json
                .accept(ContentType.JSON) //Sets accept: application/json
                .auth().oauth2(token) //Sets bearer token in the request header
                .headers("User-Agent", "Mozilla Firefox")
                .baseUri(BASE_URI) //Sets base uri
                .basePath(BASE_PATH) // Set base path
                .when()
                .body(gson.toJson(itemDTO))
                .post(RESOURCE_PATH).prettyPeek();
    }

    public Response deleteItem(int id){
        return RestAssured.given()
                .log().all() //Log everything (url, headers, body)
                .contentType(ContentType.JSON) //Sets content-type: application/json
                .accept(ContentType.JSON) //Sets accept: application/json
                .auth().oauth2(token) //Sets bearer token in the request header
                .headers("User-Agent", "Mozilla Firefox")
                .baseUri(BASE_URI) //Sets base uri
                .basePath(BASE_PATH) // Set base path
                .when()
                .delete(RESOURCE_PATH + "/" + id).prettyPeek();

    }

    public Response getItem(int id){
        return RestAssured.given()
                .log().all() //Log everything (url, headers, body)
                .contentType(ContentType.JSON) //Sets content-type: application/json
                .accept(ContentType.JSON) //Sets accept: application/json
                .auth().oauth2(token) //Sets bearer token in the request header
                .headers("User-Agent", "Mozilla Firefox")
                .baseUri(BASE_URI) //Sets base uri
                .basePath(BASE_PATH) // Set base path
                .when()
                .get(RESOURCE_PATH + "/" + id).prettyPeek();

    }

    public Response updateItem(int id, Tags tags){
        return RestAssured.given()
                .log().all() //Log everything (url, headers, body)
                .contentType(ContentType.JSON) //Sets content-type: application/json
                .accept(ContentType.JSON) //Sets accept: application/json
                .auth().oauth2(token) //Sets bearer token in the request header
                .headers("User-Agent", "Mozilla Firefox")
                .baseUri(BASE_URI) //Sets base uri
                .basePath(BASE_PATH) // Set base path
                .when()
                .body(gson.toJson(tags))
                .patch(RESOURCE_PATH + "/" + id).prettyPeek();

    }

    public static void main(String[] args) {
        ItemApi itemApi = new ItemApi("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmaXJtSUQiOjI4OTAwLCJ1c2VySUQiOjQsImlhdCI6MTc1NDU5MjU3NSwiZXhwIjoxNzU0Njc4OTc1LCJzY29wZSI6WyJodW1hbiIsImFkbWluIl19.25eAWOY_jLQqhv_3ISNO-n_cpkSr2u_gp7m6OFYAq8I"); //Create instance ItemApi
        ItemDTO coffee = new ItemDTO("Lavazza Coffe", 20.50f, 10); //Create DTO (coffee)
        itemApi.createItem(coffee); //Send POST request to create the item on the rest server
    }
}
