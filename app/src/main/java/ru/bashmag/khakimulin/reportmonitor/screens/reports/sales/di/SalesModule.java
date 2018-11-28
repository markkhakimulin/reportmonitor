package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionScope;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp.SalesModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp.SalesPresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di.TurnoverScope;
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
public class SalesModule {
    SalesReportActivity activity;
    private Date startDate,finishDate;
    private String userId;
    private ArrayList<String> chosenStoreList;
    private BasePresenter basePresenter;

    public SalesModule(SalesReportActivity context,Date startDate,Date finishDate,String userId,ArrayList<String> chosenStoreList) {
        this.activity = context;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.userId = userId;
        this.chosenStoreList = chosenStoreList;
    }

    @SalesScope
    @Provides
    @Named("PROD")
    SalesModel provideModel(TimeoutHttpTransport httpTransportSE, SoapSerializationEnvelope envelope, SoapObject soapObject) {
        return new SalesModel(httpTransportSE,envelope,soapObject,activity);
    }

    @SalesScope
    @Provides
    @Named("DUMMY")
    SalesModel provideModelDummy() {
        return new SalesModel(activity);
    }

    @SalesScope
    @Provides
    SalesPresenter providePresenter() {
        return (SalesPresenter) basePresenter;

    }
    @SalesScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @SalesScope
    @Provides
    BasePresenter provideBasePresenter(DB db, RxSchedulers schedulers, @Named("PROD")  SalesModel model,CompositeSubscription subscription) {
        basePresenter = new SalesPresenter(db, schedulers, model,activity,subscription);
        basePresenter.setStartDate(startDate);
        basePresenter.setFinishDate(finishDate);
        basePresenter.setUserId(userId);
        basePresenter.setLocalChosenStoreList(chosenStoreList);
        return basePresenter;
    }


    @SalesScope
    @Provides
    SalesReportActivity provideContext() {
        return activity;
    }


    @SalesScope
    @Provides
    SoapObject provideSoapObject() {
        return new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_SALES);
    }

}

