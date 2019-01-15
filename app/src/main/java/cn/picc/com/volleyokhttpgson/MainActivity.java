package cn.picc.com.volleyokhttpgson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HttpClient client;
    private TextView tv;
    private Request<?> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_1).setOnClickListener(this);
        findViewById(R.id.test_2).setOnClickListener(this);
        client = HttpClient.getClient(this);
        tv = (TextView) findViewById(R.id.tv_result);
    }

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

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.cancelAll(this);
    }
}
