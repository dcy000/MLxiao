package com.gcml.module_yzn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.CaseInputBean;
import com.gcml.module_yzn.bean.FenLeiInfoOutBean;
import com.gcml.module_yzn.repository.YZNRepository;
import com.gcml.module_yzn.util.BASE64Encoder;
import com.gcml.module_yzn.util.MD5Util;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_yzn.constant.Global.APP_ID;
import static com.gcml.module_yzn.constant.Global.APP_KEY;

@Route(path = "/module/yzn/zenduan/activity")
public class MedicalEncyclopediaActivity extends ToolbarBaseActivity {

    private EditText tvSearch;
    private RecyclerView items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_medical_encyclopedia);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        tvSearch = findViewById(R.id.tv_search);
        tvSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String quary = tvSearch.getText().toString().trim();
                if (TextUtils.isEmpty(quary)) {
                    return false;
                }
                startActivity(new Intent(this, ZenDuanActivity.class)
                        .putExtra("inputText", quary));
                return true;
            }
            return false;
        });

        TranslucentToolBar tb = findViewById(R.id.tb_medical_encyclopedia);
        tb.setData("医 疗 百 科", R.drawable.common_icon_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });

        setWifiLevel(tb);

        items = findViewById(R.id.rv_fenlei_items);
    }

    private void updateView() {
        items.setAdapter(new BaseQuickAdapter<FenLeiInfoOutBean, BaseViewHolder>(R.layout.layout_item_parent, parentItems) {
            @Override
            protected void convert(BaseViewHolder helper, FenLeiInfoOutBean item) {
                helper.setText(R.id.tv_group_name, item.group);

                RecyclerView childItems = helper.getView(R.id.rv_child_items);

                BaseQuickAdapter<FenLeiInfoOutBean.ItemBean, BaseViewHolder> adapter = new BaseQuickAdapter<FenLeiInfoOutBean.ItemBean, BaseViewHolder>(R.layout.layout_item_child, item.datas) {

                    @Override
                    protected void convert(BaseViewHolder helper, FenLeiInfoOutBean.ItemBean item) {
                        helper.setText(R.id.tv_child_name, item.name);
                    }
                };
                childItems.setAdapter(adapter);
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    FenLeiInfoOutBean.ItemBean childItem = item.datas.get(position);
                    Intent intent = new Intent(MedicalEncyclopediaActivity.this, YiZhiTangDetailActivity.class);
                    intent.putExtra("itemUrl", childItem.link);
                    startActivity(intent);
                });
                childItems.setLayoutManager(new GridLayoutManager(MedicalEncyclopediaActivity.this, 4));
            }
        });

        items.setLayoutManager(new LinearLayoutManager(this));
    }

    List<FenLeiInfoOutBean> parentItems = new ArrayList<>();

    private void initData() {
        CaseInputBean inputBean = new CaseInputBean();
        inputBean.userId = UserSpHelper.getUserId();
        inputBean.url = "news";

        requestFeiLeiInfo(inputBean);
    }

    YZNRepository repository = new YZNRepository();
    LoadingDialog dialog;

    private void requestFeiLeiInfo(CaseInputBean inputBean) {
        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.classificationInfo(APP_ID, currentTime, param, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doOnTerminate(() -> dialog.dismiss())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<FenLeiInfoOutBean>>() {
                    @Override
                    public void onNext(List<FenLeiInfoOutBean> data) {
                        super.onNext(data);
                        parentItems = data;
                        updateView();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.dismiss();
                    }
                });

    }
}
