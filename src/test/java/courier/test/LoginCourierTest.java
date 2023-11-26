package courier.test;

import client.CourierClient;
import data.CourierCredentials;
import data.CourierData;
import data.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import org.junit.After;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginCourierTest {

    CourierClient courierClient;
    CourierData courier;
    private int courierId;

    @Before
    public void setup() {
        //создаем тестовые данные
        courierClient = new CourierClient();
        //вызваем метод
        courier = new CourierData("login2111", "password", "login78");
        ValidatableResponse response = courierClient.createCourier(courier);
        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));


    }
    @After
    public void cleanUp() {

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(),courier.getPassword());
        int sc = courierClient.loginCourier(courierCredentials).extract().statusCode();
        if (sc != 404 && sc !=  400) {
            courierId = courierClient.loginCourier(courierCredentials).extract().path("id");
            courierClient.deleteCourier(courierId);
        }

    }

    @Test
    @DisplayName("Авторизация курьера в системе")
    @Description("Создание курьера для теста")
    public void checkLoginCourier() {

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(),courier.getPassword());
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);

        response.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());


    }

    @Test
    @DisplayName("Попытка авторизации курьера без логина")
    @Description("Авторизация без логина")
    public void checkLoginWithoutLogin(){

        CourierCredentials courierCredentials = new CourierCredentials("",courier.getPassword());
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);

        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));



    }

    @Test
    @DisplayName("Попытка авторизации курьера без пароля")
    @Description("Авторизация без пароля")
    public void checkLoginWithoutPassword(){

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(),"");
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);
        response.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));


    }

    @Test
    @DisplayName("Попытка авторизации несуществующего курьера")
    @Description("Авторизация несуществуюещго курьера")
    public void checkLoginNonExisting(){

        CourierCredentials courierCredentials = new CourierCredentials("Fdrrt134543vdfgsddfg",courier.getPassword());
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);
        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));


    }

    @Test
    @DisplayName("Попытка авторизации курьера с некорреектным паролем")
    @Description("Авторизация с некоррктным паролем курьера")
    public void loginWithWrongPasswordTest(){

        CourierCredentials courierCredentials = new CourierCredentials(courier.getPassword(),"4534tfgfgd");
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);
        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Попытка авторизации курьера с некорреектным логином и паролем")
    @Description("Авторизация с некоррктным логином паролем курьера")
    public void loginWithWrongLoginAndPasswordTest(){

        CourierCredentials courierCredentials = new CourierCredentials("Gfgvfrrtyrthf","4534tfgfgd");
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);
        response.assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }
}
