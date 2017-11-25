package com.example.han.referralproject.shopping;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.recyclerview.AddAppoActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.squareup.picasso.Picasso;

public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    Button mButton;
    int i = 1;
    NDialog1 dialog1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);


        dialog1 = new NDialog1(GoodDetailActivity.this);


        Intent intent = getIntent();
        Goods goods = (Goods) intent.getSerializableExtra("goods");

        mImageView1 = (ImageView) findViewById(R.id.goods_image);
        mImageView2 = (ImageView) findViewById(R.id.add_mount);
        mImageView3 = (ImageView) findViewById(R.id.reduce_mount);
        mImageView4 = (ImageView) findViewById(R.id.icon_back);

        mButton = (Button) findViewById(R.id.shopping);

        mTextView = (TextView) findViewById(R.id.goods_name);
        mTextView1 = (TextView) findViewById(R.id.goods_price);
        mTextView2 = (TextView) findViewById(R.id.goods_mount);
        mTextView3 = (TextView) findViewById(R.id.goods_sum_price);


        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mButton.setOnClickListener(this);

        mTextView.setText(goods.getGoodsname());
        mTextView1.setText(goods.getGoodsprice());

        Picasso.with(this)
                .load(ConstantData.BASE_URL + "/referralProject/" + goods.getGoodsimage())
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(mImageView1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_mount:

                i++;
                mTextView2.setText(i + "");
                int sumPrice = Integer.parseInt(mTextView1.getText().toString()) * i;
                mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice + ""));
                break;

            case R.id.reduce_mount:

                i--;

                if (i >= 1) {

                    mTextView2.setText(i + "");
                    int sumPrice1 = Integer.parseInt(mTextView1.getText().toString()) * i;
                    mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice1 + ""));


                } else {
                    i = 1;
                }
                break;
            case R.id.icon_back:


                finish();
                break;
            case R.id.shopping:
                ShowNormals();

                break;
        }
    }

    public void ShowNormals() {
        dialog1.setMessageCenter(true)
                .setMessage("是否确认购买该商品")
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {

                        }

                    }
                }).create(NDialog.CONFIRM).show();

    }

}
