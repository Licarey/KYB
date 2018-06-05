package kuangyibao.com.kuangyibao.forums;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.base.CommonAdapter;
import kuangyibao.com.kuangyibao.base.ViewHolder;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.PictureUtils;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**发布论坛
 * Created by apple on 18-3-17.
 */

public class ReleaseForumsActivity extends BaseActivity implements TextWatcher {
    private RecyclerView commitRv;
    private PicAdapter adapter;
    private List<LocalMedia> mDatas;
    private EditText mEtContent , mEtTitle;
    private TextView mTvCount;
    private Button publish;

    @Override
    public int getContentViewId() {
        return R.layout.activity_release_forums;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView title = (TextView) findViewById(R.id.mTvTitle);
        title.setText("发帖");
        mTvCount = (TextView) findViewById(R.id.mTvCount);
        mEtContent = (EditText) findViewById(R.id.mEtContent);
        mEtTitle = (EditText) findViewById(R.id.mEtTitle);
        publish =  findViewById(R.id.btnConfirm);
        commitRv = (RecyclerView) findViewById(R.id.mRvPic);
        commitRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        mEtContent.addTextChangedListener(this);
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isConnected(ReleaseForumsActivity.this)){
                    publish();
                }else{
                    Toast.makeText(ReleaseForumsActivity.this , "网络异常，请检查网络" , 0).show();
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mDatas = new ArrayList<>();
        mDatas.add(0 , new LocalMedia("add" , 0 , 0 , ""));
        adapter = new PicAdapter(this, R.layout.item_pic_list, mDatas);
        commitRv.setAdapter(adapter);
    }

    /**
     * 发帖
     */
    private void publish() {
        if(mDatas.size() > 1){//选择了图片
            OkHttpUtils.getInstance()
                    .post()
                    .url(Urls.POST_BBS_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addHeader("uId" , Utils.getUID(this))
                    .addParams("bbsTitle" , mEtTitle.getText().toString())
                    .addParams("bbsCont" , mEtContent.getText().toString())
                    .files("imgFiles" , getFileArray())
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
                                MessageHelper.sendMessage(new RefreshUrlMessage());
                                Toast.makeText(ReleaseForumsActivity.this , "发布成功" , 0).show();
                                finish();
                            }else{
                                Toast.makeText(ReleaseForumsActivity.this , "" + o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }else{
            OkHttpUtils.getInstance()
                    .post()
                    .url(Urls.POST_BBS_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addHeader("uId" , Utils.getUID(this))
                    .addParams("bbsTitle" , mEtTitle.getText().toString())
                    .addParams("bbsCont" , mEtContent.getText().toString())
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
                                MessageHelper.sendMessage(new RefreshUrlMessage());
                                Toast.makeText(ReleaseForumsActivity.this , "发布成功" , 0).show();
                                finish();
                            }else{
                                Toast.makeText(ReleaseForumsActivity.this , "" + o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }

    }

    private Map<String,File> getFileArray() {
        Map<String,File> files = new HashMap<>();
        for (int i = 1 ; i < mDatas.size() ; i ++){
            files.put(mDatas.get(i).getCompressPath().substring(mDatas.get(i).getCompressPath().lastIndexOf("/") + 1) , new File(mDatas.get(i).getCompressPath()));
        }
        return files;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.addList(selectList);
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
        mTvCount.setText(String.format("%s/500" , mEtContent.getText().toString().length()));
        if(!TextUtils.isEmpty(mEtTitle.getText().toString()) && !TextUtils.isEmpty(mEtContent.getText().toString())){
            publish.setBackgroundResource(R.drawable.btn_yellow1);
        }else{
            publish.setBackgroundResource(R.mipmap.btn_gray2);
        }
    }

    class PicAdapter extends CommonAdapter<LocalMedia>{


        public PicAdapter(Context context, int layoutId, List<LocalMedia> datas) {
            super(context, layoutId, datas);
        }

        public void setList(List<LocalMedia> pics){
            mDatas = pics;
            notifyDataSetChanged();
        }

        public void addList(List<LocalMedia> pics){
            mDatas.clear();
            mDatas.add(0 , new LocalMedia("add" , 0 , 0 , ""));
            mDatas.addAll(pics);
            notifyDataSetChanged();
        }

        @Override
        public void convert(ViewHolder holder, LocalMedia s , int pos) {
            if(!TextUtils.isEmpty(s.getPath()) && s.getPath().equals("add")){//加号
                ((ImageView)holder.getView(R.id.pic)).setImageResource(R.drawable.img_add_pic);
                holder.getView(R.id.pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureUtils.selectPic(ReleaseForumsActivity.this);
                    }
                });
            }else{
                ImageUtils.loadImageFromSD(ReleaseForumsActivity.this , s.getCompressPath() , (ImageView)holder.getView(R.id.pic));
            }
        }
    }
}
