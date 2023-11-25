package client;

import data.CourierCredentials;
import data.CourierData;
import data.DeleteCourier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;


public class CourierClient extends RestClient {
    private CourierData courier;
    private static final String COURIER_PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "api/v1/courier/login";
    private static final String DELETE_PATH = "api/v1/courier/";


    @Step("Создание курьера")
    public ValidatableResponse createCourier(CourierData courier) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();

    }

    @Step("Логин курьера в систему")
    public ValidatableResponse loginCourier(CourierCredentials courierCredentials) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)

                .then();

    }


    //delete
    @Step("Удаление созданного куоьера")
    public ValidatableResponse deleteCourier(int Id) {
        //удаление
        DeleteCourier deleteCourier = new DeleteCourier(String.valueOf(Id));

            return given()
                    .spec(requestSpecification())
                    .and()
                    .body(deleteCourier)
                    .when()
                    .delete(DELETE_PATH + Id)
                    .then();
        }

    }





