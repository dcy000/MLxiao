package com.gcml.module_auth_hospital.ui.register;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.Province;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class IDCardNumberRegisterInfoActivity extends BaseActivity implements View.OnClickListener {
    private TranslucentToolBar translucentToolBar;
    private String number;
    private EditText etRegisterName;
    /**
     * 请输入您的民族
     */
    private TextView etRegisterMinzu;
    /**
     * 身份证号码
     */
    private TextView etRegisterIdcrad;
    private TextView etRegisterNowAddress;
    /**
     * 请输入您的详细地址
     */
    private EditText etRegisterDetailAddress;
    /**
     * 下一步
     */
    private TextView tvAuthNext;
    private TextView authTvMan;
    private TextView authTvWoman;
    private boolean faild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_number_register_info);
        initExtra();
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initExtra() {
        Intent data = getIntent();
        if (data != null) {
            number = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER);
        }
    }

    private void initView() {
        translucentToolBar = (TranslucentToolBar) findViewById(R.id.auth_idcard_numer_register_info_tb);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterMinzu = findViewById(R.id.et_register_minzu);
        etRegisterMinzu.setOnClickListener(this);
        authTvMan = (TextView) findViewById(R.id.auth_tv_man);
        authTvMan.setOnClickListener(this);
        authTvWoman = (TextView) findViewById(R.id.auth_tv_woman);
        authTvWoman.setOnClickListener(this);
        etRegisterIdcrad = findViewById(R.id.et_register_idcrad);
        etRegisterNowAddress = findViewById(R.id.et_register_now_address);
        etRegisterNowAddress.setOnClickListener(this);
        etRegisterDetailAddress = (EditText) findViewById(R.id.et_register_detail_address);
        tvAuthNext = (TextView) findViewById(R.id.tv_auth_next);
        tvAuthNext.setOnClickListener(this);

        etRegisterIdcrad.setText(number);
        etRegisterMinzu.setText("汉族");

        authTvMan.setSelected(true);
        authTvWoman.setSelected(false);

        translucentToolBar.setData("账 号 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });

                    }
                });


        setWifiLevel(translucentToolBar);

        initJsonData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.et_register_minzu) {
            minzu();
        } else if (id == R.id.auth_tv_man) {
            authTvMan.setSelected(true);
            authTvWoman.setSelected(false);
        } else if (id == R.id.auth_tv_woman) {
            authTvMan.setSelected(false);
            authTvWoman.setSelected(true);
        } else if (id == R.id.et_register_now_address) {
            if (faild) {
                return;
            }
            showPickerView();
        } else if (id == R.id.tv_auth_next) {
            register();
        }
    }

    private void minzu() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String nation = (String) getNationItems().get(options1);
                etRegisterMinzu.setText(nation);
            }
        })
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();

        pvOptions.setPicker(getNationItems());
        pvOptions.setSelectOptions(125);
        pvOptions.show();
    }

    private List getNationItems() {
        return Arrays.asList(
                "汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族",
                "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族", "土家族", "哈尼族", "哈萨克族",
                "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族",
                "景颇族", "柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛难族",
                "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
                "崩龙族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"
        );
    }

    UserRepository repository = new UserRepository();

    private void register() {
        String deviceId = Utils.getDeviceId(getApplicationContext().getContentResolver());
        String name = etRegisterName.getText().toString().trim();
        String sex = authTvMan.isSelected() ? "男" : "女";

        String address = etRegisterNowAddress.getText().toString().trim();
        String detailName = etRegisterDetailAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort("请输入地址");
            return;
        }
        if (TextUtils.isEmpty(detailName)) {
            ToastUtils.showShort("请输入详细地址");
            return;
        }
        repository.signUpByIdCard(deviceId, name, sex, number, address + detailName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在注册...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        ToastUtils.showLong("身份证注册成功");
                        CC.obtainBuilder("com.gcml.auth.face2.signup")
                                .build()
                                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                                    @Override
                                    public void onResult(CC cc, CCResult result) {
                                        if (result.isSuccess()) {
//                                            login(deviceId);
                                            startActivity(new Intent(IDCardNumberRegisterInfoActivity.this, RegisterSuccessActivity.class)
                                                    .putExtra("idcard", number));
                                        } else {
                                            toLogin();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        String message = throwable.getMessage();
                        ToastUtils.showShort(message);
                    }
                });
    }


    private void toHome() {
        finish();
//        startActivity(new Intent(this, RegisterSuccessActivity.class));
        CC.obtainBuilder(IConstant.KEY_INUIRY_ENTRY).build().callAsync();
    }

    private void toLogin() {
        //关闭 注册时的 扫面sfz和信息录入页面
        finish();
        startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
    }


    private List<Province> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                etRegisterNowAddress.setText(tx);

            }
        })

                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private Observable<String> jsonData() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String provincesArr = Utils.readTextFromAssetFile(IDCardNumberRegisterInfoActivity.this.getApplicationContext(), "cities.json");
                emitter.onNext(provincesArr);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private void initJsonData() {//解析数据
        jsonData().observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("");
                    }
                })
                .map(new Function<String, List<Province>>() {
                    @Override
                    public List<Province> apply(String provincesArr) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(provincesArr, new TypeToken<List<Province>>() {
                        }.getType());
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<Province>>() {

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        faild = true;
                        dismissLoading();
                    }

                    @Override
                    public void onNext(List<Province> provinces) {
                        super.onNext(provinces);
                        options1Items = provinces;
                        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
                            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                            for (int c = 0; c < options1Items.get(i).getCities().size(); c++) {//遍历该省份的所有城市
                                String cityName = options1Items.get(i).getCities().get(c).getName();
                                cityList.add(cityName);//添加城市
                                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                                city_AreaList.addAll(options1Items.get(i).getCities().get(c).getCounties());
                                province_AreaList.add(city_AreaList);//添加该省所有地区数据
                            }

                            options2Items.add(cityList);
                            options3Items.add(province_AreaList);
                        }
                        dismissLoading();
                    }
                });


    }
}
