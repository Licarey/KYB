package kuangyibao.com.kuangyibao.mine;


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
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.UpdateInfoEntity;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.PictureUtils;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改信息页面
 */
public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText mEtLoginName , mEtCompany;
    private Button nan , nv , regist;
    private TextView selectPic;
    private ImageView mHead;

    @Override
    public int getContentViewId() {
        return R.layout.activity_update_info;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView view = (TextView) findViewById(R.id.mTvTitle);
        view.setText("会员修改");
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEtLoginName = findViewById(R.id.mEtLoginName);
        mEtCompany = findViewById(R.id.mEtCompany);
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
    }

    @Override
    protected void initData() {
        super.initData();
        if(!Utils.isConnected(this)){
            Toast.makeText(this , "网络异常，请检查网络" , 0).show();
            return;
        }
        getUserInfo();
    }

    private String sex = "1";

    //  /api/regSecond.shtml
    private void getUserInfo(){
        OkHttpUtils
                .post()
                .url(Urls.POST_CLIENT_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId", Utils.getUID(this))
                .build()
                .execute(new Callback<UpdateInfoEntity>() {
                    @Override
                    public UpdateInfoEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        UpdateInfoEntity adEntity = JSON.parseObject(string, new TypeReference<UpdateInfoEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(UpdateInfoActivity.this , "获取信息失败" , 0).show();
                    }

                    @Override
                    public void onResponse(UpdateInfoEntity o, int i) {
                        if(o.getMessageId().equals("1")){//成功
                            mEtCompany.setText(o.getComName() + "");
                            mEtLoginName.setText(o.getuNName() + "");
                            if(o.getuSex().equals("1")){
                                nan.setBackgroundResource(R.drawable.radio_selected);
                                nv.setBackgroundResource(R.drawable.radio_normal);
                            }else{
                                nv.setBackgroundResource(R.drawable.radio_selected);
                                nan.setBackgroundResource(R.drawable.radio_normal);
                            }
                            if(!TextUtils.isEmpty(o.getImgUrl())){
                                ImageUtils.loadImageView(UpdateInfoActivity.this , Urls.BASEURL + o.getImgUrl() , mHead);
                            }
                        }else{
                            Toast.makeText(UpdateInfoActivity.this , o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nan:
                nan.setBackgroundResource(R.drawable.radio_selected);
                nv.setBackgroundResource(R.drawable.radio_normal);
                sex = "1";
                break;
            case R.id.nv:
                nv.setBackgroundResource(R.drawable.radio_selected);
                nan.setBackgroundResource(R.drawable.radio_normal);
                sex = "2";
                break;
            case R.id.mTvPic:
                PictureUtils.selectPic(UpdateInfoActivity.this);
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
                    updateInfo();
                }else{
                    Toast.makeText(this , "网络异常，请稍后重试" , 0).show();
                }
                break;
        }
    }

    private void updateInfo() {
        if(!TextUtils.isEmpty(fileName)){
            OkHttpUtils
                    .post()
                    .url(Urls.POST_UPDATECLIENT_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addHeader("uId", Utils.getUID(this))
                    .addParams("uSex", sex)
                    .addParams("uNName", mEtLoginName.getText().toString())
                    .addParams("comName", mEtCompany.getText().toString())
                    .addFile("imgFile" , fileName , new File(filePath))
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
                            Toast.makeText(UpdateInfoActivity.this , "获取信息失败" , 0).show();
                        }

                        @Override
                        public void onResponse(BaseEntity o, int i) {
                            if(o.getMessageId().equals("1")){//成功
                                Toast.makeText(UpdateInfoActivity.this , "修改成功" , 0).show();
                            }else{
                                Toast.makeText(UpdateInfoActivity.this , o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }else{
            OkHttpUtils
                    .post()
                    .url(Urls.POST_UPDATECLIENT_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addHeader("uId", Utils.getUID(this))
                    .addParams("uSex", sex)
                    .addParams("uNName", mEtLoginName.getText().toString())
                    .addParams("comName", mEtCompany.getText().toString())
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
                            Toast.makeText(UpdateInfoActivity.this , "获取信息失败" , 0).show();
                        }

                        @Override
                        public void onResponse(BaseEntity o, int i) {
                            if(o.getMessageId().equals("1")){//成功
                                Toast.makeText(UpdateInfoActivity.this , "修改成功" , 0).show();
                            }else{
                                Toast.makeText(UpdateInfoActivity.this , o.getMessageCont() , 0).show();
                            }
                        }
                    });
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
                        ImageUtils.loadImageFromSD(UpdateInfoActivity.this , filePath , mHead);
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
                && !TextUtils.isEmpty(mEtCompany.getText().toString())){
            regist.setBackgroundResource(R.mipmap.btn_blue);
        }else{
            regist.setBackgroundResource(R.mipmap.btn_gray2);
        }
    }
}
