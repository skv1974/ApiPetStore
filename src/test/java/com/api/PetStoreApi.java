package com.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Data;

@Data
public class PetStoreApi {

    private  String baseUrl;
    private  String authToken;
    private  String endpointPutUser;
    private  String endpointGetUser;
    private  String endpointPostUser;


    @Step ("Отправка запроса PutUser для обновления данных Пользователя по его имени (string)")
    public Response putUser(String userName, String body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .put(baseUrl + endpointPutUser + userName);
    }


    @Step ("Отправка запроса GetUser для получения данных Пользователя по его имени")
    public Response getUser(String userName) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .get(baseUrl + endpointPutUser + userName);
    }

    @Step ("Отправка запроса PostUser для создания Пользователя")
    public Response postUser(String body){
        return RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(body)
                .post(baseUrl + endpointPostUser);
    }
}