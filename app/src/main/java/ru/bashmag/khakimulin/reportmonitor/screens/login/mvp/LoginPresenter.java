package ru.bashmag.khakimulin.reportmonitor.screens.login.mvp;

import android.support.design.widget.Snackbar;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class LoginPresenter extends BasePresenter {

    private LoginModel model;
    private LoginActivity view;

    public LoginPresenter(DB db,
                          LoginModel model,
                          LoginActivity view,
                          RxSchedulers schedulers,
                          CompositeDisposable subscriptions) {

        super(db,subscriptions,schedulers,view);
        this.model = model;
        this.view = view;
    }

    public void refresh() {
        subscriptions.add(getUserList());
    }
    public void onCreate() {}

    public void login(final String hash) {
        subscriptions.clear();
        subscriptions.add(loadData(hash));
    }

    public void loginAnonymous(final String userId,String pass) {
        subscriptions.clear();
        subscriptions.add(loadDataAnonymous(userId,pass));
    }

    public void getLoginById(String id) {
        subscriptions.add(getUserById(id));
    }

    private Disposable getUserList() {
        return model.getUserList().subscribeOn(rxSchedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data -> {
                if (!data.isEmpty()) {
                   view.showUserList(data);
                } else {
                   view.onShowToast("Нет пользователей в базе");
                }
            } ,this::processThrow);
    }

    private Disposable getUserById(String id) {
        return model.getUserById(id)
            .subscribeOn(rxSchedulers.runOnBackground())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data -> {
                if (!data.isEmpty()) {
                    view.showUser(data);
                }
            }
            ,this::processThrow);
    }

    private Disposable loadData(final String hash) {

        return model.isNetworkAvailable().doOnNext(networkAvailable -> {
            if (!networkAvailable) {
                view.onShowToast(view.getString(R.string.on_error_connection));
            }
        })
                .filter(isNetworkAvailable -> true)
                .flatMap((Function<Boolean, Observable<Boolean>>) isAvailable -> {
                    if (!isAvailable) {
                        view.onShowToast(view.getString(R.string.on_error_connection));
                        return Observable.just(false);
                    }
                    return model.pingPong();
                })
                .filter(isPingPong -> true)
                .flatMap(new Function<Boolean, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(Boolean aBoolean) throws Exception {
                        return model.login(hash);
                    }
                })
                    .subscribeOn(rxSchedulers.internet())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        if (data != null && !data.isEmpty()) {
                            view.showReportListActivity(data);
                        } else {
                            view.showButtons(true);
                            view.onShowToast("Неверный логин/пароль");
                        }
                    }
                    ,this::processThrow);
    }

    @Override
    protected void processThrow(Throwable throwable) {
        super.processThrow(throwable);
        view.showButtons(true);
    }

    private Disposable loadDataAnonymous(String userID,String pass) {

        return model.isNetworkAvailable().doOnNext(networkAvailable -> {
            if (!networkAvailable) {
                view.onShowToast(view.getString(R.string.on_error_connection));
            }
        })
                .filter(isNetworkAvailable -> true)
                .flatMap((Function<Boolean, Observable<Boolean>>) isAvailable -> {
                    if (!isAvailable) {
                        view.onShowToast(view.getString(R.string.on_error_connection));
                        return Observable.just(false);
                    }
                    return model.pingPong();
                })
                .filter(isPingPong -> true)
                .flatMap(isPingPong -> {

                    if (Constants.EMPTY_ID.equals(userID) && Constants.EMPTY_PASS.equals(pass)) {
                        return model.getUserById(userID);
                    }
                    return Observable.just(null);
                })
                .subscribeOn(rxSchedulers.internet())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                                   if (data != null) {
                                       view.showReportListActivity(data);
                                   } else {
                                       view.showButtons(true);
                                       view.onShowToast(view.getString(R.string.on_error_wrong_login_pass));
                                   }
                               }
                        ,this::processThrow);
    }

}
