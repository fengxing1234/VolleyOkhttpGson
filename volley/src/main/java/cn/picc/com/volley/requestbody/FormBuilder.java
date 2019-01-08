package cn.picc.com.volley.requestbody;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 用于构建表单
 * <p>
 * 适用于 "application/x-www-form-urlencoded"
 */
public class FormBuilder {
    FormBody.Builder builder;

    public FormBuilder() {
        builder = new FormBody.Builder();
    }

    public FormBuilder(Map<String, String> map) {
        builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
    }

    public FormBuilder add(String name, String value) {
        builder.add(name, value == null ? "" : value);
        return this;
    }

    public FormBuilder add(String name, Object value) {
        builder.add(name, value == null ? "" : value.toString());
        return this;
    }

    public FormBuilder addEncoded(String name, String value) {
        builder.addEncoded(name, value);
        return this;
    }

    public RequestBody build() {
        return builder.build();
    }

}
