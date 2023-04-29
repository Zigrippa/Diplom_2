import com.github.javafaker.Faker;
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

public class UserEditingTest {

    static Faker faker = new Faker();

    private UserClient userClient;
    private User user;
    String accessToken;
    String accessTokenAfterRegistration;
    String refreshTokenAfterRegistration;

    @Before
    public void setUp() {
        user = new User().generateUser();
        userClient = new UserClient();
        RestAssured.baseURI = Config.BASE_URL;
        //userClient.create(user);
        //accessTokenAfterRegistration = accessTokenExtractionAfterRegistration(user);
        refreshTokenAfterRegistration = refreshTokenExtraction(user);
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

    //Данный метод создает пользователя и возвращает accessToken
    /*public String accessTokenExtractionAfterRegistration(User user) {
        ValidatableResponse response = userClient.create(user);
        return response.extract().path("accessToken");
    }*/

    //Данный метод создает пользователя и возвращает accessToken
    public String refreshTokenExtraction(User user) {
        ValidatableResponse response = userClient.create(user);
        return response.extract().path("refreshToken");
    }

    @Test
    @DisplayName("Проверка изменение почтового адреса авторизованного пользователя")
    public void editEmailUserTest() {
        ValidatableResponse response = userClient.edit(accessTokenExtraction(user),
                new User(faker.internet().emailAddress(), user.getPassword(), user.getName()));

        assertEquals("Статус код неверный при изменение почтового адреса авторизованного пользователя",
                HttpStatus.SC_OK, response.extract().statusCode());

    }

    @Test
    @DisplayName("Проверка изменение имени авторизованного пользователя")
    public void editNameUserTest() {
        ValidatableResponse response = userClient.edit(accessTokenExtraction(user),
                new User(user.getEmail(), user.getPassword(), faker.name().firstName()));

        assertEquals("Статус код неверный при изменение имени авторизованного пользователя",
                HttpStatus.SC_OK, response.extract().statusCode());

    }

    @Test
    @DisplayName("Проверка изменение почтового адреса НЕавторизованного пользователя")
    public void editEmailNonLoginUserTest() {
        accessToken = "";
        //userClient.logout(refreshTokenAfterRegistration);
        ValidatableResponse response = userClient.edit(accessToken,
                new User(faker.internet().emailAddress(), user.getPassword(), user.getName()));

        assertEquals("Статус код неверный при изменение почтового адреса НЕавторизованного пользователя",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());

    }

    @Test
    @DisplayName("Проверка изменение имени НЕавторизованного пользователя")
    public void editNameNonLoginUserTest() {
        //accessToken = accessTokenExtraction(user);
        accessToken = "";
        //userClient.logout(refreshTokenAfterRegistration);
        ValidatableResponse response = userClient.edit(accessToken,
                new User(user.getEmail(), user.getPassword(), faker.name().firstName()));

        assertEquals("Статус код неверный при изменение имени НЕавторизованного пользователя",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());

    }

    /*@Test
    @DisplayName("")
    public void logoutTest() {
        accessToken = accessTokenExtraction(user);


        ValidatableResponse response = userClient.logout(refreshTokenAfterRegistration);

        assertEquals("Статус код неверный при изменение имени НЕавторизованного пользователя",
                HttpStatus.SC_OK, response.extract().statusCode());
    }*/


}
