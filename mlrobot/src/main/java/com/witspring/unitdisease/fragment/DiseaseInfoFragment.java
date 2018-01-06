package com.witspring.unitdisease.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.witspring.base.BaseFragment;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsdiseFragmentDiseaseInfoBinding;
import com.witspring.model.entity.DiseaseImg;
import com.witspring.unitdisease.DiseaseActivity;
import com.witspring.util.CommUtil;
import com.witspring.util.DensityUtil;
import com.witspring.util.StringUtil;

import java.util.List;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseInfoFragment extends BaseFragment {
    private WsdiseFragmentDiseaseInfoBinding binding;
    private DiseaseActivity mActivity;
    private List<DiseaseImg> imgList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (DiseaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.wsdise_fragment_disease_info, container, false);
        View view = binding.getRoot();
        initViews();
        return view;
    }

    private void initViews() {
        binding.wsdiseTvInfo.setText(dealContentForShowClean(mActivity.getmDisease().getInfo()));
        if (CommUtil.notEmpty(mActivity.getmDisease().getDiseasePhotos())) {
            binding.wsdiseLlHead.setVisibility(View.VISIBLE);
            imgList = mActivity.getmDisease().getDiseasePhotos();
            for (int i = 0; i < imgList.size(); i++) {
                final SimpleDraweeView imageView = new SimpleDraweeView(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width = DensityUtil.getWindowWidth(activity)/3;
                lp.height = DensityUtil.getWindowWidth(activity)/5;
                lp.setMargins(DensityUtil.dip2px(activity, 5),DensityUtil.dip2px(activity, 0),DensityUtil.dip2px(activity, 5),DensityUtil.dip2px(activity, 0));
                imageView.setLayoutParams(lp);
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setControllerListener(new BaseControllerListener<ImageInfo>() {
                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable anim) {
//                                imageView.setOnClickListener(imgclickListener);
                            }
                            @Override
                            public void onFailure(String id, Throwable throwable) {
                                imageView.setImageURI(Uri.parse("res:///" + R.mipmap.ws_no_img));
                            }
                        }).setUri(Uri.parse(imgList.get(0).getPhoto())).build();
                imageView.setTag(i);
                imageView.setController(controller);
                binding.wsdiseLlPhotos.addView(imageView);
            }

            if (imgList.size() >= 3) {
                binding.wsdiseTvArrLeft.setVisibility(View.VISIBLE);
                binding.wsdiseTvArrRight.setVisibility(View.VISIBLE);
            }
        } else {
            binding.wsdiseLlHead.setVisibility(View.GONE);
        }
    }

    public String dealContentForShowClean(String content){
        if (StringUtil.isTrimBlank(content)) {
            return "暂无数据";
        }
        String textContent = content.trim();
        while (textContent.startsWith("　")) {//这里判断是不是全角空格
            textContent = textContent.substring(1, textContent.length()).trim();
        }
        String strResult = "        " + textContent;
        return strResult;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
