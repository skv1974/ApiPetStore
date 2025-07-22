package com.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.UserData;
import java.io.File;
import java.io.IOException;

public class JsonReader {

    public UserData getUserJsonData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File userDataFile = new File("src/test/resources/user_data.json");
        UserData userData = objectMapper.readValue(userDataFile, UserData.class);
        return userData;
    }
}
