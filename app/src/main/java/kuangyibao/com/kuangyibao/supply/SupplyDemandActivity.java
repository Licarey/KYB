package kuangyibao.com.kuangyibao.supply;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kuangyibao.com.kuangyibao.GuessPriceActivity;
import kuangyibao.com.kuangyibao.R;

/**
 * 供求页面
 */
public class SupplyDemandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_demand);
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
