package ru.bashmag.khakimulin.reportmonitor.core;

import android.support.annotation.CallSuper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 01.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public abstract class BasePresenter {
    private BaseActivity baseView;
    public ArrayList<String> chosenStoreList;
    protected DB db;
    protected Date finishDate;
    protected String periodTitle;
    protected int periodType;
    protected RxSchedulers rxSchedulers;
    protected Date startDate;
    protected String storeId;
    protected CompositeDisposable subscriptions;
    private HashMap<Constants.ReportType, Integer> titles = new HashMap<Constants.ReportType, Integer>();
    protected String userId;
    protected String userTitle;

    public int getPeriodType() {
        return periodType;
    }

    public void setPeriodType(int periodType) {
        this.periodType = periodType;
    }

    public String getPeriodTitle() {
        return periodTitle;
    }

    public void setPeriodTitle(String periodTitle) {
        this.periodTitle = periodTitle;
    }

    public void setLocalChosenStoreList(ArrayList<String> chosenStoreList) {
        this.chosenStoreList = chosenStoreList;
    }


    public BasePresenter(DB db, CompositeDisposable subscriptions, RxSchedulers rxSchedulers,BaseActivity baseView) {
        this.db = db;
        this.subscriptions = subscriptions;
        this.rxSchedulers = rxSchedulers;
        this.baseView = baseView;
        titles.put(Constants.ReportType.fullness, R.string.fullness_report_activity_name);
        titles.put(Constants.ReportType.conversion, R.string.conversion_report_activity_name);
        titles.put(Constants.ReportType.sales, R.string.sales_report_activity_name);
        titles.put(Constants.ReportType.turnover, R.string.turnover_report_activity_name);
    }

    public String generatePeriodTitle(DateFormat df) throws NullPointerException {
        assert startDate != null && finishDate != null;
        return String.format("( %s - %s)", df.format(startDate),df.format(finishDate));
    }

    public int getTitle(Constants.ReportType type) {
        return titles.get(type);
    }

    public abstract void refresh();
    public abstract void onCreate();

    @CallSuper
    public void onDestroy() {
        subscriptions.clear();
    }

    public void showStoreList() {
        subscriptions.add(getStoreList());
    }

    private Observable<ArrayList<Store>> getStores() {
        return Observable.fromCallable(new Callable<ArrayList<Store>>() {

            @Override
            public ArrayList<Store> call() throws Exception {
                if (userId.equals(Constants.EMPTY_ID)) {
                    return (ArrayList<Store>) db.chosenStoreDao().getAllByAnonymous();
                }
                return (ArrayList<Store>) db.chosenStoreDao().getAllByUserId(userId);
            }
        });
    }

    protected Disposable getStoreList() {

        return getStores()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stores -> {
                        baseView.onShowStores(stores);
                    }, Utils::handleThrowable
            );

    }
    protected void processThrow(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            baseView.showYesNoMessageDialog(baseView.getString(R.string.on_error_connection)
                    , baseView.getString(R.string.dialog_vpn_settings_title)
                    , new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            baseView.gotoVPNSettings();
                            return null;
                        }
                    }, null);
        } else if (throwable instanceof IllegalStateException) {
            baseView.showYesNoMessageDialog(baseView.getString(R.string.on_error_process)
                    , throwable.getMessage()
                    , null
                    , null);
        } else if (throwable instanceof ConnectException) {
            baseView.showYesNoMessageDialog(baseView.getString(R.string.on_error_connection)
                    , baseView.getString(R.string.dialog_net_settings_title)
                    , new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            baseView.gotoNETSettings();
                            return null;
                        }
                    },null);
        } else {
            baseView.showYesNoMessageDialog(baseView.getString(R.string.on_error_loading)
                    , throwable.getMessage()
                    , null
                    , null);
        }
    }

    public void getChosenStores(DisposableSingleObserver<List<Store>> callback) {
        db.chosenStoreDao().getChosenByUserIdObservable(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
    protected void checkForChosenStoreIds(DisposableSingleObserver<List<String>> callback) {
        db.chosenStoreDao().getChosenIdsByUserIdObservable(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setChosenStore(String storeId) {

        ChosenStore store = db.chosenStoreDao().get(userId,storeId);
        if (store == null) {
            db.chosenStoreDao().insert(new ChosenStore(userId,storeId));
        } else {
            db.chosenStoreDao().delete(userId,storeId);
        }
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getFinishDate() {
        return this.finishDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setUserTitle(String title) {
        this.userTitle = title;
    }
    public String getUserId() {
        return this.userId;
    }
    public String getUserTitle() {
        return this.userTitle;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getStoreId() {
        return this.storeId;
    }
}
