package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverModel;
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
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SalesPresenter extends BasePresenter {

    private SalesReportActivity view;
    private SalesModel model;

    public SalesPresenter(DB db,RxSchedulers schedulers, SalesModel model, SalesReportActivity view, CompositeSubscription sub) {
        super(db,sub,schedulers,view);
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when view has been refreshed by user from  {@link ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment}
     * startDate start report date , non-null
     * finishDate finish report date , non-null
     * stores selected store ids, if null - all stores id.
     *
     * @return Observable{@link ArrayList<ConversionData>}
     */
    public void refresh() {

        if (startDate == null || finishDate == null) {
            view.setRefreshing(false);
            view.onShowToast("Не выбран период");
            return;
        }

        ArrayList<String> chosenStoreIds = getChosenStoreIds();

        if (chosenStoreIds.size() == 0) {
            view.setRefreshing(false);
            view.onShowToast("Не выбрано ни одного магазина");
            return;
        }

        subscriptions.add(getReportList(startDate,finishDate,chosenStoreIds));
    }
    @Override
    public ArrayList<String> getChosenStoreIds() {
        return  chosenStoreList;
    }
    @Override
    public void setChosenStore(String storeId) {

        if (chosenStoreList.contains(storeId)) {
            chosenStoreList.remove(storeId);
        } else {
            chosenStoreList.add(storeId);
        }
    }

    Observable<ArrayList<Store>> getStores(ArrayList<String> storeIds) {
        return Observable.fromCallable(new Callable<ArrayList<Store>>() {

            @Override
            public ArrayList<Store> call() throws Exception {
                if (userId.equals(Constants.EMPTY_ID)) {
                    return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByAnonymous(storeIds);
                }
                return (ArrayList<Store>) db.chosenStoreDao().getAllByIdsByUserId(userId,storeIds);
            }
        });
    }

    @Override
    protected Subscription getStoreList() {

        return getStores(chosenStoreList).subscribeOn(rxSchedulers.runOnBackground())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stores -> {
                            System.out.println("processing ending on thread" + Thread.currentThread().getName());
                            view.onShowStores(stores);
                        }, Utils::handleThrowable
                );

    }

    @Override
    public void onCreate() {
    }

    private Subscription getReportList(Date startDate, Date finishDate, List<String> stores) {

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
        .flatMap(new Func1<Boolean, Observable<? extends ArrayList<SalesData>>>() {
            @Override
            public Observable<? extends ArrayList<SalesData>> call(Boolean isAvailable) {
                return model.getReportList(startDate,finishDate, stores);
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

    private Subscription respondGroupClick() {

        return view.groupClicks().subscribe(data -> view.requestLayout(data));
    }


}
