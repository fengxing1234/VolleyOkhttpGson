package cn.picc.com.volley.request;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.IOException;

import cn.picc.com.volley.utils.LogUtils;
import okhttp3.RequestBody;
import okio.Buffer;

public abstract class OkRequest<T> extends Request<T> {

    protected final String TAG = "OkRequest";

    private RequestBody requestBody;

    public OkRequest(int method, String url, RequestBody body, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
        this.requestBody = body;
    }

    @Override
    public String getUrl() {
        if (isRequestBodyNoNeed())
            return super.getUrl() + "?" + getParamsString();
        else
            return super.getUrl();
    }

    protected String getParamsString() {
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.readUtf8();
    }

    protected boolean isRequestBodyNoNeed() {
        return (getMethod() == Method.GET || getMethod() == Method.DELETE) && requestBody != null;
    }

    public void setRequestBody(RequestBody body) {
        this.requestBody = body;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    @Override
    public String getBodyContentType() {
        if (!isRequestBodyNoNeed() && requestBody != null) {
            return requestBody.contentType().toString();
        }
        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (!isRequestBodyNoNeed() && requestBody != null) {
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
                return buffer.readByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                buffer.close();
            }
        } else {
            return super.getBody();
        }
    }


    protected void dd(String msg) {
        LogUtils.LOGD(TAG, msg);
    }

    protected void dd(String msg, Throwable ex) {
        LogUtils.LOGD(TAG, msg, ex);
    }

}
