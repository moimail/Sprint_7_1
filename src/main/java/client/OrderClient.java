package client;

import data.OrderData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{

    private static final String ORDER_PATH = "/api/v1/orders";
    @Step("Создание заказа")
    public ValidatableResponse createOrderTest(OrderData order){
        return   given()
                .spec(requestSpecification())
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }
}
