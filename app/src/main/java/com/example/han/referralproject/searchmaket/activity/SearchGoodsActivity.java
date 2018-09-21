package com.example.han.referralproject.searchmaket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.adapter.RecommendAdapter;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.network.RecommendRepository;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchGoodsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvSearchGoods;
    /**
     * 请输入商品名
     */
    private EditText mEtGoodName;
    /**
     * 取消
     */
    private TextView mTvCancle;
    private RecyclerView mRvGoods;
    private RecommendRepository recommendRepository = new RecommendRepository();
    private RecommendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);
        initView();
    }

    private void initView() {
        mIvSearchGoods = (ImageView) findViewById(R.id.iv_search_goods);
        mIvSearchGoods.setOnClickListener(this);
        mEtGoodName = (EditText) findViewById(R.id.et_good_name);
        mTvCancle = (TextView) findViewById(R.id.tv_cancle);
        mTvCancle.setOnClickListener(this);
        mRvGoods = (RecyclerView) findViewById(R.id.rv_goods);

        GridLayoutManager layout = new GridLayoutManager(this, 3);
//        rvCommendGoods.addItemDecoration(new GridDividerItemDecoration(UiUtils.pt(108), 0));
        mRvGoods.setLayoutManager(layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_search_goods:
                initData();
                break;
            case R.id.tv_cancle:
                finish();
                break;
        }
    }

    private void initData() {
        LoadingDialog dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        recommendRepository.searchGoodsByName(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<GoodBean>>() {

                    @Override
                    public void onNext(List<GoodBean> goodBeans) {
                        if (adapter == null) {
                            adapter = new RecommendAdapter(com.gcml.common.business.R.layout.layout_recommend_item, goodBeans);
                            mRvGoods.setAdapter(adapter);
                        }
                        adapter.replaceData(goodBeans);
                    }
                });

    }
}
