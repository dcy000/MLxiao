package com.witspring.unitdisease.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.witspring.mlrobot.R;
import com.witspring.model.entity.Medicine;

import kale.adapter.item.AdapterItem;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */

public class MedicineItem implements AdapterItem<Medicine> {
    private SimpleDraweeView ivContent;
    private TextView tvName;

    @Override
    public int getLayoutResId() {
        return R.layout.wsdise_item_medcine;
    }

    @Override
    public void bindViews(View view) {
        ivContent = view.findViewById(R.id.wsdise_iv_content);
        tvName = view.findViewById(R.id.wsdise_tv_item_name);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void handleData(Medicine medicine, int i) {
        ivContent.setImageURI(Uri.parse(medicine.getThumbnailImgUrl()));
        tvName.setText(medicine.getName());
        if (medicine.getIsPrescription() != Medicine.PRESCRIPTION_NULL) {
            Drawable drawable = ivContent.getContext().getResources().getDrawable(medicine.getIsPrescription() == Medicine.PRESCRIPTION_NO ?
                    R.mipmap.wsdise_tag_no_prescription : R.mipmap.wsdise_tag_prescription);
            drawable.setBounds(0, 0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvName.setCompoundDrawables(drawable,null, null, null);
        } else {
            tvName.setCompoundDrawables(null, null, null, null);
        }
    }
}
