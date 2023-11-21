package courier_test;

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
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateCourierTest {
    CourierClient courierClient;
    CourierData courier;
    private int courierId;

    @Before
    public void setup() {
        //создаем тестовые данные
        courierClient = new CourierClient();

    }




    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка создания нвого курьера")
    public void courierCanBeCreatedTest() {
        //вызваем метод

        courier= CourierGenerator.getRandomCourier();
        ValidatableResponse response = courierClient.createCourier(courier);

        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));


    }


    @Test
    @DisplayName("Создание уже существующего курьера(негатив)")
    @Description("Проверка ограничения на создание дубликата курьера")
    public void checkCreateDuplicateCourier(){

        courier= CourierGenerator.getRandomCourier();

        ValidatableResponse response = courierClient.createCourier(courier);
        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
        ValidatableResponse responseDouble = courierClient.createCourier(courier);

        // проверка повторного соpадния такого же курьера
        responseDouble.assertThat()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

    }

    @Test
    @DisplayName("Проверка создания курьера без пароля и имени")
    @Description("Проверка ограничения на создание курьера без пароля и имени")
    public void createCourierWithoutPasswordAndFirstName(){

        courier = new CourierData("login2111", "", "");
        ValidatableResponse response = courierClient.createCourier(courier);
        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Проверка создания курьера без пароля")
    @Description("Проверка ограничения на создание курьера без пароля")
    public void createCourierWithoutPassword(){

        courier = new CourierData("login2111", "", "Petya");
        ValidatableResponse response = courierClient.createCourier(courier);
        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Проверка создания курьера без логина")
    @Description("Проверка ограничения на создание курьера без логина")
    public void createCourierWithoutLogin(){

        courier = new CourierData("", "123", "Petya");
        ValidatableResponse response = courierClient.createCourier(courier);
        // проверка
        response.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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
}
