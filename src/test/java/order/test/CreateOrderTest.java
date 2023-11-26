package order.test;
import client.OrderClient;
import data.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    OrderClient orderClient;
    private OrderData order;

    public CreateOrderTest(OrderData order){
        this.order = order;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {new OrderData("Иван","Петров","Центральный","Кутузовская","+79002223344",4,"2023-11-12","Позвонить за час",new String[]{"BLACK"})},
                {new OrderData("Дмитрий","Иванов","Мира ул","Проспект Мира","8123222333444",3,"2023-10-30","Позвонить за час",new String[]{"GREY"})},
                {new OrderData("Daniel","Craig","Лубянка 100","Лубянка","+79316753434",4,"2023-09-01","Позвонить за час",new String[]{"BLACK","GREY"})},
                {new OrderData("Jon","Ritch","Новый арбат","Театральная","89225557733",5,"2023-08-10","Позвонить за час",new String[]{})}
        };
    }

    @Test
    @DisplayName("Тест создания заказов")
    @Description("Успешное создание заказов с разными параметрами")
    public void createOrderTest(){

        ValidatableResponse response = orderClient.createOrderTest(order);
         response

                .assertThat()
                 .statusCode(201)
                 .and()
                 .body("track", Matchers.notNullValue());
    }

}
