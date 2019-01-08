package cn.picc.com.volley.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

import okhttp3.RequestBody;

public class GsonRequest extends OkRequest<JsonElement> {

    private DataHandler mHandler;

    private Response.Listener<JsonElement> mListener;
    public GsonRequest(int method, String url, RequestBody requestBody, DataHandler handler, Response.Listener<JsonElement> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, requestBody, errorListener);
        this.mListener = listener;
        if(handler == null){
            handler = new DataHandler();
        }
        this.mHandler = handler;
    }

    public GsonRequest(int method, String url, @NonNull Response.Listener<JsonElement> listener, Response.ErrorListener errorListener) {
        this(method, url, null, listener, errorListener);
    }

    public GsonRequest(int method, String url, RequestBody body, @NonNull Response.Listener<JsonElement> listener, Response.ErrorListener errorListener) {
        super(method, url, body, errorListener);
        this.mListener = listener;
    }

    @Override
    protected Response<JsonElement> parseNetworkResponse(NetworkResponse response) {
        try {
            dd(new String(response.data, HttpHeaderParser.parseCharset(response.headers)).trim());
            JsonElement element = new JsonParser().parse(new String(response.data, HttpHeaderParser.parseCharset(response.headers)).trim());
            return Response.success(element, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JsonElement response) {
        mListener.onResponse(response);
    }
}
