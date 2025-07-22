package com.providers;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UserStatusDataProvider {

    @DataProvider(name = "userStatusDataPos")
    public String[] userStatusDataPos() throws IOException {
        //загрузка позитивных значений Статусов
        String path = "src/test/resources/user_status_data_pos.txt";
        return txtReader(path);
    }

    @DataProvider (name = "userStatusDataNeg")
    public String[] userStatusDataNeg() throws IOException {
        //загрузка негативных значений Статусов
        String path = "src/test/resources/user_status_data_neg.txt";
        return txtReader(path);
    }


    //Чтение из файла TXT
    public String[] txtReader(String path) throws IOException {
        //Загрузка значений из файла txt
        File userStatusFile = new File(path);

        return Files.readAllLines(userStatusFile.toPath()).toArray(new String[0]);
    }

}
