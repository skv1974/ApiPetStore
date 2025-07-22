package com.test;

import com.helpers.GetRequestBodyString;
import com.helpers.JsonReader;
import com.model.UserData;
import com.providers.UserStatusDataProvider;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.api.PetStoreApi;
import com.helpers.ConfigReader;
import org.testng.asserts.SoftAssert;
import java.io.IOException;
import static org.hamcrest.Matchers.equalTo;

/*
    Пример тестов одного запроса API PetStore (PutUser)
    В тестах проверяется изменение только одного из атрибутов Пользователя - userStatus
    Для проверки инициализации (пересоздания Пользователя) используется запрос PostUser
    Для проверки текущего значения userStatus используется другой запрос GetUser, возвращающий все атрибуты Пользователя.
    Сделано допущение, что запросы PostUser и GetUser гарантировано работают правильно (например предыдущими тестами), т.к. доступа к БД нет.
    Для демонстрации использованы ДатаПровайдеры для позитивных и негативных тестов, отдельно проверяется значение NULL

    Данные для подключения берутся из config.properties

    Для проверок используется Пользователь с nameUser - skv1974
    Атрибуты Пользователя загружаются из файла json
    Тестовые значения позитивных и негативных тестов для проверки userStatus загружаются из файлов txt

    т.к. API содержит баги, часть тестов падает
*/

public class UserPutTest {

    private final ConfigReader config = new ConfigReader();
    private final JsonReader jsonReader = new JsonReader();
    private final PetStoreApi petStoreApi = new PetStoreApi();
    private UserData userData = new UserData();

    @BeforeSuite
    public void setUpTests() throws IOException, InterruptedException {

        //загрузка пропертей
        petStoreApi.setBaseUrl(config.getBaseUrl());
        petStoreApi.setAuthToken(config.getAuthToken());
        petStoreApi.setEndpointPutUser(config.getEndpointPutUser());
        petStoreApi.setEndpointGetUser((config.getEndpointGetUser()));
        petStoreApi.setEndpointPostUser((config.getEndpointPostUser()));

        //загрузка данных юзера
        userData = jsonReader.getUserJsonData();

        //проверка существования Пользователя, если его не существует создание Пользователя запросом Post
        Response response = petStoreApi.getUser(userData.getUserName());
        int code = response.then().extract().statusCode();
        if (code!=200) {
            userData.setUserStatus("4791");
            String body = GetRequestBodyString.getPutRequestBodyString(userData);

            Response responsePost = petStoreApi.postUser(body);

            responsePost.then()
                    .statusCode(200);

            //Задержка для сохранения данных Пользователя (очень нестабильно работает апи)
            Thread.sleep(10000);
        }


    }

    @BeforeTest
    public void setUpTest() throws InterruptedException {

        //Пересоздание Пользователя перед каждым тестом
        userData.setUserStatus("4791");
        String body = GetRequestBodyString.getPutRequestBodyString(userData);

        Response responsePost = petStoreApi.postUser(body);

        responsePost.then()
                .statusCode(200);

        //Задержка для сохранения данных Пользователя (очень нестабильно работает апи)
        Thread.sleep(10000);
    }

    @Story ("Проверка ответов со кодом 200")
    @Test (dataProvider = "userStatusDataPos", dataProviderClass = UserStatusDataProvider.class)
    public void putUserTest200(String userStatus) throws IOException, InterruptedException {

        //Формирование Тела запроса в виде строки (статус может быть как числом, так и строкой)
        userData.setUserStatus(userStatus);
        String body = GetRequestBodyString.getPutRequestBodyString(userData);

        //Отправка запроса Put с новым статусом
        Response responsePut = petStoreApi.putUser(userData.getUserName(),body);

        // Проверка кода ответа И
        // Установка ожидаемых атрибутов в теле позитивного ожидаемого ответа
        String expectedCode = "200";
        String expectedType = "unknown";
        String expectedMessage = "1974";

        //Получение/проверка кода ответа от запроса Put
        responsePut.then()
                .statusCode(200);

        //Проверка ответа с помощью СофтАссерт
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("code"), expectedCode,"Проверка значения Code тела ответа");
        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("type"), expectedType, "Проверка значения Type тела ответа");
        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("message"), expectedMessage, "Проверка значения Message тела ответа");

        //Приостановка для того чтобы изменения атрибутов Пользователя сохранились в БД
        Thread.sleep(5000);

        //Проверка нового статуса Пользователя запросом GetUser
        Response responseGet = petStoreApi.getUser(userData.getUserName());

        responseGet.then()
                .statusCode(200);

        //Получение текущего статуса Пользователя
        String currentUserStatus = responseGet.then()
                .extract().jsonPath().getString("userStatus");

        userStatus = userStatus.replaceAll("\"", "");
        softAssert.assertEquals(currentUserStatus, userStatus, "Проверка текущего значения userStatus Пользователя в системе");

        softAssert.assertAll();
    }

    @Story("Проверка установки Статуса 0 при попытке установить userStatus = null")
    @Test(enabled = true)
    public void putUserStatusTestNull() throws InterruptedException {

        String userStatus = null;
        userData.setUserStatus(userStatus);
        String body = GetRequestBodyString.getPutRequestBodyString(userData);

        //Отправка запроса Put с новым статусом
        Response responsePut = petStoreApi.putUser(userData.getUserName(),body);

        // Проверка кода ответа И
        // Установка ожидаемых атрибутов в теле позитивного ожидаемого ответа
        String expectedCode = "200";
        String expectedType = "unknown";
        String expectedMessage = "1974";

        //Получение/проверка кода ответа от запроса Put
        responsePut.then()
                .statusCode(200);

        //Проверка ответа с помощью СофтАссерт
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("code"), expectedCode);
        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("type"), expectedType);
        softAssert.assertEquals(responsePut.then()
                .extract().jsonPath().getString("message"), expectedMessage);

        //Приостановка для того чтобы изменения атрибутов Пользователя сохранились в БД
        Thread.sleep(5000);

        //Проверка нового статуса Пользователя запросом GetUser
        Response responseGet = petStoreApi.getUser(userData.getUserName());

        responseGet.then()
                .statusCode(200);

        //Получение текущего статуса Пользователя
        String currentUserStatus = responseGet.then()
                .extract().jsonPath().getString("userStatus");

        //Ожидаемый ответ = 0
        softAssert.assertEquals(currentUserStatus, "0");

        softAssert.assertAll();

    }

    @Story ("Проверка ответов со кодом 500")
    @Test(dataProvider = "userStatusDataNeg", dataProviderClass = UserStatusDataProvider.class)
    public void putUserTest500(String userStatus) throws IOException, InterruptedException {

        //Получение текущего статуса Пользователя
        Response responseGet = petStoreApi.getUser(userData.getUserName());
        int currentUserStatus = Integer.parseInt(responseGet.then()
                .extract().jsonPath().getString("userStatus"));

        int expectedСode = 500;
        String expectedType = "unknown";
        String expectedMessage = "something bad happened";

        //Формирование Тела запроса в виде строки (статус может быть как числом, так и строкой)
        userData.setUserStatus(userStatus);
        String body = GetRequestBodyString.getPutRequestBodyString(userData);

        //Отправка запроса Put с новым статусом
        Response responsePut = petStoreApi.putUser(userData.getUserName(),body);

        // Проверка тела ответа из полученного Рест ответа (без использования SoftAsserts)
        responsePut.then()
                .statusCode(500);
        responsePut.then()
                .body("code", equalTo(expectedСode))
                .body("type", equalTo(expectedType))
                .body("message", equalTo(expectedMessage));

        //Приостановка для того чтобы изменения атрибутов Пользователя сохранились в БД
        Thread.sleep(5000);

        //Проверка нового статуса Пользователя запросом GetUser (Ожидается что не изменился)
        responseGet = petStoreApi.getUser(userData.getUserName());
        responseGet.then().
                statusCode(equalTo(200)).
                body("userStatus", equalTo(currentUserStatus));
    }

}