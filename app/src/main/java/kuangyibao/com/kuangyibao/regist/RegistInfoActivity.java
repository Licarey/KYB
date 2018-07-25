package kuangyibao.com.kuangyibao.regist;


import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.List;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.AppManager;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.RegistEntity;
import kuangyibao.com.kuangyibao.login.LoginActivity;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.PictureUtils;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 注册信息页面
 */
public class RegistInfoActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText mEtLoginName , mEtCompany , mEtPwd , mEtRePwd;
    private Button nan , nv , regist;
    private TextView selectPic;
    private ImageView mHead;
    private String phone;//电话号

    @Override
    public int getContentViewId() {
        return R.layout.activity_regist_info;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView view = (TextView) findViewById(R.id.mTvTitle);
        view.setText("注册");

        mEtLoginName = findViewById(R.id.mEtLoginName);
        mEtCompany = findViewById(R.id.mEtCompany);
        mEtPwd = findViewById(R.id.mEtPwd);
        mEtRePwd = findViewById(R.id.mEtRePwd);
        regist = findViewById(R.id.btnConfirm);
        mHead = findViewById(R.id.ivPic);
        selectPic = findViewById(R.id.mTvPic);
        nan = findViewById(R.id.nan);
        nv = findViewById(R.id.nv);
        nan.setOnClickListener(this);
        nv.setOnClickListener(this);
        selectPic.setOnClickListener(this);
        regist.setOnClickListener(this);
        mEtLoginName.addTextChangedListener(this);
        mEtCompany.addTextChangedListener(this);
        mEtPwd.addTextChangedListener(this);
        mEtRePwd.addTextChangedListener(this);
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

    private String sex = "1";

    //  /api/regSecond.shtml
    private void RegistUser(){
        if(TextUtils.isEmpty(fileName)){
            OkHttpUtils
                    .post()
                    .url(Urls.POST_REGSECOND_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addParams("uSex", sex)
                    .addParams("uNName", mEtLoginName.getText().toString())
                    .addParams("comName", mEtCompany.getText().toString())
                    .addParams("uPwd", MD5Utls.encrypt(mEtPwd.getText().toString()))
                    .addParams("mp", phone)
                    .build()
                    .execute(new Callback<RegistEntity>() {
                        @Override
                        public RegistEntity parseNetworkResponse(Response response, int i) throws Exception {
                            String string = response.body().string();
                            RegistEntity adEntity = JSON.parseObject(string, new TypeReference<RegistEntity>() {});
                            return adEntity;
                        }

                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Toast.makeText(RegistInfoActivity.this , "注册失败" , 0).show();
                        }

                        @Override
                        public void onResponse(RegistEntity o, int i) {
                            if(o.getMessageId().equals("1")){//成功
                                SpUtils.putString(RegistInfoActivity.this , "uid" , o.getuId());
                                Toast.makeText(RegistInfoActivity.this , "注册成功" , 0).show();
                                Intent intent = new Intent(RegistInfoActivity.this , LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(RegistInfoActivity.this , o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }else{
            OkHttpUtils
                    .post()
                    .url(Urls.POST_REGSECOND_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addParams("uSex", sex)
                    .addParams("uNName", mEtLoginName.getText().toString())
                    .addParams("comName", mEtCompany.getText().toString())
                    .addParams("uPwd", MD5Utls.encrypt(mEtPwd.getText().toString()))
                    .addParams("mp", phone)
                    .addFile("imgFile" , fileName , new File(filePath))
                    .build()
                    .execute(new Callback<RegistEntity>() {
                        @Override
                        public RegistEntity parseNetworkResponse(Response response, int i) throws Exception {
                            String string = response.body().string();
                            RegistEntity adEntity = JSON.parseObject(string, new TypeReference<RegistEntity>() {});
                            return adEntity;
                        }

                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Toast.makeText(RegistInfoActivity.this , "注册失败" , 0).show();
                        }

                        @Override
                        public void onResponse(RegistEntity o, int i) {
                            if(o.getMessageId().equals("1")){//成功
                                SpUtils.putString(RegistInfoActivity.this , "uid" , o.getuId());
                                Toast.makeText(RegistInfoActivity.this , "注册成功" , 0).show();
                                AppManager.getInstance().finishActivity(RegistInfoActivity.class);
                                AppManager.getInstance().finishActivity(RegistActivity.class);
                                AppManager.getInstance().startActivity(RegistInfoActivity.this , LoginActivity.class);
                            }else{
                                Toast.makeText(RegistInfoActivity.this , o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nan:
                nan.setEnabled(false);
                nv.setEnabled(true);
                sex = "1";
                break;
            case R.id.nv:
                nan.setEnabled(true);
                nv.setEnabled(false);
                sex = "2";
                break;
            case R.id.mTvPic:
                PictureUtils.selectPic(RegistInfoActivity.this);
                break;
            case R.id.btnConfirm:
                if(Utils.isConnected(this)){
                    if(TextUtils.isEmpty(mEtLoginName.getText().toString())){
                        Toast.makeText(this , "请输入昵称" , 0).show();
                        return;
                    }

                    if(TextUtils.isEmpty(mEtCompany.getText().toString())){
                        Toast.makeText(this , "请输入公司名称" , 0).show();
                        return;
                    }
                    if(TextUtils.isEmpty(mEtPwd.getText().toString()) && mEtPwd.getText().toString().length() < 8){
                        Toast.makeText(this , "请输入正确格式的密码" , 0).show();
                        return;
                    }
                    if(!mEtPwd.getText().toString().equals(mEtRePwd.getText().toString())){
                        Toast.makeText(this , "两次密码不一致" , 0).show();
                        return;
                    }
                    RegistUser();
                }else{
                    Toast.makeText(this , "网络异常，请稍后重试" , 0).show();
                }
                break;
        }
    }

    private String fileName , filePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList != null && selectList.size() > 0){
                        LocalMedia localMedia = selectList.get(0);
                        filePath = localMedia.getCompressPath();
                        ImageUtils.loadImageFromSD(RegistInfoActivity.this , filePath , mHead);
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    }
                    break;
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!TextUtils.isEmpty(mEtLoginName.getText().toString())
                && !TextUtils.isEmpty(mEtCompany.getText().toString())
                && !TextUtils.isEmpty(mEtPwd.getText().toString())
                && !TextUtils.isEmpty(mEtRePwd.getText().toString())){
            regist.setBackgroundResource(R.mipmap.btn_blue);
        }else{
            regist.setBackgroundResource(R.mipmap.btn_gray2);
        }
    }
}
