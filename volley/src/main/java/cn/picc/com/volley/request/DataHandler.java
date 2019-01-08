package cn.picc.com.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

/**
 * 根据接口规范实现的 handler
 */
public class DataHandler {

    public JsonElement getData(NetworkResponse response, JsonObject json) {
        return json.get("data");
    }

    public int getStatusCode(NetworkResponse response, JsonObject json) {
        return json.get("state").getAsInt();
    }

    public String getMessage(NetworkResponse response, JsonObject json) {
        return json.get("msg").getAsString();
    }

    public boolean isSuccess(NetworkResponse response, JsonObject json) {
        return getStatusCode(response, json) == 200;
    }

    public String getStringFromobject(JsonElement element, String name) {
        return element.getAsJsonObject().get(name).getAsString();
    }

    public boolean getBooleanFromobject(JsonElement element, String name) {
        return element.getAsJsonObject().get(name).getAsBoolean();
    }

    public JsonElement parseGsonResponse(NetworkResponse response) throws UnsupportedEncodingException {
        //Log.w("fengxing", "parseGsonResponse: "+new String(response.data,HttpHeaderParser.parseCharset(response.headers)) );
        //{"flag":true,"data":{"url":"/mcph5Version/downLoad","version":"254"},"state":"200","msg":"成功返回"}
        // trim the string to prevent start with blank, and test if the string
        // is valid JSON, because the parser don't do this :(. If Json is not
        // valid this will return null;
        return new JsonParser().parse(new String(response.data, HttpHeaderParser.parseCharset(response.headers)).trim());
    }

}
