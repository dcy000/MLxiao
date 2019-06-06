package com.gcml.module_auth_hospital.ui.findPassWord;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;

public class CodeRepository {
    private CodeService service = RetrofitHelper.service(CodeService.class);

    public Observable<String> fetchCode(String phone) {
        return service.fetchCode(phone)
                .compose(RxUtils.apiResultTransformer())
                .map(code -> code.code);
    }

    public Observable<Object> updatePassword(String phone, String passWord) {
        return service.updatePassword(phone, passWord)
                .compose(RxUtils.apiResultTransformer());
    }
}
