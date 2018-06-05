package kuangyibao.com.kuangyibao.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseApplication;
import kuangyibao.com.kuangyibao.eventMsg.HomeTitleMessage;
import kuangyibao.com.kuangyibao.eventMsg.ShareMessage;
import kuangyibao.com.kuangyibao.home.fragment.HomeFragment;
import kuangyibao.com.kuangyibao.home.fragment.NewsFragment;
import kuangyibao.com.kuangyibao.home.fragment.PriceFragment;
import kuangyibao.com.kuangyibao.home.fragment.StoreFragment;
import kuangyibao.com.kuangyibao.home.fragment.ZhiShuFragment;
import kuangyibao.com.kuangyibao.share.ShareUtil;
import kuangyibao.com.kuangyibao.share.SinaShareActivity;
import kuangyibao.com.kuangyibao.util.MessageHelper;
import kuangyibao.com.kuangyibao.view.SharePopupWindow;

/**
 * 主页面
 */
public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioButton rb_Home,rb_Price,rb_News,rb_Zhishu ,rb_Store;
    public static int CURRENTINDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MessageHelper.regist(this);
        initView();
        mRadioGroup.setOnCheckedChangeListener(this); 
        fragments = getFragments();
        normalFragment();
    }

    //默认布局
    private void normalFragment() {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(0);
        transaction.replace(R.id.mFragment,fragment);
        transaction.commit();
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        rb_Home= (RadioButton) findViewById(R.id.mRb_home);
        rb_Price= (RadioButton) findViewById(R.id.mRb_price);
        rb_News= (RadioButton) findViewById(R.id.mRb_news);
        rb_Zhishu= (RadioButton) findViewById(R.id.mRb_zhishu);
        rb_Store= (RadioButton) findViewById(R.id.mRb_store);
        rb_Home.setChecked(true);
        setHomeState();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.mRb_home:
                fragment=fragments.get(0);
                transaction.replace(R.id.mFragment,fragment);
                CURRENTINDEX = 0;
                break;
            case R.id.mRb_price:
                fragment=fragments.get(1);
                transaction.replace(R.id.mFragment,fragment);
                CURRENTINDEX = 1;
                break;
            case R.id.mRb_news:
                fragment=fragments.get(2);
                transaction.replace(R.id.mFragment,fragment);
                CURRENTINDEX = 2;
                break;
            case R.id.mRb_zhishu:
                fragment=fragments.get(3);
                transaction.replace(R.id.mFragment,fragment);
                CURRENTINDEX = 3;
                break;
            case R.id.mRb_store:
                fragment=fragments.get(4);
                transaction.replace(R.id.mFragment,fragment);
                CURRENTINDEX = 4;
                break;
        }
        setTabState();
        transaction.commit();
    }

    private void setTabState() {
        setHomeState();
        setPriceState();
        setZhishuState();
        setNewsState();
        setStoreState();
    }

    private void setPriceState() {
        if (rb_Price.isChecked()){
            rb_Price.setBackgroundColor(ContextCompat.getColor(this,R.color.color_ff8700));
        }else{
            rb_Price.setBackgroundColor(ContextCompat.getColor(this,R.color.color_1f97fc));
        }
    }

    private void setZhishuState() {
        if (rb_Zhishu.isChecked()){
            rb_Zhishu.setBackgroundColor(ContextCompat.getColor(this,R.color.color_ff8700));
        }else{
            rb_Zhishu.setBackgroundColor(ContextCompat.getColor(this,R.color.color_1f97fc));
        }
    }

    private void setNewsState() {
        if (rb_News.isChecked()){
            rb_News.setBackgroundColor(ContextCompat.getColor(this,R.color.color_ff8700));
        }else{
            rb_News.setBackgroundColor(ContextCompat.getColor(this,R.color.color_1f97fc));
        }
    }

    private void setStoreState() {
        if (rb_Store.isChecked()){
            rb_Store.setBackgroundColor(ContextCompat.getColor(this,R.color.color_ff8700));
        }else{
            rb_Store.setBackgroundColor(ContextCompat.getColor(this,R.color.color_1f97fc));
        }
    }

    private void setHomeState() {
        if (rb_Home.isChecked()){
            rb_Home.setBackgroundColor(ContextCompat.getColor(this,R.color.color_ff8700));
        }else{
            rb_Home.setBackgroundColor(ContextCompat.getColor(this,R.color.color_1f97fc));
        }
    }

    public List<Fragment> getFragments() {
        fragments.add(new HomeFragment());
        fragments.add(new PriceFragment());
        fragments.add(new NewsFragment());
        fragments.add(new ZhiShuFragment());
        fragments.add(new StoreFragment());
        return fragments;
    }

    public static int getCURRENTINDEX(){
        return CURRENTINDEX;
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final ShareMessage event){//分享
        SharePopupWindow.showWindow(this, findViewById(R.id.mLLRoot), new SharePopupWindow.IShareOption() {
            @Override
            public void shareWechat() {
                //Context context , String title, String description, final String thumbUrl, String httpUrl, final boolean isFriend
                ShareUtil.getInstance(HomeActivity.this).wxSharePage(HomeActivity.this , event.getTitle() , event.getContent() , event.getImageUrl() , event.getUrl() , true);
            }

            @Override
            public void shareFriend() {
                ShareUtil.getInstance(HomeActivity.this).wxSharePage(HomeActivity.this , event.getTitle() , event.getContent() , event.getImageUrl() , event.getUrl() , false);
            }

            @Override
            public void shareSina() {
                //Context context , final IWeiboShareAPI mWeiboShareAPI, String title, String description, String thumbUrl, String httpUrl
                Intent intent = new Intent(HomeActivity.this , SinaShareActivity.class);
                intent.putExtra("shareTitle" , event.getTitle());
                intent.putExtra("shareDesc" , event.getContent());
                intent.putExtra("shareImage" , event.getImageUrl());
                intent.putExtra("shareUrl" , event.getUrl());
                BaseApplication.getInstance().setCurrentClass(HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        MessageHelper.unRegist(this);
        super.onDestroy();
    }
}
