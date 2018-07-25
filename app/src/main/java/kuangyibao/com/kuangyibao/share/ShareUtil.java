package kuangyibao.com.kuangyibao.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.pay.wxpay.Util;
import kuangyibao.com.kuangyibao.util.ImageUtils;

/**分享工具类
 * Created by liming on 17/2/16.
 */

public class ShareUtil {
    private IWXAPI api;
    private Activity mContexts;
    private static ShareUtil mInstance = null;

    public static ShareUtil getInstance(Activity mContext) {


        if (mInstance == null) {
            mInstance = new ShareUtil(mContext);
        }
        return mInstance;
    }

    public ShareUtil(Activity mcontext) {
        mContexts = mcontext;
        api = WXAPIFactory.createWXAPI(mcontext, Urls.WX_APPID);
    }

    /**
     *
     * @param title
     * @param description
     * @param thumbUrl
     * @param httpUrl
     * @param isFriend  false 微信好友  true 朋友圈
     */
    public void wxSharePage(Context context , String title, String description, final String thumbUrl, String httpUrl, final boolean isFriend) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "您还未安装微信客户端",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = httpUrl;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        msg.title = title;
        msg.description = description;
        if (TextUtils.isEmpty(thumbUrl)) {
            Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
            msg.setThumbImage(thumb);
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = isFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } else {
            ImageUtils.loadImageView(context, thumbUrl, new Target(){

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    if (bitmap != null) {
                        msg.thumbData = Util.bmpToByteArray(bitmap, false);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = isFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        api.sendReq(req);
                    } else {
                        Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
                        msg.thumbData = Util.bmpToByteArray(thumb, false);
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = isFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        api.sendReq(req);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
                    msg.thumbData = Util.bmpToByteArray(thumb, false);
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = isFriend ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                    api.sendReq(req);
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
    }

    public void sinaSharePage(Context context , final IWeiboShareAPI mWeiboShareAPI, String title, String description, String thumbUrl, String httpUrl) {
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            Toast.makeText(mContexts, "您未安装该程序" , 0).show();
            return;
        }
        final WeiboMultiMessage weiboMultiMessage1 = new WeiboMultiMessage();
        final WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
        mediaObject.actionUrl = httpUrl;
        if (TextUtils.isEmpty(thumbUrl)) {
            Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
            mediaObject.setThumbImage(thumb);
            weiboMultiMessage1.mediaObject = mediaObject;
            SendMultiMessageToWeiboRequest request2 = new SendMultiMessageToWeiboRequest();
            request2.transaction = String.valueOf(System.currentTimeMillis());
            request2.multiMessage = weiboMultiMessage1;
            mWeiboShareAPI.sendRequest(mContexts, request2);
        } else {
            ImageUtils.loadImageView(context, thumbUrl, new Target(){

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    if (bitmap != null) {
                        mediaObject.setThumbImage(bitmap);
                        weiboMultiMessage1.mediaObject = mediaObject;
                        SendMultiMessageToWeiboRequest request2 = new SendMultiMessageToWeiboRequest();
                        request2.transaction = String.valueOf(System.currentTimeMillis());
                        request2.multiMessage = weiboMultiMessage1;
                        mWeiboShareAPI.sendRequest(mContexts, request2);
                    } else {
                        Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
                        mediaObject.setThumbImage(thumb);
                        weiboMultiMessage1.mediaObject = mediaObject;
                        SendMultiMessageToWeiboRequest request2 = new SendMultiMessageToWeiboRequest();
                        request2.transaction = String.valueOf(System.currentTimeMillis());
                        request2.multiMessage = weiboMultiMessage1;
                        mWeiboShareAPI.sendRequest(mContexts, request2);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Bitmap thumb = BitmapFactory.decodeResource(mContexts.getResources(), R.mipmap.logo);
                    mediaObject.setThumbImage(thumb);
                    weiboMultiMessage1.mediaObject = mediaObject;
                    SendMultiMessageToWeiboRequest request2 = new SendMultiMessageToWeiboRequest();
                    request2.transaction = String.valueOf(System.currentTimeMillis());
                    request2.multiMessage = weiboMultiMessage1;
                    mWeiboShareAPI.sendRequest(mContexts, request2);
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
