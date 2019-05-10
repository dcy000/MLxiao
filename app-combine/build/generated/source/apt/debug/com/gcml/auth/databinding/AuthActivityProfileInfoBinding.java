package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivityProfileInfoBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tb_profile_info, 1);
        sViewsWithIds.put(R.id.cl_basic_info, 2);
        sViewsWithIds.put(R.id.iv_avatar, 3);
        sViewsWithIds.put(R.id.cl_item_name, 4);
        sViewsWithIds.put(R.id.tv_title_name, 5);
        sViewsWithIds.put(R.id.tv_name, 6);
        sViewsWithIds.put(R.id.iv_indicator_name, 7);
        sViewsWithIds.put(R.id.cl_item_age, 8);
        sViewsWithIds.put(R.id.tv_title_age, 9);
        sViewsWithIds.put(R.id.tv_age, 10);
        sViewsWithIds.put(R.id.iv_indicator_age, 11);
        sViewsWithIds.put(R.id.cl_item_sex, 12);
        sViewsWithIds.put(R.id.tv_title_sex, 13);
        sViewsWithIds.put(R.id.tv_sex, 14);
        sViewsWithIds.put(R.id.iv_indicator_sex, 15);
        sViewsWithIds.put(R.id.cl_item_height, 16);
        sViewsWithIds.put(R.id.tv_title_height, 17);
        sViewsWithIds.put(R.id.tv_height, 18);
        sViewsWithIds.put(R.id.iv_indicator_height, 19);
        sViewsWithIds.put(R.id.cl_item_weight, 20);
        sViewsWithIds.put(R.id.tv_title_weight, 21);
        sViewsWithIds.put(R.id.tv_weight, 22);
        sViewsWithIds.put(R.id.iv_indicator_weight, 23);
        sViewsWithIds.put(R.id.cl_item_blood, 24);
        sViewsWithIds.put(R.id.tv_title_blood, 25);
        sViewsWithIds.put(R.id.tv_blood, 26);
        sViewsWithIds.put(R.id.iv_indicator_blood, 27);
        sViewsWithIds.put(R.id.cl_item_wc, 28);
        sViewsWithIds.put(R.id.tv_title_wc, 29);
        sViewsWithIds.put(R.id.tv_wc, 30);
        sViewsWithIds.put(R.id.iv_indicator_wc, 31);
        sViewsWithIds.put(R.id.cl_other_info, 32);
        sViewsWithIds.put(R.id.tv_other_info, 33);
        sViewsWithIds.put(R.id.cl_item_phone, 34);
        sViewsWithIds.put(R.id.tv_title_phone, 35);
        sViewsWithIds.put(R.id.tv_phone, 36);
        sViewsWithIds.put(R.id.iv_indicator_phone, 37);
        sViewsWithIds.put(R.id.cl_item_id_card, 38);
        sViewsWithIds.put(R.id.tv_title_id_card, 39);
        sViewsWithIds.put(R.id.tv_id_card, 40);
        sViewsWithIds.put(R.id.iv_indicator_id_card, 41);
        sViewsWithIds.put(R.id.cl_item_device_id, 42);
        sViewsWithIds.put(R.id.tv_title_device_id, 43);
        sViewsWithIds.put(R.id.tv_device_id, 44);
        sViewsWithIds.put(R.id.iv_indicator_device_id, 45);
        sViewsWithIds.put(R.id.cl_item_address, 46);
        sViewsWithIds.put(R.id.tv_title_address, 47);
        sViewsWithIds.put(R.id.tv_address, 48);
        sViewsWithIds.put(R.id.iv_indicator_address, 49);
        sViewsWithIds.put(R.id.tv_item_title_device_id, 50);
        sViewsWithIds.put(R.id.tv_item_content_device_id, 51);
        sViewsWithIds.put(R.id.cl_health_info, 52);
        sViewsWithIds.put(R.id.tv_health_info, 53);
        sViewsWithIds.put(R.id.cl_item_sports, 54);
        sViewsWithIds.put(R.id.tv_title_sports, 55);
        sViewsWithIds.put(R.id.tv_sports, 56);
        sViewsWithIds.put(R.id.iv_indicator_sports, 57);
        sViewsWithIds.put(R.id.cl_item_smoke, 58);
        sViewsWithIds.put(R.id.tv_title_smoke, 59);
        sViewsWithIds.put(R.id.tv_smoke, 60);
        sViewsWithIds.put(R.id.iv_indicator_smoke, 61);
        sViewsWithIds.put(R.id.cl_item_eat, 62);
        sViewsWithIds.put(R.id.tv_title_eat, 63);
        sViewsWithIds.put(R.id.tv_eat, 64);
        sViewsWithIds.put(R.id.iv_indicator_eat, 65);
        sViewsWithIds.put(R.id.cl_item_drink, 66);
        sViewsWithIds.put(R.id.tv_title_drink, 67);
        sViewsWithIds.put(R.id.tv_drink, 68);
        sViewsWithIds.put(R.id.iv_indicator_drink, 69);
        sViewsWithIds.put(R.id.cl_item_desease, 70);
        sViewsWithIds.put(R.id.tv_title_desease, 71);
        sViewsWithIds.put(R.id.tv_desease, 72);
        sViewsWithIds.put(R.id.iv_indicator_desease, 73);
    }
    // views
    @NonNull
    public final android.support.constraint.ConstraintLayout clBasicInfo;
    @NonNull
    public final android.support.constraint.ConstraintLayout clHealthInfo;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemAddress;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemAge;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemBlood;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemDesease;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemDeviceId;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemDrink;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemEat;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemHeight;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemIdCard;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemName;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemPhone;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemSex;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemSmoke;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemSports;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemWc;
    @NonNull
    public final android.support.constraint.ConstraintLayout clItemWeight;
    @NonNull
    public final android.support.constraint.ConstraintLayout clOtherInfo;
    @NonNull
    public final android.widget.ImageView ivAvatar;
    @NonNull
    public final android.widget.ImageView ivIndicatorAddress;
    @NonNull
    public final android.widget.ImageView ivIndicatorAge;
    @NonNull
    public final android.widget.ImageView ivIndicatorBlood;
    @NonNull
    public final android.widget.ImageView ivIndicatorDesease;
    @NonNull
    public final android.widget.ImageView ivIndicatorDeviceId;
    @NonNull
    public final android.widget.ImageView ivIndicatorDrink;
    @NonNull
    public final android.widget.ImageView ivIndicatorEat;
    @NonNull
    public final android.widget.ImageView ivIndicatorHeight;
    @NonNull
    public final android.widget.ImageView ivIndicatorIdCard;
    @NonNull
    public final android.widget.ImageView ivIndicatorName;
    @NonNull
    public final android.widget.ImageView ivIndicatorPhone;
    @NonNull
    public final android.widget.ImageView ivIndicatorSex;
    @NonNull
    public final android.widget.ImageView ivIndicatorSmoke;
    @NonNull
    public final android.widget.ImageView ivIndicatorSports;
    @NonNull
    public final android.widget.ImageView ivIndicatorWc;
    @NonNull
    public final android.widget.ImageView ivIndicatorWeight;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final com.gcml.common.widget.toolbar.TranslucentToolBar tbProfileInfo;
    @NonNull
    public final android.widget.TextView tvAddress;
    @NonNull
    public final android.widget.TextView tvAge;
    @NonNull
    public final android.widget.TextView tvBlood;
    @NonNull
    public final android.widget.TextView tvDesease;
    @NonNull
    public final android.widget.TextView tvDeviceId;
    @NonNull
    public final android.widget.TextView tvDrink;
    @NonNull
    public final android.widget.TextView tvEat;
    @NonNull
    public final android.widget.TextView tvHealthInfo;
    @NonNull
    public final android.widget.TextView tvHeight;
    @NonNull
    public final android.widget.TextView tvIdCard;
    @NonNull
    public final android.widget.TextView tvItemContentDeviceId;
    @NonNull
    public final android.widget.TextView tvItemTitleDeviceId;
    @NonNull
    public final android.widget.TextView tvName;
    @NonNull
    public final android.widget.TextView tvOtherInfo;
    @NonNull
    public final android.widget.TextView tvPhone;
    @NonNull
    public final android.widget.TextView tvSex;
    @NonNull
    public final android.widget.TextView tvSmoke;
    @NonNull
    public final android.widget.TextView tvSports;
    @NonNull
    public final android.widget.TextView tvTitleAddress;
    @NonNull
    public final android.widget.TextView tvTitleAge;
    @NonNull
    public final android.widget.TextView tvTitleBlood;
    @NonNull
    public final android.widget.TextView tvTitleDesease;
    @NonNull
    public final android.widget.TextView tvTitleDeviceId;
    @NonNull
    public final android.widget.TextView tvTitleDrink;
    @NonNull
    public final android.widget.TextView tvTitleEat;
    @NonNull
    public final android.widget.TextView tvTitleHeight;
    @NonNull
    public final android.widget.TextView tvTitleIdCard;
    @NonNull
    public final android.widget.TextView tvTitleName;
    @NonNull
    public final android.widget.TextView tvTitlePhone;
    @NonNull
    public final android.widget.TextView tvTitleSex;
    @NonNull
    public final android.widget.TextView tvTitleSmoke;
    @NonNull
    public final android.widget.TextView tvTitleSports;
    @NonNull
    public final android.widget.TextView tvTitleWc;
    @NonNull
    public final android.widget.TextView tvTitleWeight;
    @NonNull
    public final android.widget.TextView tvWc;
    @NonNull
    public final android.widget.TextView tvWeight;
    // variables
    @Nullable
    private com.gcml.auth.ui.profile.ProfileInfoActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.profile.ProfileInfoViewModel mViewModel;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivityProfileInfoBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 74, sIncludes, sViewsWithIds);
        this.clBasicInfo = (android.support.constraint.ConstraintLayout) bindings[2];
        this.clHealthInfo = (android.support.constraint.ConstraintLayout) bindings[52];
        this.clItemAddress = (android.support.constraint.ConstraintLayout) bindings[46];
        this.clItemAge = (android.support.constraint.ConstraintLayout) bindings[8];
        this.clItemBlood = (android.support.constraint.ConstraintLayout) bindings[24];
        this.clItemDesease = (android.support.constraint.ConstraintLayout) bindings[70];
        this.clItemDeviceId = (android.support.constraint.ConstraintLayout) bindings[42];
        this.clItemDrink = (android.support.constraint.ConstraintLayout) bindings[66];
        this.clItemEat = (android.support.constraint.ConstraintLayout) bindings[62];
        this.clItemHeight = (android.support.constraint.ConstraintLayout) bindings[16];
        this.clItemIdCard = (android.support.constraint.ConstraintLayout) bindings[38];
        this.clItemName = (android.support.constraint.ConstraintLayout) bindings[4];
        this.clItemPhone = (android.support.constraint.ConstraintLayout) bindings[34];
        this.clItemSex = (android.support.constraint.ConstraintLayout) bindings[12];
        this.clItemSmoke = (android.support.constraint.ConstraintLayout) bindings[58];
        this.clItemSports = (android.support.constraint.ConstraintLayout) bindings[54];
        this.clItemWc = (android.support.constraint.ConstraintLayout) bindings[28];
        this.clItemWeight = (android.support.constraint.ConstraintLayout) bindings[20];
        this.clOtherInfo = (android.support.constraint.ConstraintLayout) bindings[32];
        this.ivAvatar = (android.widget.ImageView) bindings[3];
        this.ivIndicatorAddress = (android.widget.ImageView) bindings[49];
        this.ivIndicatorAge = (android.widget.ImageView) bindings[11];
        this.ivIndicatorBlood = (android.widget.ImageView) bindings[27];
        this.ivIndicatorDesease = (android.widget.ImageView) bindings[73];
        this.ivIndicatorDeviceId = (android.widget.ImageView) bindings[45];
        this.ivIndicatorDrink = (android.widget.ImageView) bindings[69];
        this.ivIndicatorEat = (android.widget.ImageView) bindings[65];
        this.ivIndicatorHeight = (android.widget.ImageView) bindings[19];
        this.ivIndicatorIdCard = (android.widget.ImageView) bindings[41];
        this.ivIndicatorName = (android.widget.ImageView) bindings[7];
        this.ivIndicatorPhone = (android.widget.ImageView) bindings[37];
        this.ivIndicatorSex = (android.widget.ImageView) bindings[15];
        this.ivIndicatorSmoke = (android.widget.ImageView) bindings[61];
        this.ivIndicatorSports = (android.widget.ImageView) bindings[57];
        this.ivIndicatorWc = (android.widget.ImageView) bindings[31];
        this.ivIndicatorWeight = (android.widget.ImageView) bindings[23];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tbProfileInfo = (com.gcml.common.widget.toolbar.TranslucentToolBar) bindings[1];
        this.tvAddress = (android.widget.TextView) bindings[48];
        this.tvAge = (android.widget.TextView) bindings[10];
        this.tvBlood = (android.widget.TextView) bindings[26];
        this.tvDesease = (android.widget.TextView) bindings[72];
        this.tvDeviceId = (android.widget.TextView) bindings[44];
        this.tvDrink = (android.widget.TextView) bindings[68];
        this.tvEat = (android.widget.TextView) bindings[64];
        this.tvHealthInfo = (android.widget.TextView) bindings[53];
        this.tvHeight = (android.widget.TextView) bindings[18];
        this.tvIdCard = (android.widget.TextView) bindings[40];
        this.tvItemContentDeviceId = (android.widget.TextView) bindings[51];
        this.tvItemTitleDeviceId = (android.widget.TextView) bindings[50];
        this.tvName = (android.widget.TextView) bindings[6];
        this.tvOtherInfo = (android.widget.TextView) bindings[33];
        this.tvPhone = (android.widget.TextView) bindings[36];
        this.tvSex = (android.widget.TextView) bindings[14];
        this.tvSmoke = (android.widget.TextView) bindings[60];
        this.tvSports = (android.widget.TextView) bindings[56];
        this.tvTitleAddress = (android.widget.TextView) bindings[47];
        this.tvTitleAge = (android.widget.TextView) bindings[9];
        this.tvTitleBlood = (android.widget.TextView) bindings[25];
        this.tvTitleDesease = (android.widget.TextView) bindings[71];
        this.tvTitleDeviceId = (android.widget.TextView) bindings[43];
        this.tvTitleDrink = (android.widget.TextView) bindings[67];
        this.tvTitleEat = (android.widget.TextView) bindings[63];
        this.tvTitleHeight = (android.widget.TextView) bindings[17];
        this.tvTitleIdCard = (android.widget.TextView) bindings[39];
        this.tvTitleName = (android.widget.TextView) bindings[5];
        this.tvTitlePhone = (android.widget.TextView) bindings[35];
        this.tvTitleSex = (android.widget.TextView) bindings[13];
        this.tvTitleSmoke = (android.widget.TextView) bindings[59];
        this.tvTitleSports = (android.widget.TextView) bindings[55];
        this.tvTitleWc = (android.widget.TextView) bindings[29];
        this.tvTitleWeight = (android.widget.TextView) bindings[21];
        this.tvWc = (android.widget.TextView) bindings[30];
        this.tvWeight = (android.widget.TextView) bindings[22];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.presenter == variableId) {
            setPresenter((com.gcml.auth.ui.profile.ProfileInfoActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.ui.profile.ProfileInfoViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.profile.ProfileInfoActivity Presenter) {
        this.mPresenter = Presenter;
    }
    @Nullable
    public com.gcml.auth.ui.profile.ProfileInfoActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.ui.profile.ProfileInfoViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.profile.ProfileInfoViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static AuthActivityProfileInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfileInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivityProfileInfoBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_profile_info, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivityProfileInfoBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfileInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_profile_info, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivityProfileInfoBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfileInfoBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_profile_info_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivityProfileInfoBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}