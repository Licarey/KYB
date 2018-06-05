package kuangyibao.com.kuangyibao.base;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import kuangyibao.com.kuangyibao.config.Urls;
import okhttp3.OkHttpClient;

/**app
 * Created by apple on 18-3-10.
 */

public class BaseApplication extends Application {
    private Class currentClass;
    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //UMConfigure.init(
        // Context context,
        // String appkey,
        // String channel);
        UMConfigure.init(this , Urls.UMENG_APPKEY , "KuangYiBao" , UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static BaseApplication getInstance() {
        return application;
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class currentClass) {
        this.currentClass = currentClass;
    }
}
