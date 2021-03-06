package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;


/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionPresenter  extends BasePresenter {

    private ConversionData conversionData;
    private ConversionModel model;
    private ConversionReportActivity view;


    public ConversionPresenter(DB db,
                               ConversionModel model,
                               ConversionReportActivity view,
                               RxSchedulers schedulers,
                               CompositeDisposable subscriptions) {
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

        if (chosenStoreList.size() == 0) {
            view.setRefreshing(false);
            view.onShowToast(view.getString(R.string.on_error_chosen_stores));
            return;
        }
        if (getStartDate() == null || getFinishDate() == null) {
            view.setRefreshing(false);
            view.onShowToast(view.getString(R.string.on_error_chosen_period));
            return;
        }
        subscriptions.add(getConversion(startDate,finishDate,chosenStoreList,method));
    }

    @Override
    public void onCreate() {

    }
    private Observable<ArrayList<Store>> getStores(ArrayList<String> storeIds) {
        return Observable.fromCallable(() -> {
            if (userId.equals(Constants.EMPTY_ID)) {
                return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByAnonymous(storeIds);
            }
            return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByUserId(userId,storeIds);
        });
    }

    @Override
    protected Disposable getStoreList() {

        return getStores(chosenStoreList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stores -> {
                            view.onShowStores(stores);
                        }, this::processThrow
                );

    }

    private Disposable getConversion(Date startDate,Date finishDate,ArrayList<String> stores,String method) {

        return model.isNetworkAvailable().doOnNext(networkAvailable ->{
            if (!networkAvailable) {
                Utils.showSnackbar(view.findViewById(R.id.container), view.getString(R.string.on_error_check_connection), 0);
                view.setRefreshing(false);
            }
        })
                .filter(isNetworkAvailable -> {
                    return true;
                })
                .flatMap((Function<Boolean, Observable<? extends ArrayList<ConversionData>>>) aBoolean ->
                        model.getConversion(startDate,finishDate, stores,method))
                .subscribeOn(rxSchedulers.internet())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    view.setRefreshing(false);
                    view.onShowReport(data);
                },throwable-> {
                    view.setRefreshing(false);
                    processThrow(throwable);
                });
    }

    public void replaceAndRefresh() {
        view.replaceFragment(ConversionListFragment.newInstance(view.type), false, ConversionListFragment.TAG);
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
