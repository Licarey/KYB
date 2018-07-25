package kuangyibao.com.kuangyibao;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.base.BaseApplication;
import kuangyibao.com.kuangyibao.base.BaseFragment;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.eventMsg.GetTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.NewsTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.eventMsg.ShareMessage;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.share.ShareUtil;
import kuangyibao.com.kuangyibao.share.SinaShareActivity;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CommonWebView;
import kuangyibao.com.kuangyibao.view.SharePopupWindow;

/**
 * Created by apple on 18-3-8.
 */

public class SubH5DetailActivity extends BaseActivity {
    private CommonWebView webView;
    private String url;

    public static void actionStart(Context context , String url){
        Intent intent = new Intent(context , SubH5DetailActivity.class);
        intent.putExtra("url" , url);
        context.startActivity(intent);
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_h5_details;
    }

    @Override
    protected void initView() {
        super.initView();
        MessageHelper.regist(this);
        webView = findViewById(R.id.mWV);
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.mTvTitle)).setText("快讯");
    }

    @Override
    protected void initData() {
        super.initData();
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetTitleMessage event){
        ((TextView)findViewById(R.id.mTvTitle)).setText(event.getTitle());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshUrlMessage event){
        webView.reload();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final ShareMessage event){//分享
        SharePopupWindow.showWindow(this, findViewById(R.id.mLLRoot), new SharePopupWindow.IShareOption() {
            @Override
            public void shareWechat() {
                //Context context , String title, String description, final String thumbUrl, String httpUrl, final boolean isFriend
                ShareUtil.getInstance(SubH5DetailActivity.this).wxSharePage(SubH5DetailActivity.this , event.getTitle() , event.getContent() , event.getImageUrl() , event.getUrl() , false);
            }

            @Override
            public void shareFriend() {
                ShareUtil.getInstance(SubH5DetailActivity.this).wxSharePage(SubH5DetailActivity.this , event.getTitle() , event.getContent() , event.getImageUrl() , event.getUrl() , true);
            }

            @Override
            public void shareSina() {
                //Context context , final IWeiboShareAPI mWeiboShareAPI, String title, String description, String thumbUrl, String httpUrl
                Intent intent = new Intent(SubH5DetailActivity.this , SinaShareActivity.class);
                intent.putExtra("shareTitle" , event.getTitle());
                intent.putExtra("shareDesc" , event.getContent());
                intent.putExtra("shareImage" , event.getImageUrl());
                intent.putExtra("shareUrl" , event.getUrl());
                BaseApplication.getInstance().setCurrentClass(SubH5DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageHelper.unRegist(this);
    }
}
