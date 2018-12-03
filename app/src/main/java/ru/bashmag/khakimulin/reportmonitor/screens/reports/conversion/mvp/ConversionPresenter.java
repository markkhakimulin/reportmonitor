package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
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
public class ConversionPresenter  extends BasePresenter {

    private ConversionData conversionData;
    private ConversionModel model;
    private ConversionReportActivity view;


    public ConversionPresenter(DB db, ConversionModel model, ConversionReportActivity view, RxSchedulers schedulers, CompositeSubscription subscriptions) {
        super(db, subscriptions, schedulers, view);
        this.model = model;
        this.view = view;
    }

    @Override
    public void refresh() {
        refreshByMethod(Constants.SOAP_METHOD_GET_CONVERSION);
    }

    public void refreshDaily() {
        refreshByMethod(Constants.SOAP_METHOD_GET_CONVERSION_DAILY);
    }

    private void refreshByMethod(String method) {
        if (getStartDate() == null || getFinishDate() == null) {
            view.setRefreshing(false);
            view.onShowToast(view.getString(R.string.on_error_chosen_period));
            return;
        }

        if (getChosenStoreIds().size() == 0) {
            view.setRefreshing(false);
            view.onShowToast(view.getString(R.string.on_error_chosen_stores));
            return;
        }
        subscriptions.add(getConversion(startDate,finishDate,getChosenStoreIds(),method));
    }

    @Override
    public void onCreate() {

    }

    protected Subscription getStoreList() {
        return getStores(chosenStoreList)
                .subscribeOn(rxSchedulers.runOnBackground())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object stores) {
                        view.onShowStores((ArrayList<Store>)stores);
                    }
                });
    }

    private Observable getStores(final ArrayList<String> storeIds) {
        return Observable.fromCallable(new Callable<ArrayList<Store>>() {
            public ArrayList<Store> call() throws Exception {
                if (ConversionPresenter.this.userId.equals(Constants.EMPTY_ID)) {
                    return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByAnonymous(storeIds);
                }
                return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByUserId(userId, storeIds);
            }
        });
    }

    private Subscription getConversion(Date startDate,Date finishDate,List<String> stores,String method) {

        return model.isNetworkAvailable().doOnNext(new Action1<Boolean>() {
            @Override
            public void call(Boolean networkAvailable) {
                if (!networkAvailable) {
                    Log.d("no conn", "no connetion");
                    view.onShowToast("no network connection");
                    view.setRefreshing(false);
                }
            }
        })
        .filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean isNetworkAvailable) {
                return true;
            }
        })
        .flatMap(new Func1<Boolean, Observable<? extends ArrayList<ConversionData>>>() {
            @Override
            public Observable<? extends ArrayList<ConversionData>> call(Boolean isAvailable) {
                return model.getConversion(startDate,finishDate, stores,method);
            }
        })
        .subscribeOn(rxSchedulers.internet())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(data -> {
            view.setRefreshing(false);
            view.onShowReport(data);
            },(throwable)-> {
            view.setRefreshing(false);
            view.showMessage(throwable.getMessage());
        });
    }

    public void replaceAndRefresh() {
        view.replaceFragment(ConversionListFragment.newInstance(view.type), Boolean.valueOf(false), ConversionListFragment.TAG);
        view.invalidate();
    }

    public ArrayList<String> getChosenStoreIds() {
        return this.chosenStoreList;
    }

    public ConversionData getConversionData() {
        return this.conversionData;
    }

    public void setChosenStore(String storeId) {
        if (this.chosenStoreList.contains(storeId)) {
            this.chosenStoreList.remove(storeId);
        } else {
            this.chosenStoreList.add(storeId);
        }
    }

    public void setConversionData(ConversionData conversionData) {
        this.conversionData = conversionData;
    }




}
