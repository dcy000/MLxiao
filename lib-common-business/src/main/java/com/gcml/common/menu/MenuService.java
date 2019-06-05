package com.gcml.common.menu;

import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MenuService {
    @GET("/open/common/sys/menu/nav")
    Observable<ApiResult<AppMenuBean>> getAllMenus();
}
