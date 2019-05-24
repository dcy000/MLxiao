package com.gcml.module_auth_hospital.model2;

import android.content.Context;

import com.gcml.common.AppDelegate;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.RoomHelper;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;

import io.reactivex.Observable;

public class UserRepository {
    private UserService mUserService = RetrofitHelper.service(UserService.class);
    private Context mContext = AppDelegate.INSTANCE.app();
    private UserDao mUserDao = RoomHelper.db(UserDb.class, UserDb.class.getName()).userDao();


    public Observable<UserToken> signIn(UserPostBody body) {
        return mUserService.signIn(body)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<UserToken> signInByIdCard(UserPostBody body) {
        return mUserService
                .signInByIdCard(body)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<UserBean> signUp(SignUpBean body,String passWord) {
        return mUserService
                .signUp(body,passWord)
                .compose(RxUtils.apiResultTransformer());
    }


}
