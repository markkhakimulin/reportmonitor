package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
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
public class ConversionPresenter {

    private ConversionModel model;
    private RxSchedulers rxSchedulers;
    private CompositeSubscription subscriptions;
    private ConversionReportActivity view;


    public ConversionPresenter(ConversionModel model, ConversionReportActivity view, RxSchedulers schedulers, CompositeSubscription subscriptions) {
        this.model = model;
        this.rxSchedulers = schedulers;
        this.subscriptions = subscriptions;
        this.view = view;

    }
    /**
     * Called when the fragment has been refreshed by user from  {@link ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment}
     * @param startDate start report date , non-null
     * @param finishDate finish report date , non-null
     * @param stores selected store ids, if null - all stores id.
     *
     * @return Observable{@link ArrayList<ConversionData>}
     */
    public void refresh(Date startDate,Date finishDate,List<String> stores) {
        subscriptions.add(getConversion(startDate,finishDate,stores,Constants.SOAP_METHOD_GET_CONVERSION));
    }
    /**
     * Called when the fragment has been refreshed by user from  {@link ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment}
     * @param startDate start hour of date , non-null
     * @param finishDate finish hour of date , non-null
     * @param stores selected store id, not null - only single element in list.
     *
     * @return Observable{@link ArrayList<ConversionData>}
     */
    public void refreshDaily(Date startDate,Date finishDate,List<String> stores) {
        subscriptions.add(getConversion(startDate,finishDate,stores,Constants.SOAP_METHOD_GET_CONVERSION_DAILY));
    }

    public void onDestroy() {
        subscriptions.clear();
    }

    public void showStoreList(ArrayList<String> chosenStores) {

        subscriptions.add(getStoreList(chosenStores));
    }

    private Subscription getStoreList(ArrayList<String> chosenStores) {

        return Observable.just(true)
            .flatMap(new Func1<Object, Observable<ArrayList<Store>>>() {
                @Override
                public Observable<ArrayList<Store>> call(Object empty) {
                    return model.getStores();
                }
            })
            .subscribeOn(rxSchedulers.runOnBackground())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stores -> {
                System.out.println("processing ending on thread" + Thread.currentThread().getName());
                if (chosenStores != null) {
                    for (Store store : stores) {
                        if (chosenStores.contains(store.id)) {
                            store.marked = 1;
                        }
                    }
                }
                view.onShowStores(stores);
            }, Utils::handleThrowable
        );

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



}
