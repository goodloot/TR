package ru.goodloot.tr.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Artur M.
 * @created Oct 10, 2014
 * 
 * @Description ...
 */
public class AbstractUtils {

    protected static JSONParser parser = new JSONParser();

    public <S extends Object> S value(JSONObject json, String key, Class<S> clazz) {
        return json == null ? null : (S) json.get(key);
    }

    public JSONObject jsonValue(JSONObject json, String key) {
        return value(json, key, JSONObject.class);
    }

    public String value(JSONObject json, String key) {
        return value(json, key, String.class);
    }

    public String value(String json, String key) {

        try {
            JSONObject obj = (JSONObject) parser.parse(json);
            return value(obj, key);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
