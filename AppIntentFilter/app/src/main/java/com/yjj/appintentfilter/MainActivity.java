package com.yjj.appintentfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TEST";
    private Button btnFB, btnLine, btnAPP, btnAPPData, btnAPPSinyi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFB = (Button) findViewById(R.id.btn_fb);
        btnLine = (Button) findViewById(R.id.btn_line);
        btnAPP = (Button) findViewById(R.id.btn_app_1);
        btnAPPData = (Button) findViewById(R.id.btn_app_2);
        btnAPPSinyi = (Button) findViewById(R.id.btn_sinyi);
        btnFB.setOnClickListener(this);
        btnLine.setOnClickListener(this);
        btnAPP.setOnClickListener(this);
        btnAPPData.setOnClickListener(this);
        btnAPPSinyi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String packageName;
        packageName = view.getTag()!=null?view.getTag().toString():null;

        if (view == btnFB) {
        } else if (view == btnLine) {

        } else if (view == btnAPP) {

        } else if (view == btnAPPData) {

        } else if (view == btnAPPSinyi) {

        }
        this.openInstallApp(packageName);
    }


    private void openInstallApp(String packageName) {
        Log.d(TAG, "openInstallApp() called with: packageName = [" + packageName + "]");
        if (TextUtils.isEmpty(packageName)) {
            Toast.makeText(this, "無效的package", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        } else {
            Toast.makeText(this, "未安裝APP - " + packageName, Toast.LENGTH_SHORT).show();
        }
    }
}
