package kuangyibao.com.kuangyibao.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.TipEntity;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.TimeUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**后台获取通知服务
 * Created by apple on 18-4-12.
 */

public class PushTipService extends Service {
    public static final String ACTION = "kuangyibao.com.kuangyibao.service.PushTipService";

    private Notification mNotification;
    private NotificationManager mManager;
    private int id = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        initNotifiManager();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(TimeUtils.isStartTip()){//上午11点半 下午五点半
            getNotification();
        }
    }

    //初始化通知栏配置
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //弹出Notification
    private void showNotification(TipEntity tipEntity) {

        mNotification = new Notification.Builder(this)
                .setContentTitle(tipEntity.getStatusTitle())
                .setContentText(tipEntity.getStatusCont())
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(createIntent())
                .build();
        mManager.notify(id, mNotification);
        id ++;
    }

    /**
     * 创建pendingIntent
     * @return
     */
    private PendingIntent createIntent(){
        Intent i = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        return pendingIntent;
    }

    /**
     * 获取用户状态
     */
    private void getNotification() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_STATUSTIP_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("uId" , Utils.getUID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("statusVer" , System.currentTimeMillis() + "")
                .build()
                .execute(new Callback<TipEntity>() {
                    @Override
                    public TipEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        TipEntity adEntity = JSON.parseObject(string, new TypeReference<TipEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(TipEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            showNotification(o);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
