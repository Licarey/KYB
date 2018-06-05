package kuangyibao.com.kuangyibao.pay;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kuangyibao.com.kuangyibao.GridSpacingItemDecoration;
import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.base.CommonAdapter;
import kuangyibao.com.kuangyibao.base.ViewHolder;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.entity.SubscribeEntity;
import kuangyibao.com.kuangyibao.entity.WxPayEntity;
import kuangyibao.com.kuangyibao.mine.AgreementActivity;
import kuangyibao.com.kuangyibao.mine.MineActivity;
import kuangyibao.com.kuangyibao.mine.MyHtmlActivity;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 支付订阅页面
 */
public class PaySubscribeActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView commitRv;
    private Map<Integer , Boolean> states;
    private TextView mTvInfo , mTvInfoDay , tv_jifen_num , tv_qian_num;
    private View mLLJifen , mBtnSelect , magreen;
    private CheckBox cb;
    private boolean isCheck = false;
    private String productPrice = "0";//选择产品的价格

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        super.initView();
        commitRv = findViewById(R.id.mRvPaytype);
        magreen = findViewById(R.id.magreen);
        tv_jifen_num = findViewById(R.id.tv_jifen_num);
        tv_qian_num = findViewById(R.id.tv_qian_num);
        cb = findViewById(R.id.cb);
        mBtnSelect = findViewById(R.id.mBtnSelect);
        mTvInfo = findViewById(R.id.mTvInfo);
        mTvInfoDay = findViewById(R.id.mTvInfoDay);
        mLLJifen = findViewById(R.id.mLLJifen);
        mTvInfoDay.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.mTvTitle);
        title.setText("我的订阅");
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        commitRv.setLayoutManager(layoutManager);
        commitRv.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(this , 10), true));
        commitRv.setHasFixedSize(true);
        mBtnSelect.setEnabled(false);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mBtnSelect.setBackgroundResource(R.mipmap.btn_blue);
                    mBtnSelect.setEnabled(true);
                }else{
                    mBtnSelect.setBackgroundResource(R.mipmap.btn_gray2);
                    mBtnSelect.setEnabled(false);
                }
            }
        });
        magreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCheck){
                    magreen.setBackgroundResource(R.drawable.radio_selected);
                    isCheck = true;
                }else{
                    magreen.setBackgroundResource(R.drawable.radio_normal);
                    isCheck = false;
                }
            }
        });
        findViewById(R.id.mTvDis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgreementActivity.actionStart(PaySubscribeActivity.this , Urls.HTML5_DISCLAIMER_URL , "免费声明");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        states = new HashMap<>();
        states.put(0 , true);

//        mySubs();
        showSubs();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.mTvForgetPwd:
//                break;
//        }
    }

    /**
     * 我的订阅信息
     */
    private void mySubs() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_MYSUBS_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
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
                        }else{
                            Toast.makeText(PaySubscribeActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    /**
     * 订阅列表
     */
    private boolean isInit = false;
    private void showSubs() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_SHOWSUBS_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .build()
                .execute(new Callback<SubscribeEntity>() {
                    @Override
                    public SubscribeEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        SubscribeEntity adEntity = JSON.parseObject(string, new TypeReference<SubscribeEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(final SubscribeEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            getScoreInfo(o.getScore());
                            commitRv.setAdapter(new CommonAdapter<SubscribeEntity.Value>(PaySubscribeActivity.this, R.layout.item_pay_list, o.getProductList()) {
                                @Override
                                public void convert(final ViewHolder holder, final SubscribeEntity.Value s , final int pos) {
                                    holder.setText(R.id.type, s.getProductName());
                                    holder.setText(R.id.info, s.getProductSave() + "");
                                    if(states.containsKey(pos)){
                                        holder.getView(R.id.vselect).setVisibility(View.VISIBLE);
                                    }else{
                                        holder.getView(R.id.vselect).setVisibility(View.INVISIBLE);
                                    }
                                    if(!isInit){
                                        if(o.getProductList().size() > 0){
                                            mTvInfo.setText(o.getProductList().get(0).getProductCont() + "");
                                            productPrice = o.getProductList().get(0).getProductPrice();
                                        }
                                        isInit = true;
                                    }
                                    holder.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(states.containsKey(pos)){
                                                states.remove(pos);
                                            }else{
                                                states.clear();
                                            }
                                            states.put(pos , true);
                                            mTvInfo.setText(s.getProductCont() + "");
                                            productPrice = s.getProductPrice();
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                        }else{
                            Toast.makeText(PaySubscribeActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    private void getScoreInfo(String scoreNum){
        if(Integer.parseInt(scoreNum) < 10){
            tv_jifen_num.setText("共" + scoreNum + "  可用0积分抵");
            tv_qian_num.setText("￥0");
        }else {
            int money = Integer.parseInt(scoreNum) / 10;
            tv_jifen_num.setText("共" + scoreNum + "  可用" + (money * 10) + "积分抵");
            tv_qian_num.setText("￥" + money);
        }
    }

    private void wxOrderCreate() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_WXPREPAY_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .addParams("pId" , "")//选择订阅信息接口的输出参数：productId
                .addParams("ifScore" , "")
                .build()
                .execute(new Callback<WxPayEntity>() {
                    @Override
                    public WxPayEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        WxPayEntity adEntity = JSON.parseObject(string, new TypeReference<WxPayEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(final WxPayEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            startActivity(new Intent(PaySubscribeActivity.this , PayTypeActivity.class));
                        }else{
                            Toast.makeText(PaySubscribeActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

}
