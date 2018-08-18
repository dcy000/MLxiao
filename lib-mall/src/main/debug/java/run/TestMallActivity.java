package run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.medlink.mall.R;
import com.medlink.mall.ui.MallActivity;

public class TestMallActivity extends AppCompatActivity implements View.OnClickListener {

    TextView welcomeText, actionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mall);
        welcomeText = findViewById(R.id.tv_mall_welcome);
        actionText = findViewById(R.id.tv_mall_action);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mall_action:
                startActivity(new Intent(this, MallActivity.class));
        }
    }
}
