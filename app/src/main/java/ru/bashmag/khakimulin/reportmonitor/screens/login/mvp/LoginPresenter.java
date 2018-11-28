package ru.bashmag.khakimulin.reportmonitor.screens.login.mvp;

import android.support.design.widget.Snackbar;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.login.UserData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class LoginPresenter extends BasePresenter {

    private LoginModel model;
    private LoginActivity view;

    public LoginPresenter(DB db, LoginModel model, LoginActivity view, RxSchedulers schedulers, CompositeSubscription subscriptions) {

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

    private Subscription getUserList() {
        return model.getUserList().subscribeOn(rxSchedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                               @Override
                               public void call(List<User> data) {
                                   if (!data.isEmpty()) {
                                       view.showUserList(data);
                                   } else {
                                       view.onShowToast("Нет пользователей в базе");
                                   }
                               }
                           }
                        ,(throwable)-> {

                        });
    }

    private Subscription getUserById(String id) {
        return model.getUserById(id).subscribeOn(rxSchedulers.runOnBackground())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                               @Override
                               public void call(User data) {
                                   if (!data.isEmpty()) {
                                       view.showUser(data);
                                   }
                               }
                           }
                        ,(throwable)-> {

                        });
    }

    private Subscription loadData(final String hash) {

        return model
            .isNetworkAvailable()
            .doOnNext(new Action1<Boolean>() {
                @Override
                public void call(Boolean networkAvailable) {
                    if (!networkAvailable) {
                        Utils.showSnackbar(view.findViewById(R.id.fullscreen_content_controls)
                                ,"Нет интернет подключения",Snackbar.LENGTH_LONG);
                    }
                }
            })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isNetworkAvailable) {
                        return true;
                    }
                })
                .flatMap((Func1<Boolean, Observable<Boolean>>) isAvailable -> {
                    if (isAvailable)
                        return model.pingPong();
                    else
                        return Observable.just(false);
                })
                    .filter(isPingPong -> true)
                    .flatMap(new Func1<Boolean, Observable<User>>() {
                        @Override
                        public Observable<User> call(Boolean isPingPong) {
                            return model.login(hash);
                        }
                    })
                    .subscribeOn(rxSchedulers.internet())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User data) {
                            if (data != null && !data.isEmpty()) {
                                view.showReportListActivity(data);
                            } else {
                                view.showButtons(true);
                                view.onShowToast("Неверный логин/пароль");
                            }
                        }
                    }
                    ,(throwable)-> {

                        view.showButtons(true);

                        if (throwable instanceof SocketTimeoutException) {
                            view.showYesNoMessageDialog("Ошибка подключения"
                                    , "Необходимо подключение VPN. Открыть сетевые настройки?"
                                    , new Func0<Void>() {
                                        @Override
                                        public Void call() {
                                            view.gotoVPNSettings();
                                            return null;
                                        }
                                    }, null);
                        } else if (throwable instanceof IllegalStateException) {
                            view.showYesNoMessageDialog("Ошибка обработки данных"
                                    , throwable.getMessage()
                                    , null
                                    , null);
                        } else if (throwable instanceof ConnectException) {
                            view.showYesNoMessageDialog("Ошибка интернет соединения"
                                    ,"Необходимо подключение к сети. Открыть сетевые настройки?"
                                    , new Func0<Void>() {
                                        @Override
                                        public Void call() {
                                            view.gotoNETSettings();
                                            return null;
                                        }
                                    },null);
                        } else {
                            view.showYesNoMessageDialog("Ошибка загрузки данных"
                                    , throwable.getMessage()
                                    , null
                                    , null);
                        }
                    });
    }

    private Subscription loadDataAnonymous(String userID,String pass) {

        return model
                .isNetworkAvailable()
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean networkAvailable) {
                        if (!networkAvailable) {
                            Utils.showSnackbar(view.findViewById(R.id.fullscreen_content_controls)
                                    ,"Нет интернет подключения",Snackbar.LENGTH_LONG);
                        }
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean isNetworkAvailable) {
                        return true;
                    }
                })
                .flatMap((Func1<Boolean, Observable<Boolean>>) isAvailable -> {
                    if (isAvailable)
                        return model.pingPong();
                    else
                        return Observable.just(false);
                })
                .filter(isPingPong -> true)
                .flatMap(new Func1<Boolean, Observable<? extends User>>() {
                    @Override
                    public Observable<User> call(Boolean isPingPong) {

                        if (Constants.EMPTY_ID.equals(userID) && Constants.EMPTY_PASS.equals(pass)) {
                            return model.getUserById(userID);
                        }
                        return Observable.just(null);
                    }
                })
                .subscribeOn(rxSchedulers.internet())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                               @Override
                               public void call(User data) {
                                   if (data != null) {
                                       view.showReportListActivity(data);
                                   } else {
                                       view.showButtons(true);
                                       view.onShowToast("Неверный логин/пароль");
                                   }
                               }
                           }
                        ,(throwable)-> {

                            view.showButtons(true);

                            if (throwable instanceof SocketTimeoutException) {
                                view.showYesNoMessageDialog("Ошибка подключения"
                                        , "Необходимо подключение VPN. Открыть сетевые настройки?"
                                        , new Func0<Void>() {
                                            @Override
                                            public Void call() {
                                                view.gotoVPNSettings();
                                                return null;
                                            }
                                        }, null);
                            } else if (throwable instanceof IllegalStateException) {
                                view.showYesNoMessageDialog("Ошибка обработки данных"
                                        , throwable.getMessage()
                                        , null
                                        , null);
                            } else if (throwable instanceof ConnectException) {
                                view.showYesNoMessageDialog("Ошибка интернет соединения"
                                        ,"Необходимо подключение к сети. Открыть сетевые настройки?"
                                        , new Func0<Void>() {
                                            @Override
                                            public Void call() {
                                                view.gotoNETSettings();
                                                return null;
                                            }
                                        },null);
                            } else {
                                view.showYesNoMessageDialog("Ошибка загрузки данных"
                                        , throwable.getMessage()
                                        , null
                                        , null);
                            }
                        });
    }

}
