package kuangyibao.com.kuangyibao.pwd;


import android.content.Intent;
import android.os.CountDownTimer;
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
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.CodeEntity;
import kuangyibao.com.kuangyibao.regist.RegistActivity;
import kuangyibao.com.kuangyibao.regist.RegistInfoActivity;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 忘记密码页面
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText mEtLoginPhone , mEtCode;
    private Button mLogin , mCode;
    private CountTimer countTimer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView view = (TextView) findViewById(R.id.mTvTitle);
        view.setText("忘记密码");
        countTimer = new CountTimer(180 * 1000 , 1000);
        mEtLoginPhone = (EditText) findViewById(R.id.mEtLoginPhone);
        mEtCode = (EditText) findViewById(R.id.mEtLoginPwd);
        mCode = (Button) findViewById(R.id.mBtnCode);
        mLogin = (Button) findViewById(R.id.btnNext);
        mCode.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mEtLoginPhone.addTextChangedListener(this);
        mCode.addTextChangedListener(this);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mBtnCode://获取验证码
                if(Utils.isConnected(this)){
                    if(TextUtils.isEmpty(mEtLoginPhone.getText().toString()) && !Utils.isMobileNO(mEtLoginPhone.getText().toString())){
                        Toast.makeText(this , "请输入正确的手机号" , 0).show();
                        return;
                    }
                    getVertifyCode();
                }else{
                    Toast.makeText(this , "网络异常，请稍后重试" , 0).show();
                }
                break;
            case R.id.btnNext:
                if(Utils.isConnected(this)){
                    if(TextUtils.isEmpty(mEtLoginPhone.getText().toString()) && !Utils.isMobileNO(mEtLoginPhone.getText().toString())){
                        Toast.makeText(this , "请输入正确的手机号" , 0).show();
                        return;
                    }

                    if(TextUtils.isEmpty(mEtCode.getText().toString())){
                        Toast.makeText(this , "请输入验证码" , 0).show();
                        return;
                    }
                    reset();
                }else{
                    Toast.makeText(this , "网络异常，请稍后重试" , 0).show();
                }
                break;
        }
    }

    private String messId = "";
    //    /api/sendVaild.shtml
    private void getVertifyCode() {
        OkHttpUtils
                .post()
                .url(Urls.POST_SENDVAILDPASS_URL)
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("appId", Utils.getDeviceUniqID(this))
                .addParams("mp", mEtLoginPhone.getText().toString())
                .build()
                .execute(new Callback<CodeEntity>() {
                    @Override
                    public CodeEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        CodeEntity adEntity = JSON.parseObject(string, new TypeReference<CodeEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(ForgetPwdActivity.this , "获取验证码失败" , 0).show();
                    }

                    @Override
                    public void onResponse(CodeEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            Toast.makeText(ForgetPwdActivity.this , "验证码发送成功" , 0).show();
                            messId = o.getSmsId();
                            countTimer.start();
                        }else{
                            Toast.makeText(ForgetPwdActivity.this , o.getMessageCont() + "" , 0).show();
                        }
                    }
                });
    }

    // /api/regFirst.shtml
    private void reset() {
        OkHttpUtils
                .post()
                .url(Urls.POST_PASSFIRST_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("vaild", mEtCode.getText().toString())
                .addParams("aSmsId", messId)
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
                        if(o.getMessageId().equals("1")){//成功
                            Intent intent = new Intent(ForgetPwdActivity.this , ForgetPwdSetActivity.class);
                            intent.putExtra("phone" , mEtLoginPhone.getText().toString());
                            startActivity(intent);
                        }else{
                            Toast.makeText(ForgetPwdActivity.this , o.getMessageCont() , 0).show();
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
        if(!TextUtils.isEmpty(mEtLoginPhone.getText().toString())
                && !TextUtils.isEmpty(mCode.getText().toString())){
            mLogin.setBackgroundResource(R.mipmap.btn_blue);
        }else{
            mLogin.setBackgroundResource(R.mipmap.btn_gray2);
        }
    }

    class CountTimer extends CountDownTimer {

        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mCode.setText(millisUntilFinished / 1000 + "(s)");
            mCode.setEnabled(false);
        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            mCode.setText("重新获取");
            mCode.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        if(countTimer != null){
            countTimer.cancel();
        }
        super.onDestroy();
    }
}
