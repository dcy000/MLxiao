package com.gcml.module_auth_hospital.model2;

import android.content.Context;

import com.gcml.common.AppDelegate;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.RoomHelper;
import com.gcml.common.constant.Global;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class UserRepository {
    private UserService mUserService = RetrofitHelper.service(UserService.class);
    private Context mContext = AppDelegate.INSTANCE.app();
    private UserDao mUserDao = RoomHelper.db(UserDb.class, UserDb.class.getName()).userDao();


    /**
     * 账号密码登录 获取token 用于游客登录
     */
    public Observable<UserToken> signIn(UserPostBody body) {
        return mUserService.signIn(body)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(userToken -> {
                    UserSpHelper.setToken(Global.TOKEN_PREFIX + userToken.getToken());
                });
    }


    /**
     * 登录-->返回用户信息
     */
    public Observable<UserBean> signInByIdCard(UserPostBody body) {
        return signInByIdCardtrGetToken(body)
                .compose(token2UserInfoTransformer());
    }

    /**
     * token转化成用户信息
     */
    private ObservableTransformer<UserToken, UserBean> token2UserInfoTransformer() {
        return upstream ->
                upstream
                        .flatMap((Function<UserToken, ObservableSource<UserBean>>) userToken -> {
                            return getUserInfoByToken();
                        });
    }

    /**
     * 登录获取token
     */
    public Observable<UserToken> signInByIdCardtrGetToken(UserPostBody body) {
        return mUserService
                .signInByIdCard(body)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(userToken -> {
                    UserSpHelper.setToken(Global.TOKEN_PREFIX + userToken.getToken());
                });
    }

    /**
     * 注册
     *
     * @param body     json 参数
     * @param passWord 密码
     * @return 用户信息包装 UserBean
     */
    public Observable<UserBean> signUp(SignUpBean body, String passWord) {
        return mUserService
                .signUp(body, passWord)
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 根据token获取用户信息 userBean
     */
    public Observable<UserBean> getUserInfoByToken() {
        return mUserService
                .getUserInfoByToken()
                .compose(RxUtils.apiResultTransformer());
    }


}
