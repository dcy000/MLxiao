package debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.mall.R;
import com.gcml.mall.ui.MallActivity;

public class TestMallActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle, mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mall);
        mTitle = findViewById(R.id.tv_mall_title);
        mAction = findViewById(R.id.tv_mall_action);
        mAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mall_action:
                startActivity(new Intent(this, MallActivity.class));
        }
    }
}
