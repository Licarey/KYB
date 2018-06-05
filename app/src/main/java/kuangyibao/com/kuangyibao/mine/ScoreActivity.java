package kuangyibao.com.kuangyibao.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import kuangyibao.com.kuangyibao.R;

/**
 * 我的积分页面
 */
public class ScoreActivity extends AppCompatActivity {
    private TextView mScoreNum;//我的积分数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mScoreNum = findViewById(R.id.mTvScoreNum);
        TextView title = findViewById(R.id.mTvTitle);
        title.setText("我的积分");
        findViewById(R.id.mIvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
