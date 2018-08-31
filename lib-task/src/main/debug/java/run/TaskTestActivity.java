package run;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.task.R;
import com.gcml.task.network.TaskRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TaskTestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle;
    TextView mAction;
    TaskRepository mTaskRepository = new TaskRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_test);
        mTitle = findViewById(R.id.tv_task_title);
        mAction = findViewById(R.id.tv_task_action);

        mAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_task_action) {
            getIsTaskHealth();
//            CC.obtainBuilder("app.component.task.comply").build().callAsync();
        }
    }

    @SuppressLint("CheckResult")
    private void getIsTaskHealth() {
        mTaskRepository.isTaskHealthListFromApi("100206")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {

                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {

                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object body) {
                        super.onNext(body);
                        CC.obtainBuilder("app.component.task")
                                .setContext(TaskTestActivity.this)
                                .build()
                                .callAsync();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof NullPointerException) {
                            CC.obtainBuilder("app.component.task").build().callAsync();
                        } else {
                            CC.obtainBuilder("app.component.task.comply").build().callAsync();
                        }
                    }
                });
    }

}
