package com.gcml.module_factory_test.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.gcml.module_factory_test.R;
import com.gcml.module_factory_test.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScreenPointActivity extends AppCompatActivity {

    @BindView(R2.id.content_screen_point)
    RelativeLayout contentScreenPoint;
    private int colorType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_activity_screen_point);
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.content_screen_point})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.content_screen_point) {
            colorType++;
            switch (colorType) {
                case 0:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    break;
                case 1:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    break;
                case 2:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorFactory));
                    break;
                case 3:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                    break;
                case 4:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    break;
                case 5:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    break;
                case 6:
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorViolet));
                    break;
                case 7:
                    finish();
                    colorType = -1;
                    break;
                default:
                    break;
            }

        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
