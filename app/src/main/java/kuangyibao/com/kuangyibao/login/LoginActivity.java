package kuangyibao.com.kuangyibao.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.AppManager;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.LoginEntity;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.pwd.ForgetPwdActivity;
import kuangyibao.com.kuangyibao.regist.RegistActivity;
import kuangyibao.com.kuangyibao.regist.RegistInfoActivity;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText mEtLoginPhone , mEtLoginPwd;
    private Button btnLogin , btnRegist;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView title = (TextView) findViewById(R.id.mTvTitle);
        title.setText("登录");
        TextView pwd = (TextView) findViewById(R.id.mTvForgetPwd);
        mEtLoginPhone = (EditText) findViewById(R.id.mEtLoginPhone);
        mEtLoginPwd = (EditText) findViewById(R.id.mEtLoginPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegist = findViewById(R.id.btnRegist);
        pwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        mEtLoginPhone.addTextChangedListener(this);
        mEtLoginPwd.addTextChangedListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mTvForgetPwd:
                startActivity(new Intent(this , ForgetPwdActivity.class));
                break;
            case R.id.btnLogin://登陆
                if(!Utils.isConnected(this)){
                    Toast.makeText(this , "网络异常，请检查网络" , 0).show();
                }else{
                    if(TextUtils.isEmpty(mEtLoginPhone.getText().toString()) && !Utils.isMobileNO(mEtLoginPhone.getText().toString())){
                        Toast.makeText(this , "请输入正确的手机号" , 0).show();
                        return;
                    }

                    if(TextUtils.isEmpty(mEtLoginPwd.getText().toString())){
                        Toast.makeText(this , "请输入密码" , 0).show();
                        return;
                    }
                    loginUser();
                }
                break;
            case R.id.btnRegist://注册
                startActivity(new Intent(this , RegistActivity.class));
                break;
        }
    }

    /**
     * 登陆
     */
    private void loginUser() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_APILOGIN_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("acctName" , mEtLoginPhone.getText().toString())
                .addParams("acctPwd" , MD5Utls.encrypt(mEtLoginPwd.getText().toString()))
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
                            Toast.makeText(LoginActivity.this , "登录成功" , 0).show();
                            SpUtils.putString(LoginActivity.this , "token" , o.getToken());
                            SpUtils.putString(LoginActivity.this , "uid" , o.getuId());
                            SpUtils.putString(LoginActivity.this , "loginType" , o.getLoginType());
                            SpUtils.putString(LoginActivity.this , "loginId" , o.getLoginId());
                            AppManager.getInstance().finishActivity(RegistInfoActivity.class);
                            AppManager.getInstance().finishActivity(RegistActivity.class);
                            AppManager.getInstance().finishActivity(UnLoginActivity.class);
                            MessageHelper.sendMessage(new RefreshUrlMessage());//刷新页面
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!TextUtils.isEmpty(mEtLoginPhone.getText().toString()) && !TextUtils.isEmpty(mEtLoginPwd.getText().toString())){
            btnLogin.setBackgroundResource(R.mipmap.btn_blue);
        }else{
            btnLogin.setBackgroundResource(R.mipmap.btn_gray2);
        }
    }
}
