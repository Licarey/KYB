package kuangyibao.com.kuangyibao.message;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.base.CommonAdapter;
import kuangyibao.com.kuangyibao.base.ViewHolder;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.MessageEntity;
import kuangyibao.com.kuangyibao.entity.MessageListEntity;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.TimeUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的消息页面
 */
public class MessageActivity extends BaseActivity {

    private RecyclerView commitRv;
    private String noticeId = "";

    @Override
    public int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView title = findViewById(R.id.mTvTitle);
        title.setText("公告");
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commitRv = (RecyclerView) findViewById(R.id.mRvMessage);
        commitRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        super.initData();

        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_NOTICELIST_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addHeader("uId" , Utils.getUID(this))
                .addParams("noticeId" , noticeId)//列表最后一条noticeId
                .build()
                .execute(new Callback<MessageListEntity>() {
                    @Override
                    public MessageListEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        MessageListEntity adEntity = JSON.parseObject(string, new TypeReference<MessageListEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(MessageListEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            if(o.getNoticeList() != null && o.getNoticeList().size() > 0){
                                noticeId = o.getNoticeList().get(o.getNoticeList().size() - 1).getNoticeId();
                            }
                            commitRv.setAdapter(new CommonAdapter<MessageEntity>(MessageActivity.this, R.layout.item_message_list, o.getNoticeList()) {
                                @Override
                                public void convert(ViewHolder holder, final MessageEntity s , int pos) {
                                    holder.setText(R.id.mTvMessageTitle, s.getNoticeTitle() + '"');
                                    holder.setText(R.id.tv_message_content, "[最新消息]" + s.getNoticeCont());
                                    holder.setText(R.id.mTvMessageTime, TimeUtils.getDateToString(Long.valueOf(s.getNoticeTime()) , "MM-dd hh:mm"));
                                    holder.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MessageActivity.this , MessageDetailActivity.class);
                                            intent.putExtra("noticeId" , s.getNoticeId() + "");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }else{
                            Toast.makeText(MessageActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

}
