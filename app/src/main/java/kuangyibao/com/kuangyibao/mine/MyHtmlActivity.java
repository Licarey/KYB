package kuangyibao.com.kuangyibao.mine;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.eventMsg.NewsTitleMessage;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CommonWebView;

/**
 * Created by apple on 18-5-3.
 */

public class MyHtmlActivity extends BaseActivity {
    private CommonWebView webView;
    private String url , title;

    public static void actionStart(Context context , String url , String title){
        Intent intent = new Intent(context , MyHtmlActivity.class);
        intent.putExtra("title" , title);
        intent.putExtra("url" , url);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_html;
    }

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.mWV);
        findViewById(R.id.mIvBack).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.mTvTitle)).setText(title);
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?appId=").append(MD5Utls.encrypt(Utils.getDeviceUniqID(this)))
                .append("&token=").append(MD5Utls.encrypt(SpUtils.getString(this , "token" , "")));
        if(!TextUtils.isEmpty(SpUtils.getString(this , "uid" , ""))){
            sb.append("&uId=").append(MD5Utls.encrypt(SpUtils.getString(this , "uid" , "")));
        }
        webView.loadUrl(sb.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
