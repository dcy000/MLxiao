package com.gcml.common.repository;

import com.gcml.common.repository.entity.SheetEntity;
import com.gcml.common.repository.entity.SongEntity;
import com.gcml.common.repository.local.SheetDao;
import com.gcml.common.repository.local.SheetDb;
import com.gcml.common.repository.remote.MusicService;
import com.gcml.common.utils.RxUtils;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MusicRepository {
    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private MusicService mMusicService = mRepositoryHelper.retrofitService(MusicService.class);

    private SheetDao mSheetDao = mRepositoryHelper.roomDb(SheetDb.class, SheetDb.class.getName()).sheetDao();

    public Observable<List<SheetEntity>> sheetListFromApi(
            String name,
            int page,
            int limit) {
        return mMusicService.sheets(name, page, limit)
                .compose(RxUtils.<List<SheetEntity>>apiResultTransformer());
    }

    public Observable<List<SongEntity>> songListFromApi(
            int sheetId,
            int page,
            int limit) {
        return mRepositoryHelper.retrofitService(MusicService.class).songs(3, "", sheetId, page, limit)
                .compose(RxUtils.<List<SongEntity>>apiResultTransformer());
    }

    public Observable<List<SheetEntity>> sheetListFromApiAndSaveDb(
            String name,
            int page,
            int limit) {
        return mMusicService.sheets(name, page, limit)
                .compose(RxUtils.<List<SheetEntity>>apiResultTransformer())
                .doOnNext(new Consumer<List<SheetEntity>>() {
                    @Override
                    public void accept(List<SheetEntity> sheetEntities) throws Exception {
                        mSheetDao.insertAll(sheetEntities);
                    }
                });
    }

    public Observable<List<SheetEntity>> sheetListFromDb(){
        return Observable.fromCallable(new Callable<List<SheetEntity>>() {
            @Override
            public List<SheetEntity> call() throws Exception {
                return mSheetDao.getAll();
            }
        });
    }

    public Observable<Object> deleteAllSheetsFromDb(){
        return Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                 mSheetDao.deleteAll();
                 return new Object();
            }
        });
    }
}
