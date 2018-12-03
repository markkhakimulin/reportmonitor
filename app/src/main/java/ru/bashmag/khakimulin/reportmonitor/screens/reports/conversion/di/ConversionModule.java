package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.util.ArrayList;
import java.util.Date;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Module
public class ConversionModule {
    private BasePresenter basePresenter;
    private ArrayList<String> chosenStoreList;
    private ConversionReportActivity context;
    private Date finishDate;
    private Date startDate;
    private String userId;

    public ConversionModule(ConversionReportActivity context, Date startDate, Date finishDate, String userId, ArrayList<String> chosenStoreList) {
        this.context = context;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.userId = userId;
        this.chosenStoreList = chosenStoreList;
    }

    @ConversionScope
    @Provides
    BasePresenter provideBasePresenter(DB db, RxSchedulers schedulers, ConversionModel model, CompositeSubscription subscription) {
        this.basePresenter = new ConversionPresenter(db, model, this.context, schedulers, subscription);
        this.basePresenter.setStartDate(this.startDate);
        this.basePresenter.setFinishDate(this.finishDate);
        this.basePresenter.setUserId(this.userId);
        this.basePresenter.setLocalChosenStoreList(this.chosenStoreList);
        return this.basePresenter;
    }

    @ConversionScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @ConversionScope
    @Provides
    ConversionPresenter providePresenter() {
        return (ConversionPresenter) this.basePresenter;
    }

    @ConversionScope
    @Provides
    ConversionReportActivity provideSplashContext() {
        return this.context;
    }

    @ConversionScope
    @Provides
    ConversionModel provideSplashModel(DB db, TimeoutHttpTransport httpTransportSE, SoapSerializationEnvelope envelope) {
        return new ConversionModel(db, httpTransportSE, envelope, this.context);
    }
}