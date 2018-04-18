package com.medlink.danbogh.register.idcard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.CreateGroupListener;
import com.example.han.referralproject.facerecognition.FaceAuthenticationUtils;
import com.example.han.referralproject.facerecognition.JoinGroupListener;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;
import com.medlink.danbogh.register.simple.SignUp02MobileVerificationActivity;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.medlink.danbogh.utils.T;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**
 *
 */
public class SignInIdCardActivity extends BaseActivity {

    private static final String TAG = "Bluetooth";
    private static final String FILTER = "KT8000";
    private static final int PROTOCOL_TYPE = 0;

    private BluetoothAdapter bluetoothAdapter;
    private BtReadClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity_id_card);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身  份  证  登  录");
        registerReceiver();
        client = BtReadClient.getInstance();
        client.setBluetoothListener(onBluetoothListener);
        btHandler().postDelayed(oneShutRunnable, 1000);
    }

    private long startTime;

    private Runnable oneShutRunnable = new Runnable() {
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            if (bluetoothAdapter == null) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
            initDevice();
        }
    };

    private boolean initializing;

    private void initDevice() {
        if (initializing) {
            return;
        }
        initializing = true;
        if (targetDevice == null) {
            if (!findBoundTargetDevice()) {
                findTargetDevice();
            } else {
                Log.i(TAG, "Target Device Bound: named start with " + FILTER);
                onDeviceInitialized();
            }
        } else {
            Log.i(TAG, "Target Device Ready: named start with " + FILTER);
            onDeviceInitialized();
        }
    }

    private void onDeviceInitialized() {
        initializing = false;
        String address = targetDevice == null ? "targetDevice == null" : targetDevice.getAddress();
        Log.i(TAG, "Found Device: " + address);
        if (targetDevice == null) {
            onDeviceNotFound();
            return;
        }
        btHandler().post(readRunnable);
    }


    private void findTargetDevice() {
        if (bluetoothAdapter == null) {
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    private boolean ensureDeviceConnected() {
        boolean success = false;
        if (client != null && targetDevice != null) {
            if (client.getBtState() == 0) {//0是断开状态，2是连接状态
                success = client.connectBt(targetDevice.getAddress());
            } else if (client.getBtState() == 2) {
                success = true;
            }
        }
        Log.i(TAG, "ensureDeviceConnected: " + success);
        return success;
    }

    private boolean findBoundTargetDevice() {
        if (bluetoothAdapter == null) {
            return false;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        Log.i(TAG, "bondedDevices: " + devices);
        if (devices != null && devices.size() > 0) {
            String name;
            for (BluetoothDevice device : devices) {
                if (device == null) {
                    continue;
                }
                name = device.getName();
                if (TextUtils.isEmpty(name)) {
                    continue;
                }
                if (name.toUpperCase().startsWith(FILTER)) {
                    targetDevice = device;
                    return true;
                }
            }
        }
        return false;
    }

    private volatile BluetoothDevice targetDevice;

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
            if (TextUtils.isEmpty(action)) {
                return;
            }
            BluetoothDevice device;
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        String name = device.getName();
                        if (!TextUtils.isEmpty(name)) {
                            if (name.toUpperCase().startsWith(FILTER)) {
                                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                                    bluetoothAdapter.cancelDiscovery();
                                }
                                targetDevice = device;
                                createBond(device);
                                onDeviceInitialized();
                            }
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    onDeviceInitialized();
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null) {
                        break;
                    }
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            Log.d(TAG, "BOND_NONE: ");
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            Log.d(TAG, "BOND_BONDING: ");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d(TAG, "BOND_BONDED: ");
                            break;
                    }
                    break;
            }
        }
    };

    public static boolean createBond(BluetoothDevice device) {
        boolean success = false;
        try {
            Method createBond_Method = BluetoothDevice.class.getMethod("createBond");
            createBond_Method.setAccessible(true);
            success = (Boolean) createBond_Method.invoke(device);
            Log.d(TAG, "createBond: " + success);
        } catch (Throwable e) {
            Log.e(TAG, "createBond: " + success, e);
        }
        return success;
    }

    private OnBluetoothListener onBluetoothListener = new OnBluetoothListener() {
        @Override
        public void connectResult(boolean success) {
            Log.d(TAG, "connectResult: " + success);

        }

        @Override
        public void connectionLost() {
            Log.d(TAG, "connectionLost: ");
        }
    };

    private long readStartTime;

    private Runnable readRunnable = new Runnable() {
        public volatile boolean isRead;

        @Override
        public void run() {
            if (isRead) {
                return;
            }
            isRead = true;
            boolean connected = ensureDeviceConnected();
            if (connected && client != null && !isFinishing() && !isDestroyed()) {
                readStartTime = System.currentTimeMillis();
                final IDCardItem item = client.readCert(PROTOCOL_TYPE);
                isRead = false;
                if (isFinishing() && isDestroyed()) {
                    return;
                }
                if (item != null && item.retCode == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReadSuccess(item);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReadFailed();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onReadFailed();
                    }
                });
            }
        }
    };

    public Runnable checkGroupRunnable;

    public Runnable checkGroupRunnable() {
        if (checkGroupRunnable == null) {
            checkGroupRunnable = new Runnable() {
                @Override
                public void run() {
                    if (authId != null) {
                        checkGroup(authId);
                    }
                }
            };
        }
        return checkGroupRunnable;
    }

    private void checkGroup(final String xfid) {
        if (xfid == null) {
            onCreateGroupFailed();
            return;
        }
        //在登录的时候判断该台机器有没有创建人脸识别组，如果没有则创建
        String groupId = LocalShared.getInstance(mContext).getGroupId();
        String firstXfid = LocalShared.getInstance(mContext).getGroupFirstXfid();
        Logger.e("组id" + groupId);
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(firstXfid)) {
            Log.e("组信息", "checkGroup: 该机器组已近存在");
            joinGroup(groupId, xfid);
        } else {
            createGroup(xfid);
        }
    }

    private JoinGroupListener joinGroupListener;

    private void joinGroup(String groupid, final String xfid) {
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, xfid);
        if (joinGroupListener == null) {
            joinGroupListener = new JoinGroupListener() {
                @Override
                public void onResult(IdentityResult result, boolean islast) {

                }

                @Override
                public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

                }

                @Override
                public void onError(SpeechError error) {
                    Logger.e(error, "添加成员出现异常");
                    if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                        createGroup(xfid);
                    }

                }
            };
        }
        FaceAuthenticationUtils.getInstance(SignInIdCardActivity.this).setOnJoinGroupListener(joinGroupListener);
    }

    private CreateGroupListener createListener;

    private void createGroup(final String xfid) {
        FaceAuthenticationUtils.getInstance(this).createGroup(xfid);
        if (createListener == null) {
            createListener = new CreateGroupListener() {
                @Override
                public void onResult(IdentityResult result, boolean islast) {
                    try {
                        JSONObject resObj = new JSONObject(result.getResultString());
                        String groupId = resObj.getString("group_id");
                        LocalShared.getInstance(SignInIdCardActivity.this).setGroupId(groupId);
                        LocalShared.getInstance(SignInIdCardActivity.this).setGroupFirstXfid(xfid);
                        //组创建好以后把自己加入到组中去
                        onCreateGroupSuccess();
                        joinGroup(groupId, xfid);
                        FaceAuthenticationUtils.getInstance(SignInIdCardActivity.this).updateGroupInformation(groupId, xfid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

                }

                @Override
                public void onError(SpeechError error) {
                    Logger.e(error, "创建组失败");
                    onCreateGroupFailed();
//                ToastTool.showShort("出现技术故障，请致电客服咨询" + error.getErrorCode());
                }
            };
        }
        FaceAuthenticationUtils.getInstance(this).setOnCreateGroupListener(createListener);
    }

    private void onCreateGroupFailed() {

    }

    private void onCreateGroupSuccess() {

    }

    private void onReadFailed() {
        item = null;
        speak("请重新刷身份证");
        btHandler().postDelayed(readRunnable, 1000);
    }

    private void onDeviceNotFound() {
        item = null;
        speak("找不到设备");
        btHandler().postDelayed(readRunnable, 1000);
    }

    private IDCardItem item;

    private void onReadSuccess(IDCardItem item) {
        this.item = item;
        speak("读取成功");
        long currentTimeMillis = System.currentTimeMillis();
        long totalTime = currentTimeMillis - startTime;
        long readTime = currentTimeMillis - readStartTime;
        Log.d(TAG, "onReadSuccess: totalTime = " + totalTime);
        Log.d(TAG, "onReadSuccess: readTime = " + readTime);
        Log.d(TAG, "onReadSuccess: " + item.toString());
        showLoadingDialog("加载中");
        NetworkApi.isRegisteredByIdCard(item.certNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                hideLoadingDialog();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
                new JpushAliasUtils(SignInIdCardActivity.this).setAlias("user_" + response.bid);
                onAccountRegistered(response);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                onAccountNotRegistered();
            }
        });
    }

    private void onAccountNotRegistered() {
        Intent intent = new Intent().setClass(
                this,
                SignUp02MobileVerificationActivity.class)
                .putExtra("forResult", true);
        startActivityForResult(intent, 18);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 18:
                if (resultCode == RESULT_OK
                        && data != null
                        && !TextUtils.isEmpty(data.getStringExtra("phone"))) {
                    String phone = data.getStringExtra("phone");
                    onRegister(phone);
                } else {
                    onReadFailed();
                }
                break;
            default:
                break;
        }
    }

    private void onRegister(String phone) {
        showLoadingDialog("加载中");
        final LocalShared shared = LocalShared.getInstance(this);
        String name = shared.getSignUpName();
        String gender = shared.getSignUpGender();
        String address = shared.getSignUpAddress();
        String idCard = shared.getSignUpIdCard();
        float height = shared.getSignUpHeight();
        float weight = shared.getSignUpWeight();
        String bloodType = shared.getSignUpBloodType();
        String eat = shared.getSignUpEat();
        String smoke = shared.getSignUpSmoke();
        String drink = shared.getSignUpDrink();
        String sport = shared.getSignUpSport();
        NetworkApi.registerUser(
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
                        new JpushAliasUtils(SignInIdCardActivity.this).setAlias("user_" + response.bid);
                        NetworkApi.setUserMh("11", new NetworkManager.SuccessCallback<String>() {
                            @Override
                            public void onSuccess(String response) {
                                if (isFinishing() || isDestroyed()) {
                                    return;
                                }
                                onRegisterSuccess();
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {
                                if (isFinishing() || isDestroyed()) {
                                    return;
                                }
                                onRegisterFailed();
                            }
                        });
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
        onReadFailed();
    }

    private void onRegisterSuccess() {
        btHandler().post(faceRegisterRunnable());
    }

    private void onAccountRegistered(UserInfoBean response) {
        showLoadingDialog("加载中");
        NetworkApi.login(response.tel, "123456", new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                Logger.e("本次登录人的userid" + response.bid);
                hideLoadingDialog();
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                checkGroup(response.xfid);
                new JpushAliasUtils(SignInIdCardActivity.this).setAlias("user_" + response.bid);
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).addAccount(response.bid, response.xfid);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
                onLoginSuccess();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                onLoginFailed();
            }
        });
    }

    private void onLoginSuccess() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private void onLoginFailed() {
        onReadFailed();
    }

    private Runnable faceRegisterRunnable;

    private void onFaceAlreadyExist() {
        onReadFailed();
    }

    private void onFaceRegisterSuccess() {
        uploadProfile(MyApplication.getInstance().userId, authId);
    }

    private void onFaceRegisterFailed() {
        onReadFailed();
    }

    private Runnable faceRegisterRunnable() {
        if (faceRegisterRunnable == null) {
            faceRegisterRunnable = new Runnable() {
                @Override
                public void run() {
                    if (item == null) {
                        return;
                    }
                    byte[] data = item.picData;
                    if (data == null) {
                        return;
                    }
                    if (faceRequest == null) {
                        faceRequest = new FaceRequest(SignInIdCardActivity.this);
                    }
                    faceRequest.setParameter(SpeechConstant.AUTH_ID, buildAuthId());
                    faceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                    faceRequest.sendRequest(data, faceRequestListener());
                }
            };
        }
        return faceRegisterRunnable;
    }

    private FaceRequest faceRequest;

    private RequestListener requestListener;

    private RequestListener faceRequestListener() {
        if (requestListener == null) {
            requestListener = new RequestListener() {
                @Override
                public void onEvent(int eventType, Bundle params) {
                    Log.d(TAG, "onEvent: eventType = " + eventType);
                    Log.d(TAG, "onEvent: params =" + params);
                }

                @Override
                public void onBufferReceived(byte[] bytes) {
                    try {
                        String result = new String(bytes, "utf-8");
                        Log.d(TAG, "onBufferReceived: ");
                        JSONObject jsonObject = new JSONObject(result);
                        String type = jsonObject.optString("sst");
                        if ("reg".equals(type)) {
                            int ret = jsonObject.optInt("ret");
                            if (ret != 0) {
                                onFaceRegisterFailed();
                                return;
                            }
                            if ("success".equals(jsonObject.get("rst"))) {
                                onFaceRegisterSuccess();
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        onFaceRegisterFailed();
                    }
                }

                @Override
                public void onCompleted(SpeechError error) {
                    if (error != null) {
                        switch (error.getErrorCode()) {
                            case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                                onFaceAlreadyExist();
                            default:
                                break;
                        }
                    }
                }
            };
        }
        return requestListener;
    }

    private volatile String authId;
    private SimpleDateFormat simple;
    private Random random;

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

    private UploadManager uploadManager;

    private UploadManager uploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        return uploadManager;
    }

    private void uploadProfile(final String userid, final String xfid) {
        if (item.picData == null) {
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
                String key = buildAuthId() + ".jpg";
                UpCompletionHandler completionHandler = new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            NetworkApi.return_imageUrl(imageUrl, MyApplication.getInstance().userId, LocalShared.getInstance(getApplicationContext()).getXunfeiId(),
                                    new NetworkManager.SuccessCallback<Object>() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            //将账号在本地缓存
                                            if (isFinishing() || isDestroyed()) {
                                                return;
                                            }
                                            onUpLoadToServerSuccess(userid, xfid);
                                        }

                                    }, new NetworkManager.FailedCallback() {
                                        @Override
                                        public void onFailed(String message) {
                                            Log.e("注册储存讯飞id失败", "onFailed: ");
                                            if (isFinishing() || isDestroyed()) {
                                                return;
                                            }
                                            onUploadToServerFailed();
                                        }
                                    });
                        } else {
                            onUploadToServerFailed();
                        }
                    }
                };
                uploadManager().put(item.picData, key, response, completionHandler, null);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                onUploadToServerFailed();
            }
        });
    }

    private void onUpLoadToServerSuccess(String userid, String xfid) {
        LocalShared.getInstance(mContext).addAccount(userid, xfid);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onUploadToServerFailed() {
        onReadFailed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (client != null) {
            client.disconnectBt();
            client.disconnect();
            client.setBluetoothListener(null);
        }
        removeBond(targetDevice);
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
        if (btHandler != null) {
            btHandler.removeCallbacksAndMessages(null);
            Thread thread = btHandler.getLooper().getThread();
            btHandler = null;
            if (thread instanceof HandlerThread) {
                ((HandlerThread) thread).quit();
            }
        }
    }

    public static boolean removeBond(BluetoothDevice device) {
        boolean success = false;
        try {
            Method removeBond_Method = BluetoothDevice.class.getMethod("removeBond");
            removeBond_Method.setAccessible(true);
            success = (Boolean) removeBond_Method.invoke(device);
            Log.d(TAG, "removeBond: " + success);
        } catch (Throwable e) {
            Log.e(TAG, "removeBond: " + success, e);
        }
        return success;
    }

    private Handler btHandler;

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

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
