package kuangyibao.com.kuangyibao.util;

import android.content.Context;
import android.content.SharedPreferences;

/** 本地序列化工具类
 * Created by apple on 18-3-25.
 */

public class SpUtils {
    //存储数据需要editor对象，取数据直接首选项对象就好了。
    private static final String SHARE_PREFER_NAME = "config";
    private static SharedPreferences mSharedPreferences;

    public static void putBoolean(Context context, String key, boolean value) {
        if (mSharedPreferences == null) {
            //私有模式
            mSharedPreferences = context.getSharedPreferences(SHARE_PREFER_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SHARE_PREFER_NAME, Context.MODE_PRIVATE);
        }
        //如果获取不到就显示默认值
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SHARE_PREFER_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SHARE_PREFER_NAME, Context.MODE_PRIVATE);
        }
        //如果获取不到就显示默认值
        return mSharedPreferences.getString(key, defaultValue);
    }
}
