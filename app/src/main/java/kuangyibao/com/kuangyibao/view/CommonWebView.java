package kuangyibao.com.kuangyibao.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.ADEntity;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.eventMsg.GetTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.HomeTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.NewsTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.PriceTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.RefreshUrlMessage;
import kuangyibao.com.kuangyibao.eventMsg.ShareMessage;
import kuangyibao.com.kuangyibao.eventMsg.StoreTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.ZhishuTitleMessage;
import kuangyibao.com.kuangyibao.forums.ReleaseForumsActivity;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.login.LoginActivity;
import kuangyibao.com.kuangyibao.mine.MineActivity;
import kuangyibao.com.kuangyibao.pay.PaySubscribeActivity;
import kuangyibao.com.kuangyibao.pwd.ForgetPwdActivity;
import kuangyibao.com.kuangyibao.regist.RegistActivity;
import kuangyibao.com.kuangyibao.util.MD5Utls;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by apple on 18-4-21.
 */

public class CommonWebView extends WebView {
    private boolean isLoadUrl = false;
    private Context mContext;
    private String urls;

    public CommonWebView(Context context) {
        super(context);
        init(context);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        WebSettings webSettings = this.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        this.setWebViewClient(new MyWebViewClient());
        this.setWebChromeClient(new MyWebChromeClient());
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.addJavascriptInterface(new MyJavaScriptInterface(), "AndroidWebView");
    }

    public void refreshPage() {
        if (TextUtils.isEmpty(urls)) {
            return;
        }
        if (urls.contains("&uId=") && urls.contains("&token=")) {//替换uid
            int startIndex = urls.indexOf("&token=");
            int endIndex = urls.indexOf("&uId=");
            String newUrl = urls.substring(startIndex + 7, endIndex);
            String uidUrl = urls.substring(endIndex + 5);
            String s = urls.replace(newUrl, MD5Utls.encrypt(SpUtils.getString(getContext(), "token", "")));
            String news = s.replace(uidUrl, MD5Utls.encrypt(SpUtils.getString(getContext(), "uid", "")));
            loadUrl(news);
        }else if(urls.contains("&token=")){
            int startIndex = urls.indexOf("&token=");
            String newUrl = urls.substring(startIndex + 7);
            String s = urls.replace(newUrl, MD5Utls.encrypt(SpUtils.getString(getContext(), "token", "")));
            loadUrl(s);
        }else{
            loadUrl(urls);
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!isLoadUrl) {
                isLoadUrl = true;
                urls = url;
                view.loadUrl(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            refreshPage();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
                return true;
            }
            urls = url;
            if (url.contains("http:") || url.contains("https:")) {
                view.loadUrl(url);
                switch (HomeActivity.getCURRENTINDEX()) {
                    case 0:
                        MessageHelper.sendMessage(new HomeTitleMessage(true, ""));
                        break;
                    case 1:
                        MessageHelper.sendMessage(new PriceTitleMessage(true, ""));
                        break;
                    case 2:
                        MessageHelper.sendMessage(new NewsTitleMessage(true, ""));
                        break;
                    case 3:
                        MessageHelper.sendMessage(new ZhishuTitleMessage(true, ""));
                        break;
                    case 4:
                        MessageHelper.sendMessage(new StoreTitleMessage(true, ""));
                        break;
                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (view.canGoBack()){
                if(!TextUtils.isEmpty(title) && (!title.contains("无法") || !title.startsWith("http")))
                    MessageHelper.sendMessage(new GetTitleMessage(title, HomeActivity.getCURRENTINDEX() + ""));
            }
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 处理alert弹出框
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            return super.onConsoleMessage(cm);
        }

        /**
         * 处理confirm弹出框
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   JsResult result) {
            return super.onJsConfirm(view, url, message, result);

        }

        /**
         * 处理prompt弹出框
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, message, result);
        }
    }

    /**
     * 浏览器回调接口
     */
    final class MyJavaScriptInterface {
        MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void callLogin() {//需要登陆操作
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
        @JavascriptInterface
        public void callReg() {//需要注册操作
            mContext.startActivity(new Intent(mContext, RegistActivity.class));
        }

        @JavascriptInterface
        public void callMember() {//会员显示
            mContext.startActivity(new Intent(mContext, MineActivity.class));
        }

        @JavascriptInterface
        public void callPostBbs() {//发帖页
            mContext.startActivity(new Intent(mContext, ReleaseForumsActivity.class));
        }

        @JavascriptInterface
        public void callSubs() {//选择订阅
            mContext.startActivity(new Intent(mContext, PaySubscribeActivity.class));
        }

        @JavascriptInterface
        public void callShare(String url, String title, String content, String imageUrl) {//分享
            MessageHelper.sendMessage(new ShareMessage(url , title , content , imageUrl));
        }

        @JavascriptInterface
        public void callMySubs() {//我的订阅
            mContext.startActivity(new Intent(mContext, PaySubscribeActivity.class));
        }
        @JavascriptInterface
        public void callForgotPassword() {//修改密码
            mContext.startActivity(new Intent(mContext, ForgetPwdActivity.class));
        }
        @JavascriptInterface
        public void callLogout() {//登出
            loginOut();
        }
        @JavascriptInterface
        public void callVer() {//版本更新

        }
    }

    @Override
    public void reload() {
        refreshPage();
    }

    /**
     * 登出
     */
    private void loginOut() {
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_LOGINOUT_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(mContext))
                .addHeader("token" , SpUtils.getString(mContext , "token" , ""))
                .addHeader("uId" , Utils.getUID(mContext))
                .addParams("loginId" , SpUtils.getString(mContext , "loginId" , ""))
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
                            getAD();
                        }else{
                            Toast.makeText(mContext , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }

    private String currentTime = System.currentTimeMillis() + "";

    private void getAD(){
        //  /api/ver.shtml
        OkHttpUtils.getInstance()
                .post()
                .tag("101")
                .url(Urls.POST_AD_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(mContext))
                .addHeader("TIMESAMP" , currentTime)
                .addHeader("SIGNATURE" , getSignture())//MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
                .addHeader("uId" , "")
                .addHeader("token" , "")
                .addParams("ver", SpUtils.getString(mContext , "ver" , "1"))
                .addParams("advVer", SpUtils.getString(mContext , "adver" , "1524580242251"))
                .build()
                .execute(new Callback<ADEntity>() {
                    @Override
                    public ADEntity parseNetworkResponse(Response response, int i) throws Exception {
                        String string = response.body().string();
                        ADEntity adEntity = JSON.parseObject(string, new TypeReference<ADEntity>() {});
                        return adEntity;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {
                    }

                    @Override
                    public void onResponse(ADEntity o, int i) {
                        if(o != null && o.getMessageId().equals("1")){
                            SpUtils.putString(mContext , "token" , o.getToken());
                            Toast.makeText(mContext , "退出成功" , 0).show();
                            MessageHelper.sendMessage(new RefreshUrlMessage());
                        }
                    }
                });
    }

    //MD5(TIMESAMP+MD5(appId).toUpperCase()).toUpperCase()
    private String getSignture(){
        String s = currentTime + Utils.md5(Utils.getDeviceUniqID(mContext)).toUpperCase();
        return Utils.md5(s).toUpperCase();
    }
}
