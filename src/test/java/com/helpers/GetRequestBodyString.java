package com.helpers;

import com.model.UserData;
import io.qameta.allure.Step;

public class GetRequestBodyString {

    @Step("Формирование Тела запроса Put в виде строки")
    public static String getPutRequestBodyString(UserData userData) {
        String body = "{\"id\": " + userData.getId() + ","
                + " \"username\": \"" + userData.getUserName() + "\","
                + " \"firstName\": \"" + userData.getFirstName() + "\","
                + " \"lastName\": \"" + userData.getLastName() + "\","
                + " \"email\": \"" + userData.getEmail() + "\","
                + " \"password\": \"" + userData.getPassword() + "\","
                + " \"phone\": \"" + userData.getPhone() + "\","
                + " \"userStatus\": " + userData.getUserStatus() + "}";
        return body;
    }
}
