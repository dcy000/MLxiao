package com.gcml.common.widget.base.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.business.R;

public class DialogImage extends BaseDialog{
    private ImageView image;
    private TextView description;
    public DialogImage(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }



    public DialogImage(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogImage(Context context) {
        super(context);
        initView();
    }

    public DialogImage(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.utils_dialog_image, null);
        image=dialogView.findViewById(R.id.iv_image);
        description=dialogView.findViewById(R.id.tv_description);
        description.setTextIsSelectable(true);
        setContentView(dialogView);
    }

    public void setImage(int img) {
        image.setImageResource(img);
    }

    public void setImage(Bitmap bitmap){
        image.setImageBitmap(bitmap);
    }
    public void setImage(Uri uri){
        image.setImageURI(uri);
    }
    public void setDescription(CharSequence charSequence) {
        description.setText(charSequence);
    }

}
