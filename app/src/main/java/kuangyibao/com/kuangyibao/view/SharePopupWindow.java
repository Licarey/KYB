package kuangyibao.com.kuangyibao.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import kuangyibao.com.kuangyibao.R;


/**
 * 分享
 * Created by liming on 18/5/20.
 */

public class SharePopupWindow implements View.OnClickListener {

    private Activity context;
    private View mContentView;
    private PopupWindow popupWindow;
    private View Webchat,CircleFriends,MicroBlog , cancel;
    private static SharePopupWindow instance;

    public static SharePopupWindow getInstance() {
        if (instance == null) {
            instance = new SharePopupWindow();
        }
        return instance;
    }

    private SharePopupWindow() {

    }

    public static void showWindow(Activity context, View rootView , IShareOption mIShareOptions) {
        instance = getInstance();
        if (instance.popupWindow != null && instance.popupWindow.isShowing()) {
            instance.popupWindow.dismiss();
        }
        mIShareOption = mIShareOptions;
        instance.initView(context);
        if (context != null && !context.isFinishing() && rootView != null) {
            instance.show(rootView);
        }
    }

    private void show(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void initView(final Activity context) {
        this.context = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        Webchat = mContentView.findViewById(R.id.share_webchat);
        CircleFriends = mContentView.findViewById(R.id.share_circlefriends);
        MicroBlog = mContentView.findViewById(R.id.share_microblog);
        cancel = mContentView.findViewById(R.id.cancel);

        popupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        //setBackgroundDrawable加上点击外面消失  不加不消失
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setAnimationStyle(R.style.UnderPopupWindow);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,400);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1.0f);
            }
        });
        Webchat.setOnClickListener(this);
        CircleFriends.setOnClickListener(this);
        MicroBlog.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(mIShareOption == null) return;
        switch (v.getId()) {
            case R.id.share_webchat:
                mIShareOption.shareWechat();
                break;
            case R.id.share_circlefriends:
                mIShareOption.shareFriend();
                break;
            case R.id.share_microblog:
                mIShareOption.shareSina();
                break;
            case R.id.cancel:
                break;
        }
        dismiss();
    }

    private static IShareOption mIShareOption;

    public interface IShareOption{
        void shareWechat();
        void shareFriend();
        void shareSina();
    }

    // 隐藏菜单
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            if(context != null && !context.isFinishing())
                popupWindow.dismiss();
        }

    }

    public void backgroundAlpha(Context context, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            backgroundAlpha(context, 0.4f);
        }
    };
}
