package com.example.mydagger1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mydagger1.dialog.ResultDialogFragment;
import com.example.mydagger1.network.AppApiHelper;
import com.example.mydagger1.utils.ImageLoader;
import com.example.mydagger1.utils.SharedPrefs;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends DaggerAppCompatActivity implements ResultDialogFragment.OnResultDialogFragmentClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    AppApiHelper appApiHelper;

    @Inject
    ImageLoader imageLoader;

    private TextView tvResult;
    private TextView tvNum1;
    private TextView tvNum2;
    private TextView tvSub;
    private TextView tvSum;
    private TextView tvScore;
    private TextView tvHighScore;
    private ProgressBar pbLoading;
    private ProgressBar pbPercent;

    private Integer number = 0;
    private int STATE_PLAY = 0;
    private boolean mComplete = false;
    private int mScore = 0;
    private int mHighScore = 0;

//    private Disposable mDisposable;

    public static final String HIGH_SCORE = "high_score";

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHighScore = SharedPrefs.getInstance().get(HIGH_SCORE, Integer.class);

        tvResult = findViewById(R.id.tvResult);
        pbLoading = findViewById(R.id.pbLoading);
        pbPercent = findViewById(R.id.pbPercent);
        tvSub = findViewById(R.id.tvSub);
        tvNum1 = findViewById(R.id.tvNumber1);
        tvNum2 = findViewById(R.id.tvNumber2);
        tvSum = findViewById(R.id.tvSum);
        tvScore = findViewById(R.id.tvScore);
        tvHighScore = findViewById(R.id.tvHighScore);
        tvHighScore.setText(mHighScore + "");
        tvScore.setText(mScore + "");

        findViewById(R.id.cvTrue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompositeDisposable.clear();
                checkTrueFalse(true);
            }
        });

        findViewById(R.id.cvFalse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompositeDisposable.clear();
                checkTrueFalse(false);
            }
        });


        initNumber(0);

        addNewObserver();

    }

    private void addNewObserver() {
        mCompositeDisposable.add(getNumberObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        tvResult.setText(aLong + "%");
                        pbPercent.setProgress(aLong.intValue());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        setScore(false);
                        ResultDialogFragment resultDialogFragment;
                        resultDialogFragment = ResultDialogFragment.newInstance(false);
                        resultDialogFragment.show(getSupportFragmentManager(), "dialog");
                    }
                }));
    }

    private Observable<Long> getNumberObservable() {
        return Observable.interval(100, TimeUnit.MILLISECONDS).take(100).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                return aLong + 1;
            }
        });
    }

    private void initNumber(int statePlay) {
        switch (statePlay) {
            case 1:
                StateWin();
                break;
            case 0:
            case 2:
            default:
                StateStart();
                break;
        }
    }

    private void StateStart() {
        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int numSum = num1 + num2;
        tvNum1.setText(num1 + "");
        tvSub.setText("+");
        tvNum2.setText(num2 + "");
        int numLuck = random.nextInt(3) + 1;
        if (numLuck % 2 == 0) {
            tvSum.setText(numSum + "");
        } else {
            setSum(numLuck, numSum);
        }
    }

    private void StateWin() {
        Random random = new Random();
        int num1 = random.nextInt(1000);
        int num2 = random.nextInt(1000);
        int numSum = num1 + num2;
        tvNum1.setText(num1 + "");
        tvSub.setText("+");
        tvNum2.setText(num2 + "");
        int numLuck = random.nextInt(10);
        if (numLuck % 2 == 0) {
            tvSum.setText(numSum + "");
        } else {
            setSum(numLuck, numSum);
        }
    }

    private void setSum(int numLuck, int numSum) {
        Random random = new Random();
        switch (numLuck) {
            case 1:
                numSum = numSum + random.nextInt(3);
                tvSum.setText(numSum + "");
                break;
            case 3:
                numSum = numSum - random.nextInt(3);
                numSum--;
                tvSum.setText(numSum + "");
                break;
            case 5:
                numSum = numSum + random.nextInt(3) * 10;
                tvSum.setText(numSum + "");
                break;
            case 7:
                numSum = numSum - random.nextInt(3) * 10;
                tvSum.setText(numSum + "");
                break;
            default:
                numSum = numSum - random.nextInt(5) * 100;
                tvSum.setText(numSum + "");
                break;
        }
    }


    private void checkTrueFalse(boolean isChoose) {
        mComplete = true;
        int num1 = Integer.parseInt(tvNum1.getText().toString());
        int num2 = Integer.parseInt(tvNum2.getText().toString());
        int sum = Integer.parseInt(tvSum.getText().toString());
        if (isChoose == (num1 + num2 == sum)) {
            setScore(true);
            initNumber(1);
            addNewObserver();
//            resultDialogFragment = ResultDialogFragment.newInstance(true);
        } else {
            setScore(false);
            ResultDialogFragment resultDialogFragment = ResultDialogFragment.newInstance(false);
            resultDialogFragment.show(getSupportFragmentManager(), "dialog");
        }
    }

    private void setScore(boolean isWin) {
        if (isWin) {
            mScore++;
            if (mScore > mHighScore) {
                mHighScore = mScore;
                SharedPrefs.getInstance().put(HIGH_SCORE, mHighScore);
            }
            tvHighScore.setText(mHighScore + "");
            tvScore.setText(mScore + "");
        } else {
            if (mScore > mHighScore) {
                mHighScore = mScore;
                SharedPrefs.getInstance().put(HIGH_SCORE, mHighScore);
            }
            tvHighScore.setText(mHighScore + "");
            tvScore.setText(mScore + "");
            mScore = 0;
        }

    }

    @Override
    public void onWinClicked(ResultDialogFragment dialog) {
        dialog.dismiss();
        initNumber(1);
        addNewObserver();
    }

    @Override
    public void onLoseClicked(ResultDialogFragment dialog) {
        dialog.dismiss();
        tvScore.setText(0 + "");
        initNumber(2);
        addNewObserver();
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}