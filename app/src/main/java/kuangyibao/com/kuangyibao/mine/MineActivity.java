package kuangyibao.com.kuangyibao.mine;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import kuangyibao.com.kuangyibao.GuessPriceActivity;
import kuangyibao.com.kuangyibao.LancherActivity;
import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.AppManager;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.ADEntity;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.SignEntity;
import kuangyibao.com.kuangyibao.entity.UserInfoEntity;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.pay.PaySubscribeActivity;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CustomDialog;
import kuangyibao.com.kuangyibao.view.ImageViewPlus;
import kuangyibao.com.kuangyibao.view.LoadingDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的页面
 */
public class MineActivity extends BaseActivity implements View.OnClickListener {
    private ImageViewPlus mIvHead;
    private TextView mJifen , mSign , mEtLoginName , mEtLoginSex , mName;
    private String currentTime = System.currentTimeMillis() + "";

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {
        super.initView();
        mIvHead = findViewById(R.id.mIvHead);
        mEtLoginName = findViewById(R.id.mEtLoginName);
        mName = findViewById(R.id.mName);
        mEtLoginSex = findViewById(R.id.mEtLoginSex);
        mJifen = findViewById(R.id.mTvJifen);//积分
        findViewById(R.id.mTvLogout).setOnClickListener(this);
        findViewById(R.id.mIvBack).setOnClickListener(this);
        findViewById(R.id.mLLUpdate).setOnClickListener(this);
        findViewById(R.id.mLLpwdUpdate).setOnClickListener(this);
        findViewById(R.id.mLLMineHome).setOnClickListener(this);
        findViewById(R.id.mLLMineSub).setOnClickListener(this);
        findViewById(R.id.mLLMinePub).setOnClickListener(this);
        findViewById(R.id.mLLMineFar).setOnClickListener(this);
        findViewById(R.id.mLLMineTiezi).setOnClickListener(this);
        findViewById(R.id.mLLMineLiuyan).setOnClickListener(this);
        mSign = findViewById(R.id.mTvSign);
        mSign.setOnClickListener(this);
        mJifen.setOnClickListener(this);
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if(!Utils.isConnected(this)){
            Toast.makeText(this , "网络异常，请检查网络" , 0).show();
        }else {
            getUserInfo();
            ifSign();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_SHOWCLIENT_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .build()
                .execute(new Callback<UserInfoEntity>() {
                    @Override
                    public UserInfoEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        UserInfoEntity adEntity = JSON.parseObject(string, new TypeReference<UserInfoEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(UserInfoEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            if(!TextUtils.isEmpty(o.getImgUrl()))
                                ImageUtils.loadImageView(MineActivity.this , Urls.BASEURL + o.getImgUrl() , mIvHead);
                            mEtLoginName.setText(o.getuNName() + "");
                            mName.setText(o.getuNName() + "");
                            mEtLoginSex.setText(o.getuSex().equals("1") ? "男" : "女");
                        }else{
                            Toast.makeText(MineActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mTvLogout://登出
                if(!Utils.isConnected(this)){
                    Toast.makeText(this , "网络异常，请检查网络" , 0).show();
                }else {
                    new CustomDialog(this, R.style.Dialog, "确定登出？", new CustomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if(confirm){
                                loginOut();
                            }
                        }

                    }).setTitle("提示").setPositiveButton("登出").show();
                }
                break;
            case R.id.mLLUpdate://个人信息修改
                startActivity(new Intent(this , UpdateInfoActivity.class));
                break;
            case R.id.mLLpwdUpdate://密码修改
                startActivity(new Intent(this , UpdatePwdActivity.class));
                break;
            case R.id.mTvSign://签到
                if(!Utils.isConnected(this)){
                    Toast.makeText(this , "网络异常，请检查网络" , 0).show();
                }else {
                    sign();
                }
                break;
            case R.id.mTvJifen:
                startActivity(new Intent(this , ScoreActivity.class));
                break;
            case R.id.mLLMineHome://我的主页
                MyHtmlActivity.actionStart(this , Urls.HTML5_MINE_HOME_URL , "我的主页");
                break;
            case R.id.mLLMineSub://我的订阅
                startActivity(new Intent(this , PaySubscribeActivity.class));
                break;
            case R.id.mLLMinePub://我的发布
                MyHtmlActivity.actionStart(this , Urls.HTML5_MINE_SUPPLYBUY_URL , "我的发布");
                break;
            case R.id.mLLMineTiezi://我的帖子
                MyHtmlActivity.actionStart(this , Urls.HTML5_MINE_BBS_URL , "我的帖子");
                break;
            case R.id.mLLMineLiuyan://我的留言
                MyHtmlActivity.actionStart(this , Urls.HTML5_MINE_REPLY_URL , "我的留言");
                break;
            case R.id.mLLMineFar://我的关注
                MyHtmlActivity.actionStart(this , Urls.HTML5_MINE_FOLLOW_URL , "我的关注");
                break;
            case R.id.mIvBack:
                finish();
                break;
        }
    }

    /**
     * 能否签到
     */
    private void ifSign() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_IFSIGN_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .build()
                .execute(new Callback<SignEntity>() {
                    @Override
                    public SignEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        SignEntity adEntity = JSON.parseObject(string, new TypeReference<SignEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(SignEntity o, int i) {
                        if(o != null){
                            mJifen.setText(o.getScore() + "");
                        }
                        if(o != null && o.getMessageId().equals("3")){
                            mSign.setText("已签到");
                            mSign.setEnabled(false);
                        }else{
                            mSign.setText("签到");
                            mSign.setEnabled(true);
                        }
                    }
                });
    }

    /**
     * 签到
     */
    private void sign() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_SIGN_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .build()
                .execute(new Callback<SignEntity>() {
                    @Override
                    public SignEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        SignEntity adEntity = JSON.parseObject(string, new TypeReference<SignEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(SignEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            mJifen.setText(o.getScore() + "");
                            mSign.setText("已签到");
                            mSign.setEnabled(false);
                            Toast.makeText(MineActivity.this , "签到成功" , 0).show();
                        }else{
                            mSign.setText("签到");
                            mSign.setEnabled(true);
                            Toast.makeText(MineActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    private Dialog dialog = null;
    /**
     * 登出
     */
    private void loginOut() {
        dialog = LoadingDialog.createLoadingDialog(this , "正在加载...");
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_LOGINOUT_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .addParams("loginId" , SpUtils.getString(this , "loginId" , ""))
                .build()
                .execute(new Callback<BaseEntity>() {
                    @Override
                    public BaseEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        BaseEntity adEntity = JSON.parseObject(string, new TypeReference<BaseEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(BaseEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            getAD();
                        } else if("-21".equals(o.getMessageId()) || "-20".equals(o.getMessageId())){
                            SpUtils.putString(MineActivity.this , "token" , "");
                            SpUtils.putString(MineActivity.this , "uid" , "");//uid清空
                            Toast.makeText(MineActivity.this , o.getMessageCont() + "" , 0).show();
                            AppManager.getInstance().finishAllActivity();
                        }else{
                            Toast.makeText(MineActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    private void getAD(){
        //  /api/ver.shtml
        OkHttpUtils.getInstance()
                .post()
                .tag("101")
                .url(Urls.POST_AD_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("TIMESAMP" , currentTime)
                .addHeader("SIGNATURE" , getSignture())//MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
                .addHeader("uId" , "")
                .addHeader("token" , "")
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
                        Toast.makeText(MineActivity.this , e.getMessage() + "" , 0).show();
                    }

                    @Override
                    public void onResponse(ADEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            LoadingDialog.closeDialog(dialog);
                            SpUtils.putString(MineActivity.this , "uid" , "");//uid清空
                            SpUtils.putString(MineActivity.this , "token" , o.getToken());
                            Toast.makeText(MineActivity.this , "退出成功" , 0).show();
                            MessageHelper.sendMessage(new RefreshUrlMessage());
                            finish();
                        }
                    }
                });
    }

    //MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
    private String getSignture(){
        String s = currentTime + Utils.md5(Utils.getDeviceUniqID(this)).toUpperCase();
        return Utils.md5(s).toUpperCase();
    }
}
