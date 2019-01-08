package cn.picc.com.volley.http;

import com.android.volley.Request;
import com.android.volley.VolleyError;

public class VolleyRequestError extends VolleyError {

    private int code;
    private Request<?> request;

    public VolleyRequestError(Request<?> request, int code, String msg) {
        super(msg);
        this.code = code;
        this.request = request;
    }

    public Request<?> getRequest() {
        return request;
    }

    public int getCode() {
        return code;
    }

}
