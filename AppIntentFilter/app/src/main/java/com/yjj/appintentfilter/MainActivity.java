package com.yjj.appintentfilter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int RC_INTENT_BACK_SS = 2349;
    private static final String TAG = "TEST";
    private static final String PACKAGE_SS = "tw.com.sinyi.sinyistore";
    private String packageSS;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == RC_INTENT_BACK_SS) {
            String msg = data.getStringExtra("msg");
            if (!TextUtils.isEmpty(msg)) {

                new AlertDialog.Builder(this)
                        .setMessage("從外部APP返回\n" + msg)
                        .show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        ArrayList<AppInfoBean> list = new ArrayList<>();
        list.add(new AppInfoBean(AppInfoBean.Type.OPEN, "jp.naver.line.android", "啟動LINE", null));
        list.add(new AppInfoBean(AppInfoBean.Type.SEND_MSG, "jp.naver.line.android", "LINE send message", "哈囉你好"));
        list.add(new AppInfoBean(AppInfoBean.Type.OPEN, "com.facebook.katana", "啟動FB", null));
        list.add(new AppInfoBean(AppInfoBean.Type.SEND_MSG, "com.facebook.katana", "FB send message", "哈囉哈囉"));


        //SS 測試
        packageSS = BuildConfig.DEBUG ? PACKAGE_SS + ".debug" : PACKAGE_SS;
        list.add(new AppInfoBean(AppInfoBean.Type.OPEN, packageSS, "啟動SS", null));
//       String json = "{\"id\":\"G007049453\",\"title\":\"開啟對應頁面\",\"type\":\"1\",\"message\":\"訊息內容包含message type判斷類型，以利執行後續動作\"}";
        list.add(new AppInfoBean(AppInfoBean.Type.SEND_MSG, packageSS, "SS send message", "message from SS"));
//        list.add(new AppInfoBean(AppInfoBean.Type.ACTION_VIEW, packageSS, "測試Intent.ACTION_VIEW", null));

        MyAdapter adapter = new MyAdapter(this, list);
        gridView.setAdapter(adapter);

    }

    private void startIntentAction(String packageName, String activityClass) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(packageName, activityClass);
//        intent.setType("text/plain");

        startActivity(intent);
    }


    private void sendMessageToApp(String packageName, String msg) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "來自外部APP傳遞的訊息" + msg);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(packageName);
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        bundle.putString("url", "http://tw.yahoo.com/");
        sendIntent.putExtras(bundle);
        startActivityForResult(sendIntent, RC_INTENT_BACK_SS);
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


    private class MyAdapter extends BaseAdapter {

        private ArrayList<AppInfoBean> mList;
        private Context mContext;

        public MyAdapter(Context context, ArrayList<AppInfoBean> list) {
            this.mList = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            ViewHolder vh = null;

            if (view == null) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.app_grid_item, viewGroup, false);
                vh = new ViewHolder();
                vh.button = (Button) v.findViewById(R.id.btn);
                vh.imgIcon = (ImageView) v.findViewById(R.id.img_icon);

                view = v;
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }

            AppInfoBean bean = (AppInfoBean) getItem(i);
            String text = bean.getType() + " / " + bean.getTitle();
            final String packName = bean.getPackageName();
            Drawable appIcon = null;
            try {
                appIcon = mContext.getPackageManager().getApplicationIcon(packName);
                vh.imgIcon.setImageDrawable(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            vh.button.setText(text);
            vh.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AppInfoBean b = ((AppInfoBean) getItem(i));
                    int type = b.getType();
                    switch (type) {
                        case AppInfoBean.Type.OPEN:
                            openInstallApp(b.getPackageName());
                            break;
                        case AppInfoBean.Type.SEND_MSG:
                            sendMessageToApp(b.getPackageName(), b.getMessage());
                           /* AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setMessage(b.getMessage())
                                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).create();
                            dialog.show();*/
                            break;
                        case AppInfoBean.Type.SEND_DATA:

                            break;
                        case AppInfoBean.Type.ACTION_VIEW:
                            startIntentAction(packName, packName + ".view.WebviewActivity");

                            break;
                    }
                }
            });

            return view;
        }


        private final class ViewHolder {

            public Button button;
            public ImageView imgIcon;
        }
    }

}
