package ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp;

import android.util.Log;

import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SplashPresenter {

    private SplashModel model;
    private RxSchedulers rxSchedulers;
    private CompositeSubscription subscriptions;
    private SplashActivity view;

    public SplashPresenter(SplashModel model,SplashActivity view, RxSchedulers schedulers, CompositeSubscription subscriptions) {
        this.model = model;
        this.rxSchedulers = schedulers;
        this.subscriptions = subscriptions;
        this.view = view;
    }

    public void onCreate() {
        subscriptions.add(getStoreList());
    }

    public void onDestroy() {
        subscriptions.clear();
    }


    private Subscription getStoreList() {

        return model.isNetworkAvailable().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean networkAvailable) {
                if (!networkAvailable) {
                    Log.d("no conn", "no connexion");
                    // UiUtils.showSnackbar();
                    // Show Snackbar can't use app
                }
            }
        })
        .filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean isNetworkAvailable) {
                return true;
            }
        })
        .flatMap(new Func1<Boolean, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Boolean isAvailable) {
                return model.getStoreList();
            }
        })
        .subscribeOn(rxSchedulers.internet())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
            @Override
            public void call(String error) {
                if (error.isEmpty()) {
                    view.showReportListActivity();
                } else {
                    view.onShowToast(error);
                }
            }
        },(throwable)-> {
            view.onShowToast(throwable.getMessage());
            view.finish();
        });
    }

}
