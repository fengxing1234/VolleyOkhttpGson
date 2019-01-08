package cn.picc.com.volley.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.picc.com.volley.utils.LogUtils;

public abstract class VolleyClient {

    public static final String DOMAIN = "";
    public static final String HOST = "";
    private final String TAG = LogUtils.makeLogTag(getClass());
    private String mHost;
    private RequestQueue mRequestQueue;

    public VolleyClient(RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    public VolleyClient(Context context) {
        this(Volley.newRequestQueue(context, new OkHttp3Stack()));
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public Request<?> add(Context context, Request<?> request) {
        request.setTag(context);
        return getRequestQueue().add(request);
    }


    public void cancelAll(Context context) {
        getRequestQueue().cancelAll(context);
    }

    public boolean isCancel(Request<?> request) {
        return request.isCanceled();
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public String getAbsoluteUrl(String relativeUrl) {
        return !relativeUrl.startsWith("http://") && !relativeUrl.startsWith("https://") ? (relativeUrl.startsWith("/") ? this.getHost() + relativeUrl.substring(1) : this.getHost() + relativeUrl) : relativeUrl;
    }

    protected void d(String msg) {
        LogUtils.LOGD(TAG, msg);
    }

    protected void d(String msg, Throwable ex) {
        LogUtils.LOGD(TAG, msg, ex);
    }

}
