package kuangyibao.com.kuangyibao.home.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseFragment;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.eventMsg.GetTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.NewsTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.PriceTitleMessage;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CommonWebView;

/**
 * Created by apple on 18-3-8.
 */

public class PriceFragment extends BaseFragment {
    private CommonWebView webView;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_price;
    }

    @Override
    protected void setUpView() {
        MessageHelper.regist(this);
        webView = findViewById(R.id.mWV);

        findViewById(R.id.mIvBack).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.mTvTitle)).setText("价格");

        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(webView.canGoBack()){
                    webView.goBack();
                    if(!webView.canGoBack()){
                        findViewById(R.id.mIvBack).setVisibility(View.INVISIBLE);
                        ((TextView)findViewById(R.id.mTvTitle)).setText("价格");
                    }
                }
            }
        });
    }

    @Override
    protected void setUpData() {
        StringBuffer sb = new StringBuffer(Urls.HTML5_PRICE_URL);
        sb.append("?appId=").append(MD5Utls.encrypt(Utils.getDeviceUniqID(getContext())))
                .append("&token=").append(MD5Utls.encrypt(SpUtils.getString(getContext() , "token" , "")));
        if(!TextUtils.isEmpty(SpUtils.getString(getContext() , "uid" , ""))){
            sb.append("&uId=").append(MD5Utls.encrypt(SpUtils.getString(getContext() , "uid" , "")));
        }
        webView.loadUrl(sb.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PriceTitleMessage event){
        if(event.isShowBack()){
            findViewById(R.id.mIvBack).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.mIvBack).setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetTitleMessage event){
        switch (Integer.valueOf(event.getIndex())){
            case 1:
                if(!TextUtils.isEmpty(event.getTitle())){
                    ((TextView)findViewById(R.id.mTvTitle)).setText(event.getTitle());
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        MessageHelper.unRegist(this);
        super.onDestroy();
    }
}
