package com.example.mydagger1;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.mydagger1.network.AppApiHelper;
import com.example.mydagger1.utils.ImageLoader;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    AppApiHelper appApiHelper;

    @Inject
    ImageLoader imageLoader;

    private TextView tvResult;
    private TextView tvNum1;
    private TextView tvNum2;
    private TextView tvSub;
    private ProgressBar pbLoading;
    private ProgressBar pbPercent;

    private Integer number = 0;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        pbLoading = findViewById(R.id.pbLoading);
        pbPercent = findViewById(R.id.pbPercent);
        tvSub = findViewById(R.id.tvSub);
        tvNum1 = findViewById(R.id.tvNumber1);
        tvNum2 = findViewById(R.id.tvNumber2);

        /*appApiHelper.listRepos("tomdss").enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                tvResult.setText(response.body().getFollowers()+"");
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });*/

        //rx operator: zip, merge, flat
        /*appApiHelper.rxListRepos("tomdss")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Repo>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {
                                   Log.d(TAG, "onSubscribe");
                                   pbLoading.setVisibility(View.VISIBLE);
                               }

                               @Override
                               public void onSuccess(@NonNull Repo repo) {
                                   imageLoader.loadImageGlide(getBaseContext(),repo.getAvatarUrl(), ivAvatar);
                                   tvResult.setText(repo.getLogin() + " \n" + repo.getAvatarUrl());
                                   Log.d(TAG, "onSuccess");
                                   pbLoading.setVisibility(View.GONE);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {
                                   Log.d(TAG, "onError");
                                   pbLoading.setVisibility(View.GONE);
                               }
                           }
                );*/

        // Observable
        Observable<Long> numberObservable = getNumberObservable();


        // Observer
        Observer<Long> numberObserver = getNumberObserver();


        initNumber();

        // Observer subscribing to observable
        numberObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(numberObserver);


    }

    private void initNumber() {
        Random random = new Random();
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);
        tvNum1.setText(num1 + "");
        tvSub.setText("+");
        tvNum2.setText(num2 + "");

    }

    private Observable<Long> getNumberObservable() {
        return Observable.interval(50, TimeUnit.MILLISECONDS).take(100).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                return aLong + 1;
            }
        });
    }

    private Observer<Long> getNumberObserver() {
        return new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(@NonNull Long integer) {
                tvResult.setText("Number = " + integer);
                pbPercent.setProgress(integer.intValue());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

}