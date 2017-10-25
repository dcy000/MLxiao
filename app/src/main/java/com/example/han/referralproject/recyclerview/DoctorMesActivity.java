package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.medlink.danbogh.call.XDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class DoctorMesActivity extends BaseActivity {

    Button mButton;
    ImageView mImageView;

    ImageView mImageView1;
    TextView mTextView;

    SharedPreferences sharedPreferences;

    public ImageView ImageView1;
    public ImageView ImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mes);
        //    initToolBar();
        mImageView1 = (ImageView) findViewById(R.id.circleImageView);

        mTextView = (TextView) findViewById(R.id.names);

        mButton = (Button) findViewById(R.id.qianyue);

        Intent intent = getIntent();
        final Doctor doctor = (Doctor) intent.getSerializableExtra("docMsg");


        ImageView1 = (ImageView) findViewById(R.id.icon_back);
        ImageView2 = (ImageView) findViewById(R.id.icon_home);

        ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    /*    sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);


        SharedPreferences.Editor editor7 = sharedPreferences.edit();

        editor7.putString("name", doctor.getDocoerName());
        editor7.putString("position", doctor.getDuty());
        editor7.putString("feature", doctor.getGat());
        editor7.putString("image", doctor.getCard());
        editor7.commit();*/


        Picasso.with(this)
                .load(ConstantData.BASE_URL + "/referralProject/" + doctor.getCard())
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(mImageView1);


        mTextView.setText(doctor.getDocoerName());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    show();
                NetworkApi.bindDoctor(MyApplication.getInstance().userId, doctor.doctoerId, new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        XDialogFragment dialogFragment = new XDialogFragment();
                        dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        dialogFragment.show(getSupportFragmentManager(), XDialogFragment.tag());
                    }
                });
            }
        });

      /*  mImageView = (ImageView) findViewById(R.id.icon_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    public void show() {
        Toast toast = new Toast(getApplicationContext());
        LayoutInflater inflate = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.your_custom_layout, null);
        View vs = v.findViewById(R.id.back);//找到你要设透明背景的layout 的id
        vs.getBackground().setAlpha(100);//0~255透明度值

        // 在这里初始化一下里面的文字啊什么的
        toast.setView(v);
        //   toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        showMyToast(toast, 3000);
        // toast.show();
    }


    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, Toast.LENGTH_LONG);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }

   /* private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.title_content);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    mTitleText.setText(str);
        // mTitleText.setTextSize(25);
        //    mTitleText.setGravity(Gravity.CENTER);

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.tips_info);
    }

    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

    public static final String REGEX_CHECK = ".*(qianyue).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(REGEX_CHECK)) {
            mButton.performClick();
            return;
        }

        if (inSpell.matches(REGEX_BACK)) {
            mImageView.performClick();
        }
    }
}
