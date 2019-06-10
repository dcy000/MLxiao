package com.gcml.common.menu;

import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

import static com.gcml.common.constant.Global.URI;

public interface MenuService {
    @GET(URI + "/sys/menu/nav")
    Observable<ApiResult<AppMenuBean>> getAllMenus();
}
