package run;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.family.R;

public class TestFamilyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle, mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_family);
        mTitle = findViewById(R.id.tv_family_title);
        mAction = findViewById(R.id.tv_family_action);
        mAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_family_action:
                CC.obtainBuilder("app.component.family").addParam("startType", "MLVoice").build().callAsync();
        }
    }
}
