package com.gcml.old;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.recommend.adapter.RecommendAdapter;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.network.RecommendRepository;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.mall.R;
import com.sjtu.yifei.annotation.Route;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/mall/search/goods/activity")
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
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);
        initView();
        initData();
    }

    private void initView() {
        mIvSearchGoods = (ImageView) findViewById(R.id.iv_search_goods);
        mIvSearchGoods.setOnClickListener(this);
        mEtGoodName = (EditText) findViewById(R.id.et_good_name);
        mEtGoodName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    initData();
                }
                return false;
            }
        });

        mTvCancle = (TextView) findViewById(R.id.tv_cancle);
        mTvCancle.setOnClickListener(this);
        mRvGoods = (RecyclerView) findViewById(R.id.rv_goods);

        GridLayoutManager layout = new GridLayoutManager(this, 3);
//        rvCommendGoods.addItemDecoration(new GridDividerItemDecoration(UiUtils.pt(108), 0));
        mRvGoods.setLayoutManager(layout);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_goods) {
            initData();

        } else if (i == R.id.tv_cancle) {
            finish();

        } else {
        }
    }

    private void initData() {
        if (dialog == null) {
            dialog = new LoadingDialog.Builder(this)
                    .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在加载")
                    .create();
        }

        String GoodName = mEtGoodName.getText().toString().trim();
        recommendRepository.searchGoodsByName(GoodName)
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