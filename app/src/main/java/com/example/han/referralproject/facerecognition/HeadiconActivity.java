package com.example.han.referralproject.facerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.data.UserSpHelper;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import timber.log.Timber;

@Deprecated
public class HeadiconActivity extends BaseActivity {

    ImageView mCircleImageView;
    Button mButton;
    Button mButton1;

    private UploadManager uploadManager;

    String imageData1;

    ImageView mImageView1;
    ImageView mImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_headicon);
        uploadManager = new UploadManager();
        mCircleImageView = (CircleImageView) findViewById(R.id.per_image);

        mButton = findViewById(R.id.cancel);
        mButton1 = findViewById(R.id.trues);

        mImageView1 = findViewById(R.id.icon_back);
        mImageView2 = findViewById(R.id.icon_home);

        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定头像的时候就给该机器创建唯一的人脸识别组
                final String userid = UserSpHelper.getUserId();
                final String xfid = LocalShared.getInstance(HeadiconActivity.this).getXunfeiId();
                checkGroup(userid, xfid);
//                createGroup(userid, xfid);
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterVideoActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
                finish();
            }
        });


        imageData1 = LocalShared.getInstance(getApplicationContext()).getUserImg();

        if (imageData1 != null) {
            byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mCircleImageView.setImageBitmap(bitmap);
        }

        speak(R.string.head_icon);

        Intent intent = getIntent();
        isFast = intent.getBooleanExtra("isFast", false);
    }

    boolean isFast;

    private void checkGroup(final String userid, final String xfid) {
        //在登录的时候判断该台机器有没有创建人脸识别组，如果没有则创建
        String groupId = LocalShared.getInstance(mContext).getGroupId();
        String firstXfid = LocalShared.getInstance(mContext).getGroupFirstXfid();
        Timber.e("组id" + groupId);
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(firstXfid)) {
            Log.e("组信息", "checkGroup: 该机器组已近存在");
            joinGroup(userid, groupId, xfid);
        } else {
            createGroup(userid, xfid);
        }
    }

    private void uploadHeadToSelf(final String userid, final String xfid) {
        NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                byte[] data = Base64.decode(imageData1.getBytes(), 1);
                Date date = new Date();
                SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                StringBuilder str = new StringBuilder();//定义变长字符串
                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    str.append(random.nextInt(10));
                }
                //将字符串转换为数字并输出
                String key = simple.format(date) + str + ".jpg";

                uploadManager.put(data, key, response, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            NetworkApi.return_imageUrl(imageUrl, UserSpHelper.getUserId(), LocalShared.getInstance(getApplicationContext()).getXunfeiId(),
                                    new NetworkManager.SuccessCallback<Object>() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            //将账号在本地缓存
                                            LocalShared.getInstance(mContext).addAccount(userid, xfid);
                                            Class<? extends BaseActivity> aClass = isFast ? MainActivity.class : RecoDocActivity.class;
                                            Intent intent = new Intent(getApplicationContext(), aClass);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }, new NetworkManager.FailedCallback() {
                                        @Override
                                        public void onFailed(String message) {
                                            Log.e("注册储存讯飞id失败", "onFailed: ");
                                        }
                                    });
                        } else {

                        }
                    }
                }, null);
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    private void createGroup(final String userid, final String xfid) {
        FaceAuthenticationUtils.getInstance(HeadiconActivity.this).createGroup(xfid);
        FaceAuthenticationUtils.getInstance(HeadiconActivity.this).setOnCreateGroupListener(new ICreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                Timber.e("创建组成功" + result);
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    LocalShared.getInstance(HeadiconActivity.this).setGroupId(groupId);
                    LocalShared.getInstance(HeadiconActivity.this).setGroupFirstXfid(xfid);
                    //组创建好以后把自己加入到组中去
                    joinGroup(userid, groupId, xfid);
                    //加组完成以后把头像上传到我们自己的服务器
                    uploadHeadToSelf(userid, xfid);
                    FaceAuthenticationUtils.getInstance(HeadiconActivity.this).updateGroupInformation(groupId, xfid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "创建组失败");
//                if (error.getErrorCode() == 10144) {//创建组的数量达到上限
//                    ToastUtils.showShort("出现技术故障，请致电客服咨询");
//                }
                //如果在此处创建组失败就跳过创建
                uploadHeadToSelf(userid, xfid);
            }
        });
    }

    private static String TAG = "HeadiconActivity";

    private void joinGroup(final String userid, String groupid, final String xfid) {
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, xfid);
        FaceAuthenticationUtils.getInstance(HeadiconActivity.this).setOnJoinGroupListener(new IJoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                uploadHeadToSelf(userid, xfid);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                    createGroup(userid, xfid);
                } else {
                    uploadHeadToSelf(userid, xfid);
                }

            }
        });
    }


    public void getImageUrl(String token, final String userid, final String xfid) {


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        startListening();
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(".*(queding|wancheng|xiayibu).*")) {
            mButton1.performClick();
            return;
        }
        if (inSpell.matches(".*(quxiao|chongxin|zhongxin|zhongpai|zaipai|chongpai|zhongpai).*")) {
            mButton.performClick();
        }
    }
}
