package com.geekbrains.rxjava;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String LOG_TAG = "MainActivity";

    private Observable<String> observable;
    private Observer<String> observer;
    private Observable<String> observableJust;
    private Observable<Long> observableInterval;
    private Observer<Long> observerInterval;
    private Observable<String> observableTake;
    private Observable<String> observableSkip;
    private Observable<Integer> observableMap;
    private Observer<Integer> observerMap;
    private Observable<String> observableDistinct;
    private Observable<Integer> observableFilter;
    private Observable<Integer> observableMerge;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> liters = Arrays.asList("a", "b", "c", "d");
        observable = Observable.fromIterable(liters);


        observer = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onComplete() {
                Log.v(LOG_TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.v(LOG_TAG, "onError = " + e);
            }

            @Override
            public void onNext(String s) {
                Log.v(LOG_TAG, "onNext = " + s);
            }
        };

        observable.subscribe(observer);

        observableJust = Observable.just("a", "b", "c", "d");

        observableInterval = Observable.interval(200, TimeUnit.MILLISECONDS);

        observerInterval = new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onComplete() {
                Log.v(LOG_TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.v(LOG_TAG, "onError = " + e);
            }

            @Override
            public void onNext(Long s) {
                Log.v(LOG_TAG, "onNext = " + s);
            }
        };

        observerMap = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onComplete() {
                Log.v(LOG_TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.v(LOG_TAG, "onError = " + e);
            }

            @Override
            public void onNext(Integer s) {
                Log.v(LOG_TAG, "onNext = " + s);
            }
        };

        observableTake = Observable
                .fromArray("1", "2", "3", "4", "5", "6", "7")
                .take(2);

        observableSkip = Observable
                .fromArray("1", "2", "3", "4", "5", "6", "7")
                .skip(2);


        Function<String, Integer> myFuncForMap = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return Integer.parseInt(s) * 2;
            }
        };

        observableMap = Observable
                .fromArray("1", "2", "3", "gjhg", "5", "6", "7")
                .skip(2)
                .map(myFuncForMap);

        observableDistinct = Observable
                .fromArray("a", "x", "d", "a", "r", "q", "d", "s", "w")
                .distinct();


        Predicate<Integer> myFuncForFilter = new Predicate<Integer>() {

            @Override
            public boolean test(Integer integer) {
                int res = integer%3;
                if (res == 0) return true;
                return false;
            }
        };

        observableFilter = Observable
                .fromArray(9, 12, 33, 17, 18, 26)
                .filter(myFuncForFilter);

        observableMerge = Observable
                .fromArray(1,2,3,4,5,6)
                .take(5)
                .mergeWith(Observable.fromArray(4,5,6,7,8,9))
                .distinct();

        Flowable.just(7, 8, 9, 999)
                .distinct()
                .take(4)
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(LOG_TAG, "onNext");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_error_completed:
                observable.subscribe(observer);
                break;
            case R.id.just:
                observableJust.subscribe(observer);
                break;
            case R.id.interval:
                observableInterval.subscribe(observerInterval);
                break;
            case R.id.take:
               observableTake.subscribe(observer);
                break;
            case R.id.skip:
                observableSkip.subscribe(observer);
                break;
            case R.id.map:
                observableMap.subscribe(observerMap);
                break;
            case R.id.distinct:
                observableDistinct.subscribe(observer);
                break;
            case R.id.filter:
                observableFilter.subscribe(observerMap);
                break;
            case R.id.merge:
                observableMerge.subscribe(observerMap);
                break;
            case R.id.unsubscribe:
                mDisposable.dispose();
                break;
        }
    }
}
