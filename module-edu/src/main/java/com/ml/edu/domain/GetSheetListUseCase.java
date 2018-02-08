package com.ml.edu.domain;

import com.ml.edu.common.base.UseCase;
import com.ml.edu.data.ApiHelper;
import com.ml.edu.data.entity.SheetEntity;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by afirez on 18-2-6.
 */

public class GetSheetListUseCase extends UseCase<GetSheetListUseCase.Params, List<SheetEntity>> {

    private final ApiHelper mApiHelper;

    public GetSheetListUseCase() {
        super();
        mApiHelper = ApiHelper.getInstance();
    }

    @Override
    protected Observable<List<SheetEntity>> rxResult(Params params) {
        return mApiHelper.sheetList(params.sheetName, params.page, params.limit);
    }

    public static class Params {
        public String sheetName;

        public int page;

        public int limit;

        public Params(String sheetName, int page, int limit) {
            this.sheetName = sheetName;
            this.page = page;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "Params{" +
                    "sheetName='" + sheetName + '\'' +
                    ", page=" + page +
                    ", limit=" + limit +
                    '}';
        }
    }
}
