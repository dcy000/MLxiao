package com.gcml.common.repository;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gcml.common.repository.entity.SheetEntity;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import com.gcml.common.component.R;
public class RepositoryActivity extends AppCompatActivity {

    private MusicRepository mMusicRepository;
    private Disposable mDisposable = Disposables.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        mMusicRepository = new MusicRepository();

    }

    public void onNetwork(View view) {
        mDisposable.dispose();
        Observable<List<SheetEntity>> listObservable = mMusicRepository.sheetListFromApi("", 1, 12);
        mDisposable = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "开始...", Toast.LENGTH_SHORT).show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "结束！！！", Toast.LENGTH_SHORT).show();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<SheetEntity>>() {
                    @Override
                    public void onNext(List<SheetEntity> sheetEntities) {
                        super.onNext(sheetEntities);
                        Toast.makeText(RepositoryActivity.this, sheetEntities.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(RepositoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void onNetworkAndWriteDb(View view) {
        mDisposable.dispose();
        Observable<List<SheetEntity>> listObservable = mMusicRepository.sheetListFromApiAndSaveDb("", 1, 12);
        mDisposable = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "开始...", Toast.LENGTH_SHORT).show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "结束！！！", Toast.LENGTH_SHORT).show();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<SheetEntity>>() {
                    @Override
                    public void onNext(List<SheetEntity> sheetEntities) {
                        super.onNext(sheetEntities);
                        Toast.makeText(RepositoryActivity.this, sheetEntities.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(RepositoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void onReadDb(View view) {
        mDisposable.dispose();
        Observable<List<SheetEntity>> listObservable = mMusicRepository.sheetListFromDb();
        mDisposable = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "开始...", Toast.LENGTH_SHORT).show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "结束！！！", Toast.LENGTH_SHORT).show();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<SheetEntity>>() {
                    @Override
                    public void onNext(List<SheetEntity> sheetEntities) {
                        super.onNext(sheetEntities);
                        Toast.makeText(RepositoryActivity.this, sheetEntities.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(RepositoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void onDeleteDb(View view) {
        mDisposable.dispose();
        Observable<Object> listObservable = mMusicRepository.deleteAllSheetsFromDb();
        mDisposable = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "开始...", Toast.LENGTH_SHORT).show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        Toast.makeText(RepositoryActivity.this, String.valueOf(Looper.myLooper() == Looper.getMainLooper()) + "结束！！！", Toast.LENGTH_SHORT).show();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        Toast.makeText(RepositoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(RepositoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public void onLoadImage(View view) {
        String url1 = "https://www.baidu.com/img/bd_logo1.png";
        ImageLoader.with(this)
                .load(url1)
//                .load(/** Bitmap, file, DrawableRes */)
                .resize(200, 200)
//                .radius(10)
                .circle()
                .blur()
                .blurValue(15)
                .skipMemoryCache()
                .diskCacheStrategy(ImageLoader.DiskCacheStrategy.RESULT)
                .callback(new ImageLoader.Callback() {
                    @Override
                    public void onSuccess() {
                        Timber.i("onSuccess");
                    }

                    @Override
                    public void onFailed() {
                        Timber.i("onFailed");
                    }
                })
                .into(findViewById(R.id.iv_image));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

}

