package com.gcml.family.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.List;

import timber.log.Timber;

/**
 * 作者：wecent
 * 时间：2017/11/8
 * 描述：
 */

abstract public class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int indext = 0; indext < fragmentManager.getFragments().size(); indext++) {
            Fragment fragment=fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if(fragment == null) {
                Timber.w(toString() + "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
            } else {
                handleResult(fragment,requestCode,resultCode,data);
            }
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        Timber.e(toString() + "BaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null) {
            for(Fragment frag:childFragment) {
                if(frag != null) {
                    handleResult(frag, requestCode, resultCode, data);
                }
            }
        }
        if (childFragment == null) {
            Timber.e(toString() + "BaseFragmentActivity");
        }
    }
}
