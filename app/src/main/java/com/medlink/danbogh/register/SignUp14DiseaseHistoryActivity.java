package com.medlink.danbogh.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.idcard.SignInIdCardActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.SpeechConstant;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.medlink.danbogh.utils.T;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp14DiseaseHistoryActivity extends BaseActivity {

    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;
    private DiseaseHistoryAdapter mAdapter;
    public List<DiseaseHistoryModel> mModels;
    public GridLayoutManager mLayoutManager;
    private Handler btHandler;
    private byte[] jpgData;
    private Runnable faceRegisterRunnable;
    private UploadManager uploadManager;
    private SimpleDateFormat simple;
    private Random random;
    private String authId;
    private String bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up14_disease_history);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvContent.setLayoutManager(mLayoutManager);
        mModels = modals();
        mAdapter = new DiseaseHistoryAdapter(mModels);
        rvContent.setAdapter(mAdapter);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab2HealthInfo.setTextColor(getResources().getColor(R.color.textColorSelected));
    }

    private List<DiseaseHistoryModel> modals() {
        mModels = new ArrayList<>(9);
        String[] diseaseTypes = getResources().getStringArray(R.array.disease_type);
        for (String diseaseType : diseaseTypes) {
            DiseaseHistoryModel model = new DiseaseHistoryModel(
                    diseaseType,
                    false,
                    R.color.textColorDiseaseSelected,
                    R.color.textColorDiseaseUnselected,
                    R.drawable.bg_tv_disease_selected,
                    R.drawable.bg_tv_disease
            );
            mModels.add(model);
        }
        return mModels;
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_disease_history_tip);
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
//        String mh = getMh();
//        if (TextUtils.isEmpty(mh)) {
//            mh = "11";
////            navToNext();
////            return;
//        }

//        showLoadingDialog(getString(R.string.do_uploading));
//        NetworkApi.setUserMh(mh, new NetworkManager.SuccessCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                hideLoadingDialog();
//                navToNext();
//            }
//        }, new NetworkManager.FailedCallback() {
//            @Override
//            public void onFailed(String message) {
//                hideLoadingDialog();
//            }
//        });

        navToNext();
    }

    private void navToNext() {
//        Intent intent = new Intent(mContext, RegisterVideoActivity.class);
//        startActivity(intent);
        onRegister();
    }

    private String getMh() {
        StringBuilder mhBuilder = new StringBuilder();
        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            if (mModels.get(i).isSelected()) {
                mhBuilder.append(i + 1);
                mhBuilder.append(",");
            }
        }
        int length = mhBuilder.length();
        return length == 0 ? mhBuilder.toString() : mhBuilder.substring(0, length - 1);
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        //语音选择病史标签
        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            DiseaseHistoryModel model = mModels.get(i);
            if (result.contains(model.getName())) {
                View view = rvContent.getChildAt(i);
                DiseaseHolder holder = (DiseaseHolder) rvContent.getChildViewHolder(view);
                holder.onTvDiseaseClicked();
                return;
            }
        }
    }


    private void onRegister() {
        showLoadingDialog("加载中");
        final LocalShared shared = LocalShared.getInstance(this);
        String eat = shared.getSignUpEat();
        String smoke = shared.getSignUpSmoke();
        String drink = shared.getSignUpDrink();
        String sport = shared.getSignUpSport();
        String name = shared.getSignUpName();
        String gender = shared.getSignUpGender();
        String address = shared.getSignUpAddress();
        String idCard = shared.getSignUpIdCard().replaceAll(" ", "").trim();
        String phone = shared.getSignUpPhone();
        float height = shared.getSignUpHeight();
        float weight = shared.getSignUpWeight();
        String bloodType = shared.getSignUpBloodType();
        String mh = getMh();
        String allergy = "5";
        String fetation = "1";
        NetworkApi.registerYiYuanUser(
                name,
                gender,
                address,
                idCard,
                phone,
                "123456",
                height,
                weight,
                bloodType,
                eat,
                smoke,
                drink,
                sport,
                allergy,
                fetation,
                mh,
                new NetworkManager.SuccessCallback<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean response) {
                        hideLoadingDialog();
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        shared.setUserInfo(response);
                        LocalShared.getInstance(mContext).setSex(response.sex);
                        LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                        LocalShared.getInstance(mContext).setUserAge(response.age);
                        LocalShared.getInstance(mContext).setUserHeight(response.height);
                        bid = response.bid;
                        new JpushAliasUtils(SignUp14DiseaseHistoryActivity.this).setAlias("user_" + bid);
                        onRegisterSuccess();

                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        onRegisterFailed();
                    }
                }
        );
    }

    private void onRegisterFailed() {
        T.show("注册失败,请稍后重试~");
    }

    private void onRegisterSuccess() {
//        btHandler().post(upHeadPhotoRunnable());
        startActivity(new Intent(this, MainActivity.class));
        T.show("注册成功");
    }

    private Runnable upHeadPhotoRunnable() {
        if (faceRegisterRunnable == null) {
            faceRegisterRunnable = new Runnable() {
                @Override
                public void run() {
                    if (MyApplication.bitmap == null) {
                        T.show("头像上传失败");
                        return;
                    }

                    ByteArrayOutputStream stream = null;
                    try {
                        stream = new ByteArrayOutputStream();
                        MyApplication.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        jpgData = stream.toByteArray();
                        //上传头像
                        uploadProfile(MyApplication.getInstance().userId, "");
                    } finally {
                        try {
                            if (stream != null) {
                                stream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        return faceRegisterRunnable;
    }

    private Handler btHandler() {
        if (btHandler == null) {
            synchronized (SignInIdCardActivity.class) {
                if (btHandler == null) {
                    HandlerThread thread = new HandlerThread("bt");
                    thread.start();
                    btHandler = new Handler(thread.getLooper());
//                    btHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return btHandler;
    }

    private void uploadProfile(final String userid, final String xfid) {
        if (jpgData == null) {
            T.show("头像上传失败");
            return;
        }
        showLoadingDialog("加载中");
        NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                hideLoadingDialog();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (jpgData == null) {
                    T.show("头像上传失败");
                    return;
                }
                UpCompletionHandler completionHandler = new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            NetworkApi.return_imageUrl(imageUrl, bid, xfid,
                                    new NetworkManager.SuccessCallback<Object>() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            if (isFinishing() || isDestroyed()) {
                                                return;
                                            }
                                            onUpLoadToServerSuccess();
                                        }

                                    }, new NetworkManager.FailedCallback() {
                                        @Override
                                        public void onFailed(String message) {
                                            if (isFinishing() || isDestroyed()) {
                                                return;
                                            }

                                        }
                                    });
                        } else {
                            T.show("头像上传失败");
                        }
                    }
                };
                String key = buildAuthId() + ".jpg";
                uploadManager().put(jpgData, key, response, completionHandler, null);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                T.show("头像上传失败");
            }
        });
    }

    private void onUpLoadToServerSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        T.show("注册成功");

    }

    private UploadManager uploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        return uploadManager;
    }

    private String buildAuthId() {
        if (simple == null) {
            simple = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        }
        StringBuilder randomBuilder = new StringBuilder();//定义变长字符串
        if (random == null) {
            random = new Random();
        }
        for (int i = 0; i < 8; i++) {
            randomBuilder.append(random.nextInt(10));
        }
        Date date = new Date();
        authId = simple.format(date) + randomBuilder;
        return authId;
    }

}
