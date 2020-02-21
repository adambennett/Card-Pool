package versionP;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Database {

    private static ObjectMapper objMap = new ObjectMapper();

    public static void load(String fullFile) {
        try {
            objMap.readValue(new File(fullFile), new TypeReference<ArrayList<CardNode>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
