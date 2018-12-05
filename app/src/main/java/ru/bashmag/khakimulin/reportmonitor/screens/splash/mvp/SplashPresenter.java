package ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp;

import java.net.SocketTimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SplashPresenter extends BasePresenter {

    private SplashModel model;
    private SplashActivity view;

    public SplashPresenter(DB db,
                           SplashModel model,
                           SplashActivity view,
                           RxSchedulers schedulers,
                           CompositeDisposable subscriptions) {
        super(db, subscriptions, schedulers, view);
        this.model = model;
        this.view = view;
    }

    public void onCreate() {
    }

    private Disposable loadData() {

        return model.isNetworkAvailable()
            .subscribeOn(rxSchedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(isNetworkAvailable -> {
                    if (isNetworkAvailable) {
                        view.setStatus(view.getString(R.string.check_connection));
                        subscriptions.add(pingPong());
                        return;
                    }
                    Utils.showSnackbar(view.findViewById(R.id.fullscreen_content_controls), view.getString(R.string.on_error_check_connection), 0);
            }, this::processThrow);
    }

    private Disposable getStores() {

        return model.getStores()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(error-> {
                    if (error.isEmpty()) {
                        view.setStatus(view.getString(R.string.loading_users));
                        subscriptions.add(getUsers());
                        return;
                    }
                    view.onShowToast(error);
            }, this::processThrow);
    }

    private Disposable getUsers() {
        return model.getUsers()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(error-> {
                if (error.isEmpty()) {
                    view.showReportListActivity();
                    return;
                }
                view.showLoginActivity();
                view.onShowToast(error);
        }, this::processThrow);
    }

    private Disposable pingPong() {
        return model.pingPong()
            .subscribeOn(rxSchedulers.internet())
            .unsubscribeOn(rxSchedulers.internet())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(success-> {
                if (success) {
                    view.setStatus(view.getString(R.string.loading_stores));
                    subscriptions.add(getStores());

                }
            }, this::processThrow);
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
