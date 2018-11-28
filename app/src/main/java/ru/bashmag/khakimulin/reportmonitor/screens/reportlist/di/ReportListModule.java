package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.AppRxSchedulers;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Module
public class ReportListModule {
    ReportListActivity reportListContext;

    public ReportListModule(ReportListActivity context) {
        this.reportListContext = context;
    }

    @ReportListScope
    @Provides
    ReportListModel provideModel() {
        return new ReportListModel(reportListContext);
    }

    @ReportListScope
    @Provides
    ReportListPresenter providePresenter(RxSchedulers schedulers, ReportListModel model) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        return new ReportListPresenter(schedulers, model, reportListContext, subscriptions);
    }

    @ReportListScope
    @Provides
    ReportListActivity provideContext() {
        return reportListContext;
    }
}

