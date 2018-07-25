package kuangyibao.com.kuangyibao.home.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseFragment;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.LoginEntity;
import kuangyibao.com.kuangyibao.eventMsg.GetTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.HomeTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.NewsTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.PriceTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.eventMsg.StoreTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.ZhishuTitleMessage;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.login.LoginActivity;
import kuangyibao.com.kuangyibao.login.UnLoginActivity;
import kuangyibao.com.kuangyibao.message.MessageActivity;
import kuangyibao.com.kuangyibao.mine.MineActivity;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CommonWebView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by apple on 18-3-8.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private CommonWebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setUpView() {
        MessageHelper.regist(this);
        webView = findViewById(R.id.mWV);
        findViewById(R.id.mIvMessage).setOnClickListener(this);
        findViewById(R.id.mIvMine).setOnClickListener(this);
    }

    @Override
    protected void setUpData() {
        StringBuffer sb = new StringBuffer(Urls.HTML5_INDEX_URL);
        sb.append("?appId=").append(MD5Utls.encrypt(Utils.getDeviceUniqID(getContext())))
                .append("&token=").append(MD5Utls.encrypt(SpUtils.getString(getContext() , "token" , "")));
        if(!TextUtils.isEmpty(SpUtils.getString(getContext() , "uid" , ""))){
            sb.append("&uId=").append(MD5Utls.encrypt(SpUtils.getString(getContext() , "uid" , "")));
        }
        webView.loadUrl(sb.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mIvMessage:
                startActivity(new Intent(getContext() , MessageActivity.class));
                break;
            case R.id.mIvMine:
                if(!Utils.isConnected(getContext())){
                    Toast.makeText(getContext() , "网络异常，请检查网络" , 0).show();
                }else{
                    ifLogin();
                }
                break;
        }
    }

    /**
     * 判断登录状态
     */
    private void ifLogin() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_IFLOGIN_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(getContext()))
                .addHeader("token" , SpUtils.getString(getContext() , "token" , ""))
                .addHeader("uId" , Utils.getUID(getContext()))
                .build()
                .execute(new Callback<LoginEntity>() {
                    @Override
                    public LoginEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        LoginEntity adEntity = JSON.parseObject(string, new TypeReference<LoginEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(LoginEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            startActivity(new Intent(getContext() , MineActivity.class));
                            if(!TextUtils.isEmpty(o.getLoginType())){//登录过
                                SpUtils.putString(getContext() , "loginType" , o.getLoginType());
                            }

                        }else{
                            startActivity(new Intent(getContext() , UnLoginActivity.class));
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshUrlMessage event){
        webView.reload();
    }

    @Override
    public void onDestroy() {
        MessageHelper.unRegist(this);
        super.onDestroy();
    }
}
