package com.stevecv.malvinas.Guns.gunData;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class ReadData {
    public Object readJson(String path, String name) throws Exception {
        JSONObject jsonObject = (JSONObject) readJsonSimpleDemo(path);

        return jsonObject.get(name);
    }

    public Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
