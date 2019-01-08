package cn.picc.com.volley.requestbody;

import org.json.JSONException;
import org.json.JSONObject;

import cn.picc.com.volley.data.Model;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 构建Json格式
 */
public class JsonBuilder {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private JSONObject jsonObject;

    public JsonBuilder() {
        jsonObject = new JSONObject();
    }

    public JsonBuilder add(String name, String value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonBuilder add(String name, boolean value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonBuilder add(String name, Object value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonBuilder add(String name, int value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public RequestBody build() {
        return RequestBody.create(JSON, jsonObject.toString());
    }


    public RequestBody buildGson(Object data) {
        String json = Model.toJson(data);
        return RequestBody.create(JSON, json);
    }


}
