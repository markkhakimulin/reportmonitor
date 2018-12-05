package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMAT_DATE_HOUR;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverPresenter extends BasePresenter {

    TurnoverReportActivity view;
    TurnoverModel model;
    ArrayList<TurnoverReportData> reportItems = new ArrayList<>();

    public TurnoverPresenter(DB db, RxSchedulers schedulers, TurnoverModel model, TurnoverReportActivity view, CompositeDisposable sub) {
        super(db,sub,schedulers,view);
        this.view = view;
        this.model = model;
    }

    public void onCreate() {

        subscriptions.add(respondToDailyFactTurnoverClick());
        subscriptions.add(respondToDailyPlanTurnoverClick());
        subscriptions.add(respondToConversionTurnoverClick());
        subscriptions.add(respondToFullnessTurnoverClick());
        subscriptions.add(respondToStoreTurnoverClick());
    }

    public void refresh() {

        checkForChosenStoreIds(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> chosenStoreIds) {
                if (chosenStoreIds.size() == 0) {
                    view.setRefreshing(false);
                    view.onShowToast(view.getString(R.string.on_error_chosen_stores));
                    return;
                }
                if (getStartDate() == null || getFinishDate() == null) {
                    view.setRefreshing(false);
                    view.onShowToast(view.getString(R.string.on_error_chosen_period));
                    return;
                }
                subscriptions.add(getReportList(startDate,finishDate,(ArrayList<String>) chosenStoreIds));
            }

            @Override
            public void onError(Throwable e) {
                processThrow(e);
            }
        });
    }

    private Disposable getReportList(Date startDate, Date finishDate, ArrayList<String> stores) {

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
            .flatMap(isPingPong -> model.getReportList(startDate,finishDate,stores)).
            subscribeOn(rxSchedulers.internet()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe(reportList -> {
                reportItems = reportList;
                view.setDataList(reportItems);
                subscriptions.add(updateExchangeStatuses(stores));
            }, throwable -> {
                view.setRefreshing(false);
                processThrow(throwable);
            }
        );
    }

    private Disposable updateExchangeStatuses(ArrayList<String> stores) {

        return model.updateStoreList(stores).
            subscribeOn(rxSchedulers.internet()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe(storeUpdateList -> {
                if (!storeUpdateList.isEmpty()) {

                    for (TurnoverReportData turnoverReportData:reportItems) {

                        Store store= getStore(storeUpdateList,turnoverReportData.turnover.storeId);
                        if (store != null) {
                            turnoverReportData.turnoverLast.actual = store.actual;
                            turnoverReportData.turnover.actual = store.actual;
                        }
                    }
                    view.updateStatusList(reportItems);
                }
                subscriptions.add(updateExchangeStatuses(stores));
            }, throwable -> {
                processThrow(throwable);
                view.setRefreshing(false);
            });
    }

    private Store getStore(ArrayList<Store> list ,String storeId) {
        for (Store store:list) {
            if (store.id.equals(storeId)) {
                return store;
            }
        }
        return null;
    }

    private Disposable respondToStoreTurnoverClick() {
        return view.storeClicks().subscribe(storeId -> {

            model.getStore(storeId).
                subscribeOn(rxSchedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(store -> {
                    DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_HOUR, Locale.getDefault());

                    view.showYesNoMessageDialog("Состояние обменов",
                            String.format("%s : %s \n%s : %s\n%s : %s",
                                    "Последний обмен",
                                    dateFormat.format(store.last),
                                    "Загрузка из магазина",
                                    dateFormat.format(store.loaded),
                                    "Выгрузка в магазин",
                                    dateFormat.format(store.unloaded)),
                            null,
                            null);
                });
        });
    }

    private Disposable respondToDailyPlanTurnoverClick() {

        return view.dailyPlanClicks().subscribe(data -> view.gotoDailyFact(data));
    }

    private Disposable respondToDailyFactTurnoverClick() {

        return view.dailyFactClicks().subscribe(data -> view.gotoDailyFact(data));
    }
    private Disposable respondToConversionTurnoverClick() {

        return view.conversionClicks().subscribe(data -> view.gotoConversion(data));
    }
    private Disposable respondToFullnessTurnoverClick() {

        return view.fullnessClicks().subscribe(data -> view.gotoFullness(data));
    }

}
