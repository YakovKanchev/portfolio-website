package api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ItemApiTest {
    private ItemApi itemApi;
    private final LoginApi loginApi = new LoginApi();


    @Test
    @Tag("ui")
    @DisplayName("Can create item - api")
    public void canCreateItem(){
        Response loginResponse = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97"); // obtain token
        String token = loginResponse.jsonPath().getString("token"); // extract token value
        itemApi = new ItemApi(token); //Create item api instance with valid token
        ItemDTO football = new ItemDTO("NewArmor", 25.30f,10); //Create item dto
        Response createNewItemResponse = itemApi.createItem(football);
        int idOfCreatedItem = createNewItemResponse.jsonPath().getInt("id");//Send POST request
        Response getCreatedItemResponseBodyResponse = itemApi.getItem(idOfCreatedItem);
        String nameOfCreatedItem = getCreatedItemResponseBodyResponse.jsonPath().getString("name");
        Assertions.assertEquals(201, createNewItemResponse.statusCode());//Check status code
        Assertions.assertEquals("NewArmor", nameOfCreatedItem);
    }

    @Test
    @Tag("api")
    @DisplayName("Can delete item")
    public void canDeleteItem(){
        int id = 2; // Not a great solution

        Response loginResponse = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97"); // obtain token
        String token = loginResponse.jsonPath().getString("token"); // extract token value
        itemApi = new ItemApi(token); //Create item api instance with valid token
        Response deleteItemResponse = itemApi.deleteItem(id);
        Assertions.assertEquals(204, deleteItemResponse.statusCode());
        Response item = itemApi.getItem(id);
        Assertions.assertEquals(404, item.statusCode());
        String errorMessage = item.jsonPath().getString("error");
        Assertions.assertEquals("Item Not Found", errorMessage);
    }

    @Test
    @Tag("api")
    @DisplayName("Can update item")
    public void updateItem(){
        Response loginResponse = loginApi.login("yakovkanchev@gmail.com", "qwerty123", "yk97"); // obtain token
        String token = loginResponse.jsonPath().getString("token"); // extract token value
        itemApi = new ItemApi(token);
        ArrayList<String> newTags = new ArrayList<>();
        newTags.add("newThing");
        Response newTag = itemApi.updateItem(6, new Tags(newTags));

    }
}
