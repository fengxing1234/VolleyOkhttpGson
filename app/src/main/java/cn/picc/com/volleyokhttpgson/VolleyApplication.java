package cn.picc.com.volleyokhttpgson;

import android.app.Application;
import android.content.Context;

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
