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
import com.example.han.referralproject.bean.AllDoctor;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.Doctors;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.medlink.danbogh.call.XDialogFragment;
import com.medlink.danbogh.register.ConfirmContractActivity;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class DoctorMesActivity extends BaseActivity {

    ImageView mImageView;

    ImageView mImageView1;
    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;


    public ImageView ImageView1;
    public ImageView ImageView2;

    public ImageView mStar1;
    public ImageView mStar2;
    public ImageView mStar3;
    public ImageView mStar4;
    public ImageView mStar5;

    Button mButton;

    private AllDoctor allDoctor;
    private int flag = -1;//记录是从预约医生过来的还是在线医生过来的，0：预约医生，1：在线医生
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mes);
        //    initToolBar();

        mImageView1 = (ImageView) findViewById(R.id.circleImageView);

        mTextView = (TextView) findViewById(R.id.names);
        mTextView1 = (TextView) findViewById(R.id.duty);
        mTextView2 = (TextView) findViewById(R.id.hospital);
        mTextView3 = (TextView) findViewById(R.id.department);
        mTextView4 = (TextView) findViewById(R.id.introduce);
        title = (TextView) findViewById(R.id.title);

        mButton = (Button) findViewById(R.id.qianyue);
        mStar1 = (ImageView) findViewById(R.id.star1);
        mStar2 = (ImageView) findViewById(R.id.star2);
        mStar3 = (ImageView) findViewById(R.id.star3);
        mStar4 = (ImageView) findViewById(R.id.star4);
        mStar5 = (ImageView) findViewById(R.id.star5);

        Intent intent = getIntent();
        Docter doctor = (Docter) intent.getSerializableExtra("docMsg");

        if (intent.getSerializableExtra("docMsg") instanceof Doctors) {
            doctor = (Docter) intent.getSerializableExtra("docMsg");
            flag = 0;
        } else if (intent.getSerializableExtra("docMsg") instanceof AllDoctor) {
            allDoctor = (AllDoctor) intent.getSerializableExtra("docMsg");
            flag = 1;
        }

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


        Picasso.with(this)
                .load(doctor.getDocter_photo())
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(mImageView1);


        mTextView.setText(doctor.getDoctername());
        mTextView1.setText(doctor.getDuty());
        mTextView2.setText(doctor.getHosname());
        mTextView3.setText(doctor.getDepartment());
        mTextView4.setText(doctor.getPro());

        if (Integer.parseInt(doctor.getEvaluation()) <= 60) {

            mStar1.setVisibility(View.VISIBLE);

        } else if (Integer.parseInt(doctor.getEvaluation()) <= 70 &&
                Integer.parseInt(doctor.getEvaluation()) > 60) {
            mStar1.setVisibility(View.VISIBLE);
            mStar2.setVisibility(View.VISIBLE);


        } else if (Integer.parseInt(doctor.getEvaluation()) <= 80 &&
                Integer.parseInt(doctor.getEvaluation()) > 70) {
            mStar1.setVisibility(View.VISIBLE);
            mStar2.setVisibility(View.VISIBLE);
            mStar3.setVisibility(View.VISIBLE);


        } else if (Integer.parseInt(doctor.getEvaluation()) <= 90 &&
                Integer.parseInt(doctor.getEvaluation()) > 80) {
            mStar1.setVisibility(View.VISIBLE);
            mStar2.setVisibility(View.VISIBLE);
            mStar3.setVisibility(View.VISIBLE);
            mStar4.setVisibility(View.VISIBLE);


        } else if (Integer.parseInt(doctor.getEvaluation()) > 90
                ) {
            mStar1.setVisibility(View.VISIBLE);
            mStar2.setVisibility(View.VISIBLE);
            mStar3.setVisibility(View.VISIBLE);
            mStar4.setVisibility(View.VISIBLE);
            mStar5.setVisibility(View.VISIBLE);


        }

        if (flag == 0) {
            Picasso.with(this)
                    .load(ConstantData.BASE_URL + "/referralProject/" + doctor.getDocter_photo())
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(mImageView1);


            mTextView.setText(doctor.getDoctername());
            mTextView1.setText(doctor.getDuty());
            mTextView2.setText(doctor.getHosname());
            mTextView3.setText(doctor.getDepartment());
            mTextView4.setText(doctor.getPro());
            mButton.setText("签约");
            title.setText(R.string.doctor_qianyue);

        } else if (flag == 1) {
            Picasso.with(this)
                    .load(allDoctor.docter_photo)
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(mImageView1);

            mTextView.setText(allDoctor.doctername);
            mTextView1.setText(allDoctor.duty);
            mTextView2.setText(allDoctor.hosname);
            mTextView3.setText(allDoctor.department);
            mTextView4.setText(allDoctor.pro);
            mButton.setText("咨询");
            title.setText(R.string.doctor_zixun);
        }

        final Docter finalDoctor = doctor;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {

                    ConfirmContractActivity.start(DoctorMesActivity.this, finalDoctor.getDocterid());
                }

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

//    public void show() {
//        Toast toast = new Toast(getApplicationContext());
//        LayoutInflater inflate = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflate.inflate(R.layout.your_custom_layout, null);
//        View vs = v.findViewById(R.id.back);//找到你要设透明背景的layout 的id
//        vs.getBackground().setAlpha(100);//0~255透明度值
//
//        // 在这里初始化一下里面的文字啊什么的
//        toast.setView(v);
//        //   toast.setDuration(Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
//        showMyToast(toast, 3000);
//        // toast.show();
//    }


//    public void showMyToast(final Toast toast, final int cnt) {
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                toast.show();
//            }
//        }, 0, Toast.LENGTH_LONG);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                toast.cancel();
//                timer.cancel();
//            }
//        }, cnt);
//    }

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
        setDisableGlobalListen(true);
        if (flag == 0) {//预约医生
            speak(R.string.tips_info);
        } else if (flag == 1) {//咨询在线医生

        }

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

            Intent intent = new Intent(getApplicationContext(), RecoDocActivity.class);
            startActivity(intent);
            finish();

            //  mImageView.performClick();
        }
    }
}
