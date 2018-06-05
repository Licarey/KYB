package kuangyibao.com.kuangyibao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import kuangyibao.com.kuangyibao.config.Urls;
import kuangyibao.com.kuangyibao.entity.ADEntity;
import kuangyibao.com.kuangyibao.entity.BaseEntity;
import kuangyibao.com.kuangyibao.home.HomeActivity;
import kuangyibao.com.kuangyibao.login.LoginActivity;
import kuangyibao.com.kuangyibao.util.ImageUtils;
import kuangyibao.com.kuangyibao.util.SpUtils;
import kuangyibao.com.kuangyibao.util.Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 猜价格页面
 */
public class GuessPriceActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView personNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_price);

        findViewById(R.id.mIvPrice_up).setOnClickListener(this);
        findViewById(R.id.mIvPrice_ping).setOnClickListener(this);
        findViewById(R.id.mIvPrice_down).setOnClickListener(this);
        personNum = (TextView) findViewById(R.id.mTvNum);

        personNum.setText(getIntent().getStringExtra("personNum") + "");
    }


    @Override
    public void onClick(View v) {
        if(!Utils.isConnected(this)){
            Toast.makeText(this , "网络异常，请检查网络" , 0).show();
            return;
        }
        switch (v.getId()){
            case R.id.mIvPrice_up:
                guessPrice("1");
                break;
            case R.id.mIvPrice_ping:
                guessPrice("2");
                break;
            case R.id.mIvPrice_down:
                guessPrice("3");
                break;
        }
    }

    private void guessPrice(String change){
        OkHttpUtils.getInstance()
                .post()
                .url(Urls.POST_GUESSPRICE_URL)
                .addHeader("appId" , Utils.getDeviceUniqID(this))
                .addHeader("uId" , Utils.getUID(this))
                .addHeader("token" , SpUtils.getString(this , "token" , ""))
                .addParams("change" , change)
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
                        if(o != null && (o.getMessageId().equals("1") || o.getMessageId().equals("2"))){//或已猜过
                            startActivity(new Intent(GuessPriceActivity.this , HomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(GuessPriceActivity.this , "" + o.getMessageCont() , 0).show();
                        }
                    }
                });
    }
}
