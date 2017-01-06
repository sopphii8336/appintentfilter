package com.yjj.appintentfilter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONObject;

/**
 * Created by Eric on 2017/1/4.
 */

public class AppInfoBean {


    public static final class Type {
        public static final int OPEN = 1;
        public static final int SEND_MSG = 2;
        public static final int SEND_DATA = 3;
        public static final int ACTION_VIEW = 4;
    }

    /**
     * packageName : tw.com.jtc
     * title : JTCAPP
     * message : 訊息來自JTC
     */

    private String packageName;
    private String title;
    private String message;
    private int type;
    private JSONObject jsonObject;

    public AppInfoBean(int type, String packageName, String title, String message, JSONObject jsonObject) {
        this.type = type;
        this.packageName = packageName;
        this.title = title;
        this.message = message;
        this.jsonObject = jsonObject;
    }

    public AppInfoBean(int type, String packageName, String title, @Nullable String message) {
        this.type = type;
        this.packageName = packageName;
        this.title = title;
        this.message = message;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
