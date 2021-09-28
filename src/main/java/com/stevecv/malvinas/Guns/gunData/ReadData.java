package com.stevecv.malvinas.Guns.gunData;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadData {
    public static Object readJson(String json, String name) throws Exception {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

        return jsonObject.get(name);
    }
}
