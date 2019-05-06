package com.example.module_control_volume.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.module_control_volume.R;

public class ScreenPointActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout contentScreenPoint;
    private int colorType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_point);
        contentScreenPoint=findViewById(R.id.content_screen_point);
        contentScreenPoint.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
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
                    contentScreenPoint.setBackgroundColor(getResources().getColor(R.color.colorAccent));
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
                    colorType = 0;
                    break;
                default:
                    break;
            }

        } else {
        }
    }
}
