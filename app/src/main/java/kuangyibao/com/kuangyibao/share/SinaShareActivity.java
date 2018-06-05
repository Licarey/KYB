package kuangyibao.com.kuangyibao.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseApplication;
import kuangyibao.com.kuangyibao.config.Urls;


/**
 * 微博统一分享页面
 * Created by mapollo on 16/10/18.
 */

public class SinaShareActivity extends Activity implements IWeiboHandler.Response {
    private IWeiboShareAPI mWeiboShareAPI = null;
    private boolean isClose = false;
    private boolean isJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Urls.SINA_APPKEY);
        mWeiboShareAPI.registerApp();
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            Toast.makeText(this, "您未安装程序" , 0).show();
            finish();
            return;
        }
        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        initData();
    }

    public void initData() {
        if (getIntent().hasExtra("shareTitle")) {
            String shareTitle = getIntent().getStringExtra("shareTitle");
            String shareDesc = getIntent().getStringExtra("shareDesc");
            String shareImage = getIntent().getStringExtra("shareImage");
            String shareUrl = getIntent().getStringExtra("shareUrl");
            ShareUtil.getInstance(this).sinaSharePage(this , mWeiboShareAPI, shareTitle, shareDesc, shareImage, shareUrl);
        }
        isClose = true;
        isJump = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {

        if (baseResp != null) {
            isClose = false;
            isJump = true;
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "分享成功" , 0).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "分享取消" , 0).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "分享失败" , 0).show();
                    break;
            }
            mHandlers.sendMessageDelayed(mHandlers.obtainMessage(), 500);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isClose && !isJump) finish();
    }

    private Handler mHandlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(SinaShareActivity.this, BaseApplication.getInstance().getCurrentClass()));
            SinaShareActivity.this.finish();
        }
    };
}
