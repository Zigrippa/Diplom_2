package models;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static config.Config.*;
import static io.restassured.RestAssured.given;

public class UserClient {

    public UserClient() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(requestSpec())
                .and()
                .body(user)
                .when()
                .post(USER_REGISTER)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(User user) {
        return given()
                .spec(requestSpec())
                .and()
                .body(user)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    @Step("Логаут пользователя")
    public ValidatableResponse logout(String refreshToken) {
        return given()
                .header("token", refreshToken)
                .spec(requestSpec())
                .when()
                .post(USER_LOGOUT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(requestSpec())
                .when()
                .delete(USER_GET_UPDATE_DELETE)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse edit(String accessToken, User user) {
        return given()
                .header("Authorization", accessToken)
                .spec(requestSpec())
                .and()
                .body(user)
                .when()
                .patch(USER_GET_UPDATE_DELETE)
                .then();
    }



}
