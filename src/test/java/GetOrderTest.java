import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import models.OrderClient;
import models.User;
import models.UserClient;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetOrderTest {

        private OrderClient orderClient;
        private UserClient userClient;
        private User user;
        String accessToken;

        @Before
        public void setUp() {
            user = new User().generateUser();
            userClient = new UserClient();
            RestAssured.baseURI = Config.BASE_URL;
            userClient.create(user);

            orderClient = new OrderClient();

        }

        @After
        public void tearDown() {
            if (accessToken != null) userClient.delete(accessTokenExtraction(user));
        }

        //Данный метод логинит пользователя и возвращает accessToken
        public String accessTokenExtraction(User user) {
            ValidatableResponse response = userClient.login(user);
            return response.extract().path("accessToken");
        }


        @Test
        @DisplayName("Проверка получения заказа с авторизацией")
        public void getOrderWithLoginTest() {
            ValidatableResponse response = orderClient.getUserOrder(user, accessTokenExtraction(user));

            assertEquals("Статус код неверный при получении заказа с авторизацией",
                    HttpStatus.SC_OK, response.extract().statusCode());

            assertTrue("Невалидные данные в теле:", response.extract().path("success"));

        }

    @Test
    @DisplayName("Проверка получения заказа без авторизации")
    public void getOrderWithoutLoginTest() {
        ValidatableResponse response = orderClient.getUserOrder(user, "");

        assertEquals("Статус код неверный при получении заказа без авторизации",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());

        assertFalse("Невалидные данные в теле:", response.extract().path("success"));

    }

}
