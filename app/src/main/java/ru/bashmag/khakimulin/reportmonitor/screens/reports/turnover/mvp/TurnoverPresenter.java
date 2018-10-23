package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverPresenter {

    TurnoverReportActivity view;
    TurnoverModel model;
    RxSchedulers rxSchedulers;
    CompositeSubscription subscriptions;
    ArrayList<TurnoverReportData> reportItems = new ArrayList<>();

    public TurnoverPresenter(RxSchedulers schedulers, TurnoverModel model, TurnoverReportActivity view, CompositeSubscription sub) {
        this.rxSchedulers = schedulers;
        this.view = view;
        this.model = model;
        this.subscriptions = sub;
    }

    public void onCreate() {
        subscriptions.add(respondToMonthFactTurnoverClick());
        subscriptions.add(respondToMonthPlanTurnoverClick());
        subscriptions.add(respondToDailyFactTurnoverClick());
        subscriptions.add(respondToDailyPlanTurnoverClick());
        subscriptions.add(respondToConversionTurnoverClick());
        subscriptions.add(respondToFullnessTurnoverClick());
    }
    public void onDestroy() {
        subscriptions.clear();
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
        subscriptions.add(getReportList(startDate,finishDate,stores));
    }

    private Subscription getReportList(Date startDate, Date finishDate, List<String> stores) {

        return model.isNetworkAvailable().doOnNext(networkAvailable -> {
            if (!networkAvailable) {
                view.onShowToast("Интернет соединение недоступно");
            }
        }).
            filter(isNetworkAvailable -> true).
            flatMap(isAvailable -> model.getReportList(startDate,finishDate,stores)).
            subscribeOn(rxSchedulers.internet()).
            observeOn(rxSchedulers.androidThread()).
            subscribe(reportList -> {
                view.setDataList(reportList);
                reportItems = reportList;
            }, throwable -> {
                Utils.handleThrowable(throwable);
            }
        );
    }

    private Subscription respondToMonthFactTurnoverClick() {

        return view.monthFactClicks().subscribe(new Action1<TurnoverData>() {
            @Override
            public void call(TurnoverData data) {
                view.gotoMonthlyPlan(data);
            }
        });
    }

    private Subscription respondToMonthPlanTurnoverClick() {

        return view.monthPlanClicks().subscribe(new Action1<TurnoverData>() {
            @Override
            public void call(TurnoverData data) {
               view.gotoMonthlyFact(data);
            }
        });
    }

    private Subscription respondToDailyPlanTurnoverClick() {

        return view.dailyPlanClicks().subscribe(new Action1<TurnoverData>() {
            @Override
            public void call(TurnoverData data) {
                view.gotoDailyPlan(data);
            }
        });
    }

    private Subscription respondToDailyFactTurnoverClick() {

        return view.dailyFactClicks().subscribe(new Action1<TurnoverData>() {
            @Override
            public void call(TurnoverData data) {
                view.gotoDailyFact(data);
            }
        });
    }
    private Subscription respondToConversionTurnoverClick() {

        return view.conversionClicks().subscribe(new Action1<ConversionData>() {
            @Override
            public void call(ConversionData data) {
                view.gotoConversion(data);
            }
        });
    }
    private Subscription respondToFullnessTurnoverClick() {

        return view.conversionClicks().subscribe(new Action1<ConversionData>() {
            @Override
            public void call(ConversionData data) {
                view.gotoFullness(data);
            }
        });
    }

}
