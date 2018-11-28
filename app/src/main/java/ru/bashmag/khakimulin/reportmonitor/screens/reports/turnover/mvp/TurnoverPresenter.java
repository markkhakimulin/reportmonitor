package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp;

import android.support.design.widget.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_FROM_1C;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_TO_1C;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverPresenter extends BasePresenter {

    TurnoverReportActivity view;
    TurnoverModel model;
    ArrayList<TurnoverReportData> reportItems = new ArrayList<>();

    public TurnoverPresenter(DB db, RxSchedulers schedulers, TurnoverModel model, TurnoverReportActivity view, CompositeSubscription sub) {
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

    /**
     * Called when the fragment has been refreshed by user from  {@link ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment}
     * startDate start report date , non-null
     * finishDate finish report date , non-null
     * stores selected store ids, if null - all stores id.
     *
     * @return Observable{@link ArrayList<ConversionData>}
     */
    public void refresh() {

        ArrayList<String> chosenStoreIds = getChosenStoreIds();

        if (chosenStoreIds.size() == 0) {
            view.setRefreshing(false);
            view.onShowToast("Не выбрано ни одного магазина");
            return;
        }
        //subscriptions.add(updateExchangeStatuses(chosenStoreIds));
        subscriptions.add(getReportList(startDate,finishDate,chosenStoreIds));
    }

    private Subscription getReportList(Date startDate, Date finishDate, ArrayList<String> stores) {

        return model.isNetworkAvailable().doOnNext(networkAvailable -> {
            if (!networkAvailable) {
                view.onShowToast("Интернет соединение недоступно");
            }
        }).
            filter(isNetworkAvailable -> true).
            flatMap((Func1<Boolean, Observable<Boolean>>) isAvailable -> {
            if (isAvailable)
                return model.pingPong();
            else
                return Observable.just(false);
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
                Utils.showSnackbar(view.getCurrentFocus(),throwable.getMessage(),Snackbar.LENGTH_LONG);
                Utils.handleThrowable(throwable);
            }
        );
    }

    private Subscription updateExchangeStatuses(ArrayList<String> stores) {

        return model.updateStoreList(stores).
                subscribeOn(rxSchedulers.internet()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<ArrayList<Store> >() {
                              @Override
                              public void call(ArrayList<Store>  storeUpdateList) {
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

                              }
                          }, throwable -> {
                            processThrow(throwable);
                            view.setRefreshing(false);
                        }
                );
    }

    private Store getStore(ArrayList<Store> list ,String storeId) {
        for (Store store:list) {
            if (store.id.equals(storeId)) {
                return store;
            }
        }
        return null;
    }

    private Subscription respondToStoreTurnoverClick() {
        return view.storeClicks().subscribe(storeId -> {


            model.getStore(storeId).
                subscribeOn(rxSchedulers.runOnBackground()).
                observeOn(rxSchedulers.androidThread()).
                subscribe(new Action1<Store>() {

                    @Override
                    public void call(Store store) {

                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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
                    }
                });
        });
    }

    private Subscription respondToDailyPlanTurnoverClick() {

        return view.dailyPlanClicks().subscribe(data -> view.gotoDailyPlan(data));
    }

    private Subscription respondToDailyFactTurnoverClick() {

        return view.dailyFactClicks().subscribe(data -> view.gotoDailyFact(data));
    }
    private Subscription respondToConversionTurnoverClick() {

        return view.conversionClicks().subscribe(data -> view.gotoConversion(data));
    }
    private Subscription respondToFullnessTurnoverClick() {

        return view.fullnessClicks().subscribe(data -> view.gotoFullness(data));
    }

}
