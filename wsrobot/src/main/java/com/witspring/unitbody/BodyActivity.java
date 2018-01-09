package com.witspring.unitbody;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsbodyActivityBodyBinding;
import com.witspring.model.Constants;
import com.witspring.model.entity.Member;
import com.witspring.unitbody.contract.BodyContract;
import com.witspring.util.BitmapUtil;
import com.witspring.util.CommUtil;
import com.witspring.util.DensityUtil;
import com.witspring.view.TouchImageView;
import com.witspring.view.pageflow.PageScrollView;

public class BodyActivity extends BaseActivity implements BodyContract.View {

    private WsbodyActivityBodyBinding binding;
    private Member member;
    private boolean isMan;
    private SparseIntArray manPartIds, womanPartIds;
    private BodyContract.Presenter presenter;
    private String[] partNames = {"头部", "颈部", "胸部", "腹部", "生殖器", "下肢", "上肢", "枕部", "后颈部", "背部", "臀部", "下肢", "上肢", "全身"};
    public static final int PRESSED_PART_NULL = -1;
    private int pressedPart = PRESSED_PART_NULL;
    private PageScrollView.TagStyle tagStyle;

    private final int[] manPartX1 = {163, 159, 112, 132, 116, 116, 0, 163, 159, 113, 116, 116, 0};
    private final int[] manPartY1 = {0, 147, 186, 367, 492, 603, 202, 0, 144, 195, 490, 603, 206};
    private final int[] manPartX2 = {284, 289, 334, 318, 332, 332, 448, 282, 289, 335, 332, 332, 448};
    private final int[] manPartY2 = {157, 197, 360, 499, 604, 1090, 643, 157, 194, 501, 602, 1090, 647};
    private final int[] womanPartX1 = {116, 131, 94, 98, 77, 77, 0, 115, 115, 94, 77, 77, 0};
    private final int[] womanPartY1 = {0, 160, 199, 360, 492, 591, 215, 0, 153, 199, 492, 591, 214};
    private final int[] womanPartX2 = {253, 238, 276, 272, 293, 293, 370, 254, 255, 276, 293, 293, 370};
    private final int[] womanPartY2 = {158, 205, 360, 492, 592, 1090, 631, 152, 208, 492, 592, 1090, 630};

    private int[] manPartImgs = {R.mipmap.wsbody_man_head, R.mipmap.wsbody_man_neck, R.mipmap.wsbody_man_chest,
            R.mipmap.wsbody_man_abdomen, R.mipmap.wsbody_man_genital, R.mipmap.wsbody_man_lower, R.mipmap.wsbody_man_upper,
            R.mipmap.wsbody_man_head_back, R.mipmap.wsbody_man_neck_back, R.mipmap.wsbody_man_back, R.mipmap.wsbody_man_butt, R.mipmap.wsbody_man_lower_back, R.mipmap.wsbody_man_upper_back};
    private int[] womanPartImgs = {R.mipmap.wsbody_woman_head, R.mipmap.wsbody_woman_neck, R.mipmap.wsbody_woman_chest,
            R.mipmap.wsbody_woman_abdomen, R.mipmap.wsbody_woman_genital, R.mipmap.wsbody_woman_lower, R.mipmap.wsbody_woman_upper,
            R.mipmap.wsbody_woman_head_back, R.mipmap.wsbody_woman_neck_back, R.mipmap.wsbody_woman_back, R.mipmap.wsbody_woman_butt, R.mipmap.wsbody_woman_lower_back, R.mipmap.wsbody_woman_upper_back};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.wsbody_activity_body);
        setToolbar(binding.toolbar, "症状自查");
        member = (Member) getIntent().getSerializableExtra("member");
        isMan = member.getSex() == Constants.GENDER_MAN;
        Button btnRight = binding.toolbar.findViewById(R.id.btnRight);
        btnRight.setText(member.getName());
        presenter = new BodyContract.Presenter(this);
        initBody();
        initSymptoms();
    }

    private void initBody() {
        if (!isMan) {
            binding.touchView.setImageResource(R.mipmap.wsbody_front_woman);
        }
        binding.touchView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {

            @Override
            public void onMove() {}

            @Override
            public void onDrag(int x, int y) {
                if (pressedPart != PRESSED_PART_NULL && binding.ivCheck.getVisibility() == View.VISIBLE) {
                    binding.ivCheck.setImageMatrix(binding.touchView.getImageMatrix());
                }
            }

            @Override
            public void onClick(int x, int y) {
                pressedPart = getPressedPart(x, y);
                gotoBodyPart();
            }

            @Override
            public void onFling(boolean isLeft) {
                ctvSwitch(null);
            }

            @Override
            public void onLongClick(int x, int y) {
                pressedPart = getPressedPart(x, y);
            }

            @Override
            public void onTouchCancel() {
                if (binding.ivCheck.getVisibility() == View.VISIBLE) {
                    binding.ivCheck.setVisibility(View.GONE);
                    gotoBodyPart();
                }
            }
        });
        presenter.topLevelOrgans(isMan);
    }

    private void initSymptoms() {
        int margin = DensityUtil.dip2px(this, 4);
        tagStyle = PageScrollView.TagStyle.build().backgroundResId(R.drawable.wsbody_selector_tag_comm_symptom)
                .textColorState(this.getResources().getColorStateList(R.color.wsbody_selector_comm_symptom))
                .textSize(getResources().getDimensionPixelOffset(R.dimen.ws_font_medium))
                .padding(3 * margin, (int)(1.2f * margin), 3 * margin, (int)(1.2f * margin))
                .margin(2 * margin, (int)(1.5f * margin), 2 * margin, (int)(1.5f * margin));
        binding.pageView.setOnTagClickListener(new PageScrollView.OnTagClickListener() {
            @Override
            public void onClick(View view, Object obj) {
                Intent intent = new Intent(getContext(), InquiryActivity.class);
                intent.putExtra("symptom", (String)obj);
                intent.putExtra("member", member);
                startActivity(intent);
            }
        });
        presenter.commSymptoms(member.getSex(), member.getAgeMonths());
    }

    private void gotoBodyPart() {
        if (pressedPart != PRESSED_PART_NULL) {
            SparseIntArray partIds = isMan ? manPartIds : womanPartIds;
            if (partIds != null) {
                Intent intent = new Intent(getContext(), BodyPartActivity.class);
                intent.putExtra("partIndex", pressedPart);
                intent.putExtra("partId", partIds.get(pressedPart));
                intent.putExtra("member", member);
                startActivity(intent);
            } else {
                showDialog("数据请求失败，是否重新加载？", "重新加载", "取消", new DialogActionListener() {
                    @Override
                    public void onPositive() {
                        binding.ivCheck.setVisibility(View.GONE);
                        presenter.topLevelOrgans(isMan);
                    }

                    @Override
                    public void onNegative() {
                        binding.ivCheck.setVisibility(View.GONE);
                        presenter.topLevelOrgans(isMan);
                    }
                });
            }
            pressedPart = PRESSED_PART_NULL;
        }
    }

    public void ctvSwitch(View view) {
        boolean isFront = binding.ctvSwitch.isChecked();
        if (isFront) {
            binding.touchView.setImageResource(isMan ? R.mipmap.wsbody_back_man : R.mipmap.wsbody_back_woman);
        } else {
            binding.touchView.setImageResource(isMan ? R.mipmap.wsbody_front_man : R.mipmap.wsbody_front_woman);
        }
        binding.ctvSwitch.setChecked(!isFront);
        binding.touchView.resetZoom();
    }

    public void ctvAll(View view) {
        pressedPart = 13;
        gotoBodyPart();
    }

    @Override
    public void setBodyPartIds(Integer[] ids) {
        if (CommUtil.notEmpty(ids)) {
            SparseIntArray partIds = new SparseIntArray(ids.length);
            for (int i = 0; i < ids.length; i++) {
                partIds.put(i, ids[i]);
            }
            if (isMan) {
                manPartIds = partIds;
            } else {
                womanPartIds = partIds;
            }
        }
    }

    @Override
    public void setCommSymptoms(String[] symptoms) {
        if (CommUtil.notEmpty(symptoms)) {
            binding.pageView.setData(symptoms, tagStyle);
        }
    }

    private int getPressedPart(int x, int y) {
        Matrix matrix = binding.touchView.getImageMatrix();
        float[] values = new float[9];
        matrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        Bitmap bitmap = BitmapUtil.convertBitmap(binding.touchView);
        int pixel = bitmap.getPixel(x,y);
        bitmap.recycle();
        int pressedPart = PRESSED_PART_NULL;
        if (pixel != 0) {
            float density = getResources().getDisplayMetrics().density / 2;
            int[] partX1 = isMan ? manPartX1 : womanPartX1;
            int[] partX2 = isMan ? manPartX2 : womanPartX2;
            int[] partY1 = isMan ? manPartY1 : womanPartY1;
            int[] partY2 = isMan ? manPartY2 : womanPartY2;
            int[] partImgs = isMan? manPartImgs : womanPartImgs;
            int start = binding.ctvSwitch.isChecked() ? 0 : 7;
            for (int i = start; i < partNames.length - 1; i++) {
                float x1 = partX1[i] * density * scale + transX;
                float x2 = partX2[i] * density * scale + transX;
                if (x >= x1 && x <= x2) {
                    float y1 = partY1[i] * density * scale + transY;
                    float y2 = partY2[i] * density * scale + transY;
                    if (y >= y1 && y <= y2) {
                        pressedPart = i;
                        binding.ivCheck.setImageResource(partImgs[i]);
                        binding.ivCheck.setImageMatrix(matrix);
                        binding.ivCheck.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
        return pressedPart;
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.ivCheck.setVisibility(View.GONE);
    }

}
