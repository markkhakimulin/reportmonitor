package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.ReportListScope;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListPresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Module
public class TurnoverModule {
    TurnoverReportActivity activity;

    public TurnoverModule(TurnoverReportActivity context) {
        this.activity = context;
    }


    @TurnoverScope
    @Provides
    @Named("PROD")
    TurnoverModel provideModel(HttpTransportSE httpTransportSE,SoapSerializationEnvelope envelope,SoapObject soapObject) {
        return new TurnoverModel(httpTransportSE,envelope,soapObject,activity);
    }

    @TurnoverScope
    @Provides
    @Named("DUMMY")
    TurnoverModel provideModelDummy() {
        return new TurnoverModel(activity);
    }

    @TurnoverScope
    @Provides
    TurnoverPresenter providePresenter(RxSchedulers schedulers,@Named("DUMMY")  TurnoverModel model) {
        CompositeSubscription subscriptions = new CompositeSubscription();
        return new TurnoverPresenter(schedulers, model, activity, subscriptions);
    }

    @TurnoverScope
    @Provides
    TurnoverReportActivity provideContext() {
        return activity;
    }


    @TurnoverScope
    @Provides
    SoapObject provideSoapObject() {
        return new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_STORES);
    }

}

