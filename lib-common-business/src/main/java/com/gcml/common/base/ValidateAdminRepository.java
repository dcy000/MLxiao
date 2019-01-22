package com.gcml.common.base;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;


/**
 * Created by lenovo on 2019/1/22.
 */

public class ValidateAdminRepository {
    ValidateAdminService service = RetrofitHelper.service(ValidateAdminService.class);

    public Observable<Object> validateAdmin(String password) {
        return service.validateAdmin(password).compose(RxUtils.apiResultTransformer());
    }
}
