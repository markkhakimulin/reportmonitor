package ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp;

import android.util.Log;

import java.net.SocketTimeoutException;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
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
public class SplashPresenter extends BasePresenter {

    private SplashModel model;
    private SplashActivity view;

    public SplashPresenter(DB db, SplashModel model, SplashActivity view, RxSchedulers schedulers, CompositeSubscription subscriptions) {
        super(db, subscriptions, schedulers, view);
        this.model = model;
        this.view = view;
    }

    public void onCreate() {
    }

    private Subscription loadData() {
        return model.isNetworkAvailable()
            .subscribeOn(rxSchedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Boolean>() {
                public void call(Boolean isNetworkAvailable) {
                    if (isNetworkAvailable.booleanValue()) {
                        view.setStatus(view.getString(R.string.check_connection));
                        subscriptions.add(pingPong());
                        return;
                    }
                    Utils.showSnackbar(SplashPresenter.this.view.findViewById(R.id.fullscreen_content_controls), view.getString(R.string.on_error_check_connection), 0);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    processThrow(throwable);
                }
            });
    }

    private Subscription getStores() {


        return model.getStores()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<String>() {
                @Override
                public void call(String error) {
                    if (error.isEmpty()) {
                        view.setStatus(view.getString(R.string.loading_users));
                        subscriptions.add(getUsers());
                        return;
                    }
                    view.onShowToast(error);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    processThrow(throwable);
                }
            });


    }

    private Subscription getUsers() {
        return model.getUsers()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<String>() {
                public void call(String error) {
                    if (error.isEmpty()) {
                        view.showReportListActivity();
                        return;
                    }
                    view.showLoginActivity();
                   view.onShowToast(error);
                }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                processThrow(throwable);
            }
        });
    }

    private Subscription pingPong() {
        return model.pingPong()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean success) {

                    if (success) {
                        view.setStatus(view.getString(R.string.loading_stores));
                        subscriptions.add(getStores());

                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    processThrow(throwable);
                }
            });
    }

    @Override
    protected void processThrow(Throwable throwable) {
        super.processThrow(throwable);
        if (throwable instanceof SocketTimeoutException) {
            view.showButton();
        }
    }

    public void refresh() {
        subscriptions.clear();
        subscriptions.add(loadData());
    }

}
