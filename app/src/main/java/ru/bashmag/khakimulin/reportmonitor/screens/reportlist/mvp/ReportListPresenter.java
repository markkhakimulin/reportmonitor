package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp;

import java.util.ArrayList;

import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ReportListPresenter {

    ReportListActivity view;
    ReportListModel model;
    RxSchedulers rxSchedulers;
    CompositeSubscription subscriptions;
    ArrayList<ReportItem> reportItems = new ArrayList<>();

    public ReportListPresenter(RxSchedulers schedulers, ReportListModel model, ReportListActivity view, CompositeSubscription sub) {
        this.rxSchedulers = schedulers;
        this.view = view;
        this.model = model;
        this.subscriptions = sub;
    }

    public void onCreate() {

        subscriptions.add(getReportList());
        subscriptions.add(respondToClick());
    }
    public void onDestroy() {
        subscriptions.clear();
    }


    private Subscription getReportList() {

        return model.provideReportList().
                subscribeOn(rxSchedulers.runOnBackground()).
                observeOn(rxSchedulers.androidThread()).subscribe(reportList -> {
                    view.setDataList(reportList);
                    reportItems = reportList;
                }, throwable -> {
                    Utils.handleThrowable(throwable);
                }
        );

    }

    private Subscription respondToClick() {

        return view.itemClicks().subscribe(integer -> model.goToReportActivity(reportItems.get(integer)));
    }

}
