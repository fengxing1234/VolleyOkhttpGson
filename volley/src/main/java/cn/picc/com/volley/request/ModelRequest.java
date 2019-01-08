package cn.picc.com.volley.request;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import cn.picc.com.volley.data.Model;
import cn.picc.com.volley.http.VolleyRequestError;
import okhttp3.RequestBody;

public class ModelRequest<T> extends OkRequest<T> {
    private Type type;
    private Response.Listener<T> mListener;
    private DataHandler mHandler;

    public ModelRequest(int method, String url, Type type, @NonNull Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, null, type, null, listener, errorListener);
    }

    public ModelRequest(int method, String url, RequestBody requestBody, Type type, @NonNull Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, requestBody, type, null, listener, errorListener);
    }

    public ModelRequest(int method, String url, Type type, DataHandler dataHandler, @NonNull Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, null, type, dataHandler, listener, errorListener);
    }

    public ModelRequest(int method, String url, RequestBody requestBody, Type type, DataHandler dataHandler, @NonNull Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, errorListener);
        this.type = type;
        this.mListener = listener;
        if (dataHandler == null) {
            dataHandler = new DataHandler();
        }
        mHandler = dataHandler;
    }

    public Type getType() {
        return type;
    }

    public DataHandler getDataHandler() {
        return mHandler;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            JsonObject json = mHandler.parseGsonResponse(response).getAsJsonObject();
            if (mHandler.isSuccess(response, json)) {
                return Response.success((T) Model.commonCreate(mHandler.getData(response, json), type), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyRequestError(this, mHandler.getStatusCode(response, json), mHandler.getMessage(response, json)));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
