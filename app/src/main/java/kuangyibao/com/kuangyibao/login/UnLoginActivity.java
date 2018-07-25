package kuangyibao.com.kuangyibao.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.andsync.xpermission.XPermissionUtils;

import kuangyibao.com.kuangyibao.R;
import kuangyibao.com.kuangyibao.base.BaseActivity;
import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.mine.AgreementActivity;
import kuangyibao.com.kuangyibao.util.DialogUtil;
import kuangyibao.com.kuangyibao.util.Utils;
import kuangyibao.com.kuangyibao.view.CodeDialogView;

/**
 * 未登陆页面
 */
public class UnLoginActivity extends BaseActivity implements View.OnClickListener {
    private View login;
    private int CAMERA_REQUESTCODE = 0x11;

    @Override
    public int getContentViewId() {
        return R.layout.activity_unlogin;
    }

    @Override
    protected void initView() {
        super.initView();
        login = findViewById(R.id.mRlLogin);
        login.setOnClickListener(this);
        findViewById(R.id.mLLCode).setOnClickListener(this);
        findViewById(R.id.mLLAbout).setOnClickListener(this);
        findViewById(R.id.mLLContact).setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mRlLogin:
                startActivity(new Intent(this , LoginActivity.class));
                break;
            case R.id.mLLAbout://关于我们
                Intent intent = new Intent(this , AgreementActivity.class);
                intent.putExtra("title" , "关于我们");
                intent.putExtra("url" , Urls.BASEURL + "/wap/aboutus.html?ver=" + Utils.getVersionName(this));
                startActivity(intent);
                break;
            case R.id.mLLCode://二维码
                CodeDialogView.Builder dialogBuild = new CodeDialogView.Builder(this);
                CodeDialogView dialog = dialogBuild.create();
                dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                dialog.show();
                break;
            case R.id.mLLContact://联系客服
                doOpenCallPhone();
                break;

        }
    }

    private void doOpenCallPhone() {
        XPermissionUtils.requestPermissions(this, CAMERA_REQUESTCODE, new String[] { Manifest.permission.CALL_PHONE },
                new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0633-8277880"));
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(final String[] deniedPermissions, boolean alwaysDenied) {
                        Toast.makeText(UnLoginActivity.this, "获取通话权限失败", Toast.LENGTH_SHORT).show();
                        if (alwaysDenied) { // 拒绝后不再询问 -> 提示跳转到设置
                            DialogUtil.showPermissionManagerDialog(UnLoginActivity.this, "通话");
                        } else {    // 拒绝 -> 提示此公告的意义，并可再次尝试获取权限
                            new AlertDialog.Builder(UnLoginActivity.this).setTitle("温馨提示")
                                    .setMessage("我们需要通话权限才能正常使用该功能")
                                    .setNegativeButton("取消", null)
                                    .setPositiveButton("验证权限", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            XPermissionUtils.requestPermissionsAgain(UnLoginActivity.this, deniedPermissions,
                                                    CAMERA_REQUESTCODE);
                                        }
                                    })
                                    .show();
                        }
                    }
                });
    }
}
