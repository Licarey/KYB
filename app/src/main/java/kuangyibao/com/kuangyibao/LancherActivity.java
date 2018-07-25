package kuangyibao.com.kuangyibao;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andsync.xpermission.XPermissionUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;


import java.util.ArrayList;

import kuangyibao.com.kuangyibao.base.AppManager;
import kuangyibao.com.kuangyibao.base.BaseApplication;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.ADEntity;
import kuangyibao.com.kuangyibao.entity.IfGuessEntity;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.service.PushTipService;
import kuangyibao.com.kuangyibao.util.DialogUtil;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.PushServiceUtils;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 开屏页
 */
public class LancherActivity extends AppCompatActivity {
    private String currentTime = System.currentTimeMillis() + "";
    private String messageId = "";
    private String personNum = "0";
    private int READ_PHONE_STATE = 133;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        if(!Utils.isConnected(this)){
            Toast.makeText(this , "网络异常，请检查网络" , 0).show();
            return;
        }
        getReadPhoneState();
    }

    /**
     * 获取手机imei权限
     */
    private void getReadPhoneState() {
        XPermissionUtils.requestPermissions(this, READ_PHONE_STATE , new String[] { Manifest.permission.READ_PHONE_STATE},
                new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Log.e("LM" , "onPermissionGranted-----------");
                        getAD();
                        PushServiceUtils.startPollingService(LancherActivity.this,  30 , PushTipService.class, PushTipService.ACTION);
                        findViewById(R.id.mJump).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (TextUtils.isEmpty(messageId) || !messageId.equals("2")) {
                                    Intent intent = new Intent(LancherActivity.this, GuessPriceActivity.class);
                                    intent.putExtra("personNum", personNum);
                                    startActivity(intent);
                                } else {
                                    startActivity(new Intent(LancherActivity.this, HomeActivity.class));
                                }
                                OkHttpUtils.getInstance().cancelTag("100");
                                OkHttpUtils.getInstance().cancelTag("101");
                                mHandler.removeCallbacks(runnable);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(final String[] deniedPermissions, boolean alwaysDenied) {
                        Log.e("LM" , "onPermissionDenied-----------");
                        Toast.makeText(LancherActivity.this, "获取手机访问权限失败", Toast.LENGTH_SHORT).show();
                        if (alwaysDenied) { // 拒绝后不再询问 -> 提示跳转到设置
                            DialogUtil.showPermissionManagerDialog(LancherActivity.this, "手机访问权限");
                        } else {    // 拒绝 -> 提示此公告的意义，并可再次尝试获取权限
                            new AlertDialog.Builder(LancherActivity.this).setTitle("温馨提示")
                                    .setMessage("我们需要手机访问权限才能正常使用该功能")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("验证权限", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            XPermissionUtils.requestPermissionsAgain(LancherActivity.this, deniedPermissions,
                                                    READ_PHONE_STATE);
                                        }
                                    })
                                    .show();
                        }
                    }
                });
    }

    /**
     * 获取用户状态
     */
    private void getUserStatus() {
        OkHttpUtils.getInstance()
                .post()
                .tag("100")
                .url(Urls.POST_IFGUESS_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("uId" , Utils.getUID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .build()
                .execute(new Callback<IfGuessEntity>() {
                    @Override
                    public IfGuessEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        IfGuessEntity adEntity = JSON.parseObject(string, new TypeReference<IfGuessEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(IfGuessEntity o, int i) {
                        if(o != null){
                            messageId = o.getMessageId();
                            if(o.getMessageId().equals("1")){//成功才有值
                                personNum = o.getGuessNum();
                                findViewById(R.id.mJump).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void getAD(){
        //  /api/ver.shtml
        OkHttpUtils.getInstance()
                .post()
                .tag("101")
                .url(Urls.POST_AD_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("TIMESAMP" , currentTime)
                .addHeader("SIGNATURE" , getSignture())//MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
                .addHeader("uId" , Utils.getUID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("ver", SpUtils.getString(this , "ver" , "1"))
                .addParams("advVer", SpUtils.getString(this , "adver" , "1524580242251"))
                .build()
                .execute(new Callback<ADEntity>() {
                    @Override
                    public ADEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        ADEntity adEntity = JSON.parseObject(string, new TypeReference<ADEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(LancherActivity.this , e.getMessage() + "" , 0).show();
                    }

                    @Override
                    public void onResponse(ADEntity o, int i) {
                        getUserStatus();
                        if(o != null && o.getMessageId().equals("1")){
                            BaseApplication.getInstance().setVersionName(o.getVer());//版本号
                            Log.e("LM" , "服务器版本 " + o.getVer());
                            SpUtils.putString(LancherActivity.this , "token" , o.getToken());
                            SpUtils.putString(LancherActivity.this , "adver" , o.getAdvVer());
                            if(!TextUtils.isEmpty(o.getAppAdvImg())){
                                SpUtils.putString(LancherActivity.this , "adimg" , o.getAppAdvImg());
                            }
                            if(!TextUtils.isEmpty(o.getAppAdvImg())){//广告图
                                ImageUtils.loadImageView(LancherActivity.this , o.getAppAdvImg(), (ImageView) findViewById(R.id.bg));
                            }else if(!TextUtils.isEmpty(SpUtils.getString(LancherActivity.this , "adimg" , ""))){
                                ImageUtils.loadImageView(LancherActivity.this , SpUtils.getString(LancherActivity.this , "adimg" , ""), (ImageView) findViewById(R.id.bg));
                            }
                        }
                        if("-21".equals(o.getMessageId()) || "-20".equals(o.getMessageId())){
                            SpUtils.putString(LancherActivity.this , "token" , "");
                            Toast.makeText(LancherActivity.this , o.getMessageCont() + "" , 0).show();
                            AppManager.getInstance().finishAllActivity();
                        }else{
                            mHandler.postDelayed(runnable , 5000);
                        }
                    }
                });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(messageId.equals("2")){//已猜过
                startActivity(new Intent(LancherActivity.this , HomeActivity.class));
            }else{
                Intent intent = new Intent(LancherActivity.this , GuessPriceActivity.class);
                intent.putExtra("personNum" , personNum);
                startActivity(intent);
            }
            finish();
        }
    };

    //MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
    private String getSignture(){
        String s = currentTime + Utils.md5(Utils.getDeviceUniqID(this)).toUpperCase();
        return Utils.md5(s).toUpperCase();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
