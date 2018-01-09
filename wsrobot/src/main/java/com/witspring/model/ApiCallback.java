package com.witspring.model;

import com.witspring.model.entity.Result;

/**
 * API请求完毕回调
 * @author Created by Goven on 2017/8/7 下午2:09
 * @email gxl3999@gmail.com
 */
public abstract class ApiCallback<T> {

    public abstract void onResult(Result<T> result);

}
