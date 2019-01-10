package com.gcml.common.gzq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.repository.MusicRepository;
import com.gcml.common.repository.entity.FingerBean;
import com.gcml.common.utils.display.ImageUtils;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.fingerprint.Fingerprint_WeiEr_PresenterImp;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import com.gcml.common.component.R;
public class TestFingerActivity extends AppCompatActivity implements IView {
    private Fingerprint_WeiEr_PresenterImp fingerprintWeiErPresenterImp;
    private byte[] finger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_xien);
        BluetoothClientManager.init(this);
        RetrofitUrlManager.getInstance().putDomain("abcd", "http://192.168.1.126:8080/");
        new MusicRepository()
                .getFingers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<FingerBean>, List<byte[]>>() {
                    @Override
                    public List<byte[]> apply(List<FingerBean> fingerBeans) throws Exception {
                        List<byte[]> bytes = new ArrayList<>();
                        if (finger != null) {
                            bytes.add(finger);
                        }
                        for (FingerBean fingerBean : fingerBeans) {
                            byte[] bytes1 = ByteUtils.stringToBytes(fingerBean.getFingerPrint());
                            bytes.add(bytes1);
                        }
                        return bytes;
                    }
                }).subscribe(new Consumer<List<byte[]>>() {
            @Override
            public void accept(List<byte[]> bytes) throws Exception {
                fingerprintWeiErPresenterImp = new Fingerprint_WeiEr_PresenterImp(TestFingerActivity.this,
                        new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC,
                                "zjwellcom"), bytes);
            }
        });


    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            String data = datas[0];
            if (data.equals("input")) {
                finger = ByteUtils.stringToBytes(datas[1]);
                fingerprintWeiErPresenterImp.addByte(finger);
            } else if (data.equals("validate")) {

            } else if (data.equals("image")) {
                ((ImageView) findViewById(R.id.image)).setImageBitmap(ImageUtils.convertStringToIcon(datas[1]));
            }
        }
    }

    private static final String TAG = "TestFingerActivity";

    @Override
    public void updateState(String state) {
        Log.e(TAG, "updateState: " + state);
    }

    @Override
    public Context getThisContext() {

        return this;
    }

    public void pipei(View view) {
        fingerprintWeiErPresenterImp.validateFinger();
    }

    public void luru(View view) {
        fingerprintWeiErPresenterImp.collectFingers();
    }
}
