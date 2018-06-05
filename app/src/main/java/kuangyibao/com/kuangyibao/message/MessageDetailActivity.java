package kuangyibao.com.kuangyibao.message;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;


import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.MessageDetailEntity;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.TimeUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的消息页面
 */
public class MessageDetailActivity extends BaseActivity {
    private TextView mTvTime , tv_message_title , tv_message_content;
    private String noticeId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView title = findViewById(R.id.mTvTitle);
        mTvTime = findViewById(R.id.mTvTime);
        tv_message_title = findViewById(R.id.tv_message_title);
        tv_message_content = findViewById(R.id.mTvMessageContent);
        title.setText("公告详情");
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
        noticeId = getIntent().getStringExtra("noticeId");
        if(!Utils.isConnected(this)){
            Toast.makeText(this , "网络异常，请检查网络" , 0).show();
        }else{
            OkHttpUtils.getInstance()
                    .post()
                    .url(Urls.POST_NOTICEDETAIL_URL)
                    .addHeader("appId" , Utils.getDeviceUniqID(this))
                    .addHeader("token" , SpUtils.getString(this , "token" , ""))
                    .addHeader("uId" , Utils.getUID(this))
                    .addParams("noticeId" , noticeId)
                    .build()
                    .execute(new Callback<MessageDetailEntity>() {
                        @Override
                        public MessageDetailEntity parseNetworkResponse(Response response, int i) throws Exception {
                            String string = response.body().string();
                            MessageDetailEntity adEntity = JSON.parseObject(string, new TypeReference<MessageDetailEntity>() {});
                            return adEntity;
                        }

                        @Override
                        public void onError(Call call, Exception e, int i) {

                        }

                        @Override
                        public void onResponse(MessageDetailEntity o, int i) {
                            if(o != null && o.getMessageId().equals("1")){
                                mTvTime.setText(TimeUtils.getDateToString(Long.valueOf(o.getNoticeTime()) , "MM-dd hh:mm"));
                                tv_message_title.setText(o.getNoticeTitle() + "");
                                tv_message_content.setText(o.getNoticeCont() + "");

                            }else{
                                Toast.makeText(MessageDetailActivity.this , "" + o.getMessageCont() , 0).show();
                            }
                        }
                    });
        }
    }
}
