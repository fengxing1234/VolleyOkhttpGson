# VolleyOkhttpGson
#项目地址
https://github.com/fengxing1234/VolleyOkhttpGson/tree/master


## Volley

[Volley官方](https://developer.android.com/training/volley/)

[Github Volley](https://github.com/mcxiaoke/android-volley)


[官方教程](https://developer.android.com/training/volley/simple)

[Android Volley完全解析(一)，初识Volley的基本用法 ](https://link.juejin.im/?target=http%3A%2F%2Fblog.csdn.net%2Fguolin_blog%2Farticle%2Fdetails%2F17482095)

[Android Volley完全解析(二)，使用Volley加载网络图片 ](https://link.juejin.im/?target=http%3A%2F%2Fblog.csdn.net%2Fsinyu890807%2Farticle%2Fdetails%2F17482165)

[Android Volley完全解析(三)，定制自己的Request ](https://link.juejin.im/?target=http%3A%2F%2Fblog.csdn.net%2Fguolin_blog%2Farticle%2Fdetails%2F17612763)

[Android Volley 之自定义Request ](https://link.juejin.im/?target=http%3A%2F%2Fblog.csdn.net%2Flmj623565791%2Farticle%2Fdetails%2F24589837)

>Volley是Google开发的HTTP库，它使Android应用程序的网络更容易，最重要的是，更快。

>Volley非常适合去进行数据量不大，但通信频繁的网络操作，而对于大数据量的网络操作，比如说下载文件等，Volley的表现就会非常糟糕。

#####官方给出的Volley优点：

![image.png](https://upload-images.jianshu.io/upload_images/4118241-f2f9a0308a40eb69.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本人英文不好，用软件翻译一下。。

1. 自动调度网络请求。
2. 多个并发网络连接。 
3. 具有标准HTTP缓存一致性的透明磁盘和内存响应缓存。 
4. 支持请求优先级。 
5. 取消请求API。
6. 可以取消单个请求，也可以设置要取消的请求块或范围。
7. 易于定制，例如，重试和退避。 
8. 强大的排序，可以使用从网络异步获取的数据轻松正确填充UI。 
9. 调试和跟踪工具。

##OkHttp

##Gson

# 使用
1. 在Application中初始化HttpClient
```
public class VolleyApplication extends Application {

    private static Context context;
    private HttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        httpClient = new HttpClient(this);
        context = this;
    }

    public HttpClient getClient() {
        return httpClient;
    }

    public static Context getContext() {
        return context;
    }
}

```
2. 配置 ```HttpClient```类
根据接口文档配置头信息
```
public Map<String, String> generateHeader(RequestBody requestBody) {
        HashMap<String, String> map = new HashMap<>();
        map.put("", "8");
        map.put("", "123456");
        map.put("", DeviceUtil.getUUID(VolleyApplication.getContext()));
        map.put("", "1");


        String token = "";
        if (!"".equals(token)) {
            map.put("picc-m-sid", token);
        }
        return map;
    }
```
3. 创建子类实现DataHandler
根据接口文档填充。

```
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
```

4. 定义接口
```
public Request<?> queryClaimPage(Context context, String page_id, String report_id, Response.Listener<JsonElement> listener, Response.ErrorListener error) {
        String url = API_CLAIM_QUERY_CLAIM_PAGE;
        JsonBuilder builder = new JsonBuilder();
        builder.add("page_id", page_id);
        builder.add("report_id", report_id);
        RequestBody requestBody = builder.build();
        return addGsonRequest(context, POST, url, requestBody, requestBody, listener, error);
    }
```

5. 代码中使用
首先初始化```HttpClient```类
```
client = HttpClient.getClient(this);
```
```
@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_1:
                request = client.getProvinceCity(this, new Response.Listener<JsonElement>() {
                    @Override
                    public void onResponse(JsonElement response) {
                        tv.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toast(error.getMessage());
                    }
                });
                break;
            case R.id.test_2:
                client.queryClaimPage(this, "4", "08cc85f8db9f4befbebf2e57f97eb016", new Response.Listener<JsonElement>() {
                    @Override
                    public void onResponse(JsonElement response) {
                        tv.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toast(error.getMessage());
                    }
                });
                break;
            case R.id.test_cancel:
                client.cancelAll(this);
                boolean cancel = client.isCancel(request);
                Log.d("isCancel", "onClick: " + cancel);
                break;
        }
    }

```
**主要**
根据接口文档一般的项目传递的都是Json格式，在定义接口时可以使用``JsonBuilder builder = new JsonBuilder();```来添加参数。

如果有特殊需求例如 使用表单就不能使用JsonBuilder类。应该使用FormBuilder类。
区别在于:
表单的type：`application/x-www-form-urlencoded`
Json的type:`application/json; charset=utf-8`

现在就可以正常使用了

#取消请求

每一个接口都会传递一个context上下文，如果页面关闭，请求队列还是会在后台请求工作，当请求成功一般都设置设置数据，但是页面关闭了，如果不处理很容易造成崩溃，解决办法就是在页面关闭后者失去焦点时，取消请求。

```
@Override
    protected void onDestroy() {
        super.onDestroy();
        client.cancelAll(this);
    }
```

##  request封装
自定义OkRequest类继承Request（Volley）类。
```

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
```
在这里处理url的拼接，请求体，和数据类型（content-type）以及头信息。
头信息我放在了HttpClient类处理。

#### DataRequest MessageRequest GsonRequest ModelRequest

这四个类都继承OkRequest。区别在与处理数据不通。
- DataRequest：
- MessageRequest：
DataRequest和MessageRequest 差不多相同，都时根据业务逻辑和接口文档定义出来的。
适用于这样的返回
```{"flag":true,"data":{"url":"/mcph5Version/downLoad","version":"254"},"state":"200","msg":"成功返回"}```
DataRequest：取出来的数据data数据。
MessageRequest：取出来的时msg数据。

- GsonRequest：
获取到的JsonElement数据，可以说时万能的request。
```
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
```
在成功回调中使用Gson就可以解析出来。

- ModelRequest：

```
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

```
使用时需要传入转换的类型，然后由Gson把亲故的数据转换成实体类。

在定义接口时：
```
public Request<?> queryHospitalInfo(Context context, int now_page, int row_num, String hospital_name, Response.Listener<HospitalInfo> listener, Response.ErrorListener error) {
        String url = API_CLAIM_QUERY_HOSPITAL_INFO;
        McpJsonBuilder builder = new McpJsonBuilder();
        builder.add("now_page", 1);
        builder.add("row_num", 20);
        builder.add("hospital_name", hospital_name);
        RequestBody requestBody = builder.build();
        return addModelRequest(context, POST, url, requestBody, requestBody, HospitalInfo.class, listener, error);
    }
```
使用ModelRequest 在定义接口时，需要传入类型，```HospitalInfo.class```。

如果是一个集合应该这样写：
```
public Request<?> queryBankInfo(Context context, Response.Listener<List<BankData>> listener, Response.ErrorListener error) {
        String url = API_CLAIM_QUERY_BANK_INFO;
        McpJsonBuilder builder = new McpJsonBuilder();
        RequestBody requestBody = builder.build();
        return addModelRequest(context, POST, url, requestBody, requestBody, new TypeToken<List<BankData>>() {
        }.getType(), listener, error);
    }
```
其它不变。


## 设置策略

```
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
```

在定义接口时，这几个方法对应上面的说的request。
这里都时做了相同的几件事：
- 定义头信息`generateHeader`方法
- 设置策略
- 把请求添加到队列
- 获取到完整的url

完整的url是由下面组成的
```
private static final String mDebugApiHost = "http://mtest.baidu.cn/abcd/";
private String getBaseHost() {
        return mDebugApiHost;
    }
```

```
public static final String API_CLAIM_QUERY_CLAIM_PAGE = "/api/claim/query-claim-page";

public Request<?> queryClaimPage(Context context, String page_id, String report_id, Response.Listener<JsonElement> listener, Response.ErrorListener error) {
        String url = API_CLAIM_QUERY_CLAIM_PAGE;
        JsonBuilder builder = new JsonBuilder();
        builder.add("page_id", page_id);
        builder.add("report_id", report_id);
        RequestBody requestBody = builder.build();
        return addGsonRequest(context, POST, url, requestBody, requestBody, listener, error);
    }

```

最后调用VolleyClient的方法，得到完整的url
```
public String getAbsoluteUrl(String relativeUrl) {
        return !relativeUrl.startsWith("http://") && !relativeUrl.startsWith("https://") ? (relativeUrl.startsWith("/") ? this.getHost() + relativeUrl.substring(1) : this.getHost() + relativeUrl) : relativeUrl;
    }
```
