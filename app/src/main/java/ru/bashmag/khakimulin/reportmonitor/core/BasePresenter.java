package ru.bashmag.khakimulin.reportmonitor.core;

import android.support.annotation.CallSuper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public abstract class BasePresenter {
    private BaseActivity baseView;
    protected ArrayList<String> chosenStoreList;
    protected DB db;
    protected Date finishDate;
    protected String periodTitle;
    protected int periodType;
    protected RxSchedulers rxSchedulers;
    protected Date startDate;
    protected String storeId;
    protected CompositeSubscription subscriptions;
    private HashMap<Constants.ReportType, Integer> titles = new HashMap();
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


    public BasePresenter(DB db, CompositeSubscription subscriptions, RxSchedulers rxSchedulers,BaseActivity baseView) {
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

    protected Subscription getStoreList() {

        return getStores()
            .subscribeOn(rxSchedulers.runOnBackground())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stores -> {
                        baseView.onShowStores(stores);
                    }, Utils::handleThrowable
            );

    }
    protected void processThrow(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            baseView.showYesNoMessageDialog(baseView.getString(R.string.on_error_connection)
                    ,baseView.getString(R.string.dialog_vpn_settings_title)
                    , new Func0<Void>() {
                        @Override
                        public Void call() {
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
                    ,baseView.getString(R.string.dialog_net_settings_title)
                    , new Func0<Void>() {
                        @Override
                        public Void call() {
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

    public ArrayList<Store> getChosenStores() {
        return (ArrayList<Store>) db.chosenStoreDao().getChosenByUserId(userId);
    }
    public ArrayList<String> getChosenStoreIds() {
        return (ArrayList<String>) db.chosenStoreDao().getChosenIdsByUserId(userId);
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

    public ArrayList<Store>  change() {
        return (ArrayList<Store>) db.chosenStoreDao().getAllByUserId(userId);
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
