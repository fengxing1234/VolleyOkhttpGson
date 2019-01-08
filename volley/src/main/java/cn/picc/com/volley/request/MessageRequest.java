package cn.picc.com.volley.request;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

import cn.picc.com.volley.http.VolleyRequestError;
import okhttp3.RequestBody;

public class MessageRequest extends OkRequest<String> {
    private Response.Listener<String> mListener;
    private DataHandler mHandler;


    public MessageRequest(int method, String url, @NonNull Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(method, url, null, null, listener, errorListener);
    }

    public MessageRequest(int method, String url, RequestBody requestBody, @NonNull Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(method, url, requestBody, null, listener, errorListener);
    }


    public MessageRequest(int method, String url, DataHandler dataHandler, @NonNull Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(method, url, null, dataHandler, listener, errorListener);
    }

    public MessageRequest(int method, String url, RequestBody requestBody, DataHandler dataHandler, @NonNull Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, errorListener);
        this.mListener = listener;
        if (dataHandler == null) {
            dataHandler = new DataHandler();
        }
        mHandler = dataHandler;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            JsonObject json = mHandler.parseGsonResponse(response).getAsJsonObject();
            if (mHandler.isSuccess(response, json)) {
                return Response.success(mHandler.getMessage(response, json), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyRequestError(this, mHandler.getStatusCode(response, json), mHandler.getMessage(response, json)));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
