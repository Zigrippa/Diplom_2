import config.Config;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.UserClient;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserCreationTest {

        private UserClient userClient;
        private User user;
        String accessToken;

        @Before
        public void setUp() {
            user = new User().generateUser();
            userClient = new UserClient();
            RestAssured.baseURI = Config.BASE_URL;

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
        @DisplayName("Проверка создания пользователя")
        public void createUniqueUserTest() {
            ValidatableResponse response = userClient.create(user);

            assertEquals("Статус код неверный при создании пользователя",
                    HttpStatus.SC_OK, response.extract().statusCode());

        }

        @Test
        @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
        public void createAlreadyExistUserTest() {
            userClient.create(user);

            ValidatableResponse response = userClient.create(user);

            assertEquals("Статус код неверный при создании уже существующего пользователя",
                    HttpStatus.SC_FORBIDDEN, response.extract().statusCode());

        }

        @Test
        @DisplayName("Проверка создания пользователя без заполнения почтового адреса")
        public void createUserWithoutEmailTest() {
            user.setEmail(null);
            userClient.create(user);

            ValidatableResponse response = userClient.create(user);

            assertEquals("Статус код неверный при создании пользователя без заполнения почтового адреса",
                    HttpStatus.SC_FORBIDDEN, response.extract().statusCode());

        }

        @Test
        @DisplayName("Проверка создания пользователя без заполнения пароля")
        public void createUserWithoutPasswordTest() {
            user.setPassword(null);
            userClient.create(user);

            ValidatableResponse response = userClient.create(user);

            assertEquals("Статус код неверный при создании пользователя без заполнения пароля",
                    HttpStatus.SC_FORBIDDEN, response.extract().statusCode());

        }

        @Test
        @DisplayName("Проверка создания пользователя без заполнения имени")
        public void createUserWithoutNameTest() {
            user.setName(null);
            userClient.create(user);

            ValidatableResponse response = userClient.create(user);

            assertEquals("Статус код неверный при создании пользователя без заполнения имени",
                    HttpStatus.SC_FORBIDDEN, response.extract().statusCode());

        }


}
