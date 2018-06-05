package kuangyibao.com.kuangyibao.pwd;


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
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 忘记密码_设置密码页面
 */
public class ForgetPwdSetActivity extends BaseActivity implements TextWatcher {
    private Button btnNext;
    private EditText pwd , rePwd;
    private String phone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_forgetpwd_set;
    }

    @Override
    protected void initView() {
        super.initView();
        btnNext = findViewById(R.id.btnNext);
        pwd = findViewById(R.id.mEtPwd);
        rePwd = findViewById(R.id.mEtRePwd);
        TextView view = (TextView) findViewById(R.id.mTvTitle);
        view.setText("忘记密码");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isConnected(ForgetPwdSetActivity.this)){
                    if(!pwd.getText().toString().equals(rePwd.getText().toString())){
                        Toast.makeText(ForgetPwdSetActivity.this , "两次密码不一致" , 0).show();
                        return;
                    }
                    resetPwd();
                }else{
                    Toast.makeText(ForgetPwdSetActivity.this , "网络异常，请检查网络" , 0).show();
                }
            }
        });
        pwd.addTextChangedListener(this);
        rePwd.addTextChangedListener(this);
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
        phone = getIntent().getStringExtra("phone");
    }

    /**
     * 获取用户信息
     */
    private void resetPwd() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_PASSSECOND_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("mp" , phone)
                .addParams("uPwd" , MD5Utls.encrypt(pwd.getText().toString()))
                .addParams("cUPwd" , MD5Utls.encrypt(rePwd.getText().toString()))
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
                            Toast.makeText(ForgetPwdSetActivity.this , "重置密码成功" , 0).show();
                        }else{
                            Toast.makeText(ForgetPwdSetActivity.this , "" + o.getMessageCont() , 0).show();
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
        if(!TextUtils.isEmpty(pwd.getText().toString()) && !TextUtils.isEmpty(rePwd.getText().toString())){
            btnNext.setBackgroundResource(R.mipmap.btn_blue);
            btnNext.setEnabled(true);
        }else{
            btnNext.setBackgroundResource(R.mipmap.btn_gray2);
            btnNext.setEnabled(false);
        }
    }
}
