package cn.picc.com.volleyokhttpgson;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.picc.com.volley.http.OkHttp3Stack;
import cn.picc.com.volley.http.VolleyClient;
import cn.picc.com.volley.request.DataHandler;
import cn.picc.com.volley.request.DataRequest;
import cn.picc.com.volley.request.GsonRequest;
import cn.picc.com.volley.request.MessageRequest;
import cn.picc.com.volley.request.ModelRequest;
import cn.picc.com.volley.requestbody.JsonBuilder;
import okhttp3.RequestBody;
import okio.Buffer;

import static com.android.volley.Request.Method.POST;

public class HttpClient extends VolleyClient {

    private static final int DEFAULT_SOCKET_TIMEOUT_MS = 30000;
    private static final int MAX_SOCKET_TIMEOUT_MS = 300000;
    private static final int SHORTER_SOCKET_TIMEOUT_MS = 5000;
    private static final int LONGER_SOCKET_TIMEOUT_MS = 100000;


    private static final String mDebugApiHost = "http://mtest.picclife.cn/ucsp/";
    private static final String mReleaseApiHost = "http://m.picclife.cn/ucsp/";


    public static final String API_USER_GET_PROVINCE_CITY = "/api/user/get-province-city";
    public static final String API_CLAIM_QUERY_CLAIM_PAGE = "/api/claim/query-claim-page";



    private DataHandler mDataHandler;

    public HttpClient(Context context) {
        //super(createQueue(context));
        this(context, null);

    }

    public HttpClient(Context context, String baseHost) {
        //super(createQueue(context));
        super(context);
        setHost(baseHost == null ? getBaseHost() : baseHost);
        mDataHandler = new McpDataHandler();
    }

    private String getBaseHost() {
        return mDebugApiHost;
    }

    public static HttpClient getClient(Context context) {
        return ((VolleyApplication) context.getApplicationContext()).getClient();
    }


    public static RequestQueue createQueue(Context context) {
        RequestQueue queue =
                new RequestQueue(new DiskBasedCache(new File(context.getCacheDir(), "volley")),
                        new BasicNetwork(new OkHttp3Stack()), 4,
                        new ExecutorDelivery(new Handler(Looper.getMainLooper()) {
                            @Override
                            public void dispatchMessage(Message msg) {
                                super.dispatchMessage(msg);
                            }
                        }));
        queue.start();
        return queue;
    }

    public Map<String, String> generateHeader(RequestBody requestBody) {
        HashMap<String, String> map = new HashMap<>();
        map.put("platform", "8");
//        map.put("openid", "123456");
        map.put("openid", DeviceUtil.getUUID(VolleyApplication.getContext()));
//        map.put("openid", DeviceUtil.getImei(context));
        map.put("sid-in-header", "1");


        String token = "7ac623a2-136a-41d2-8333-95ad796fb62f";
        if (!"".equals(token)) {
            map.put("picc-m-sid", token);
        }


        Buffer buffer = new Buffer();
        try {
            if (requestBody != null) {
                requestBody.writeTo(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = buffer.snapshot().utf8();
        map.put("secret", secretSignMD5(data));
        return map;
    }

    private String secretSignMD5(String data) {
        String s = data + "123456";
        return Md5Utils.md5hash(s);
    }


    public <T> Request<?> addModelRequest(Context context, int method, String url,
                                          RequestBody requestBody, Type type, final RequestBody requestBodyForHeader,
                                          RetryPolicy retryPolicy,
                                          Response.Listener<T> listener, @NonNull Response.ErrorListener error) {
        ModelRequest<T> request =
                new ModelRequest<T>(method, getAbsoluteUrl(url), requestBody, type, mDataHandler, listener,
                        error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return generateHeader(requestBodyForHeader);
                    }
                };
        request.setRetryPolicy(retryPolicy);
        return add(context, request);
    }

    public Request<?> addMessageRequest(Context context, int method, String url,
                                        RequestBody requestBody, final RequestBody requestBodyForHeader,
                                        RetryPolicy retryPolicy,
                                        Response.Listener<String> listener, @NonNull Response.ErrorListener error) {
        MessageRequest request =
                new MessageRequest(method, getAbsoluteUrl(url), requestBody, mDataHandler, listener,
                        error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return generateHeader(requestBodyForHeader);
                    }
                };
        request.setRetryPolicy(retryPolicy);
        return add(context, request);
    }

    public Request<?> addDataRequest(Context context, int method, String url, RequestBody requestBody,
                                     final RequestBody requestBodyForHeader,
                                     RetryPolicy retryPolicy,
                                     Response.Listener<JsonElement> listener,
                                     @NonNull Response.ErrorListener error) {
        DataRequest request =
                new DataRequest(method, getAbsoluteUrl(url), requestBody, mDataHandler, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return generateHeader(requestBodyForHeader);
                    }
                };
        request.setRetryPolicy(retryPolicy);
        return add(context, request);
    }

    public Request<?> addGsonRequest(final Context context, int method, String url,
                                     RequestBody requestBody, final RequestBody requestBodyForHeader,
                                     RetryPolicy retryPolicy,
                                     Response.Listener<JsonElement> listener, @NonNull Response.ErrorListener error) {
        GsonRequest request =
                new GsonRequest(method, getAbsoluteUrl(url), requestBody, mDataHandler, listener,
                        error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return generateHeader(requestBodyForHeader);
                    }
                };
        request.setRetryPolicy(retryPolicy);
        return add(context, request);
    }

    public Request<?> addGsonRequest(Context context, int method, String url, RequestBody requestBody, RequestBody requestBodyForHeader, Response.Listener<JsonElement> listener, Response.ErrorListener error) {
        return addGsonRequest(context, method, url, requestBody, requestBodyForHeader,
                new DefaultRetryPolicy(DEFAULT_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT),
                listener, error);
    }

    public <T> Request<?> addModelRequest(Context context, int method, String url,
                                          RequestBody requestBody, Type type, final RequestBody requestBodyForHeader,
                                          Response.Listener<T> listener, @NonNull Response.ErrorListener error) {
        return addModelRequest(context, method, url, requestBody, type, requestBodyForHeader,

                new DefaultRetryPolicy(DEFAULT_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT),

                listener, error);
    }


    public Request<?> addMessageRequest(Context context, int method, String url,
                                        RequestBody requestBody, final RequestBody requestBodyForHeader,
                                        Response.Listener<String> listener, @NonNull Response.ErrorListener error) {
        return addMessageRequest(context, method, url, requestBody, requestBodyForHeader,

                new DefaultRetryPolicy(DEFAULT_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT),

                listener, error);
    }


    public Request<?> addDataRequest(Context context, int method, String url, RequestBody requestBody,
                                     final RequestBody requestBodyForHeader, Response.Listener<JsonElement> listener,
                                     @NonNull Response.ErrorListener error) {
        return addDataRequest(context, method, url, requestBody, requestBodyForHeader,
                new DefaultRetryPolicy(DEFAULT_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT),

                listener, error);
    }




    public Request<?> getProvinceCity(Context context, Response.Listener<JsonElement> listener, Response.ErrorListener error) {
        String url = API_USER_GET_PROVINCE_CITY;
        JsonBuilder builder = new JsonBuilder();
        //builder.add("timestamp", timestamp);
        RequestBody requestBody = builder.build();
        return addGsonRequest(context, POST, url, requestBody, requestBody, listener, error);
    }

    public Request<?> queryClaimPage(Context context, String page_id, String report_id, Response.Listener<JsonElement> listener, Response.ErrorListener error) {
        String url = API_CLAIM_QUERY_CLAIM_PAGE;
        JsonBuilder builder = new JsonBuilder();
        builder.add("page_id", page_id);
        builder.add("report_id", report_id);
        RequestBody requestBody = builder.build();
        return addGsonRequest(context, POST, url, requestBody, requestBody, listener, error);
    }

}
