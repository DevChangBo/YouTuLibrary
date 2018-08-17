package com.example.mralbert.youtulibrary;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.youtulibrary.YouTuConfig;
import com.example.youtulibrary.plugins.YouTuPlugin;

import org.json.JSONException;



/**
 * ================================================================
 * 创建时间：2018-6-7 14:26:44
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    YouTuPlugin.getInstance(MainActivity.this).execute("","0",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.bt_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    YouTuPlugin.getInstance(MainActivity.this).execute("","1",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.bt_button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    YouTuPlugin.getInstance(MainActivity.this).execute("","2",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.bt_button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    YouTuPlugin.getInstance(MainActivity.this).execute("","3",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 返回页面所获得值
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) return;
        if (requestCode == YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA
                && resultCode == YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA) {
            String responseBody=intent.getStringExtra("responseBody");
            Toast.makeText(this,responseBody, Toast.LENGTH_LONG ).show();
            Log.d("responseBody",responseBody);
        }
    }
}
