package cn.picc.com.volley.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by DouO on 7/19/15.
 */
public class ModelGson {
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
