package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp;

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
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SalesPresenter extends BasePresenter {

    private SalesReportActivity view;
    private SalesModel model;

    public SalesPresenter(DB db,RxSchedulers schedulers, SalesModel model, SalesReportActivity view, CompositeDisposable sub) {
        super(db,sub,schedulers,view);
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void refresh() {

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

        subscriptions.add(getReportList(startDate,finishDate,chosenStoreList));
    }

    @Override
    public void setChosenStore(String storeId) {

        if (chosenStoreList.contains(storeId)) {
            chosenStoreList.remove(storeId);
        } else {
            chosenStoreList.add(storeId);
        }
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

    @Override
    public void onCreate() {
    }

    private Disposable getReportList(Date startDate, Date finishDate, ArrayList<String> stores) {

        return model.isNetworkAvailable().doOnNext(networkAvailable ->{
            if (!networkAvailable) {
                Utils.showSnackbar(view.findViewById(R.id.container), view.getString(R.string.on_error_check_connection), 0);
                view.setRefreshing(false);
            }
        })
        .filter(isNetworkAvailable -> {
                return true;
        })
        .flatMap((Function<Boolean, Observable<? extends ArrayList<SalesData>>>) aBoolean ->
                model.getReportList(startDate,finishDate, stores))
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

    public Disposable respondGroupClick() {
        return view.groupClicks().subscribe(data -> view.requestLayout(data));
    }


}
