package com.gcml.common.base;

import android.support.v4.app.Fragment;

public class LazyFragment extends Fragment {

    private boolean isPageResume;

    public boolean isPageResume() {
        return isPageResume;
    }

    @Override
    public void onResume() {
        if (!isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            onPageResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isPageResume) {
            isPageResume = false;
            onPagePause();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (isPageResume && getUserVisibleHint()) {
                isPageResume = false;
                onPagePause();
            }
        } else {
            if (!isPageResume && getUserVisibleHint()) {
                isPageResume = true;
                onPageResume();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!isPageResume && !isHidden()) {
                isPageResume = true;
                onPageResume();
            }
        } else {
            if (isPageResume && !isHidden()) {
                isPageResume = false;
                onPagePause();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onPageResume() {

    }

    protected void onPagePause() {

    }
}
