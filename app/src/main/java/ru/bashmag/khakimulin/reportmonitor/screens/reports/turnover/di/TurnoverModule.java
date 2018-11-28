package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di;

import android.content.SharedPreferences;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionScope;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_FINISHDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_STARTDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_TITLE_PREFERENCES;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_TYPE_PREFERENCES;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Module
public class TurnoverModule {
    private TurnoverReportActivity activity;
    private BasePresenter basePresenter;

    public TurnoverModule(TurnoverReportActivity context) {
        this.activity = context;
    }

    @TurnoverScope
    @Provides
    @Named("PROD")
    TurnoverModel provideModel(DB db,TimeoutHttpTransport httpTransportSE, SoapSerializationEnvelope envelope, SoapObject soapObject) {
        return new TurnoverModel(db,httpTransportSE,envelope,soapObject,activity);
    }

    @TurnoverScope
    @Provides
    @Named("DUMMY")
    TurnoverModel provideModelDummy() {
        return new TurnoverModel(activity);
    }

    @TurnoverScope
    @Provides
    TurnoverPresenter providePresenter() {

        return (TurnoverPresenter)basePresenter;
    }

    @TurnoverScope
    @Provides
    BasePresenter provideBasePresenter(DB db,SharedPreferences preferences, RxSchedulers schedulers, @Named("PROD")  TurnoverModel model,CompositeSubscription subscription) {

        if (preferences.getLong(Constants.PERIOD_STARTDATE,0) == 0 || preferences.getLong(Constants.PERIOD_FINISHDATE,0) == 0) {
            //дефолтный период
            DateFormat dateFormat = new SimpleDateFormat(FORMATDATE,Locale.getDefault());

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            Date finishDate = calendar.getTime();
            calendar.setTime(finishDate);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            Date startDate = Utils.getStartOfADay(calendar.getTime());

            String currentPeriodTitle = dateFormat.format(startDate) +"-"+dateFormat.format(finishDate);

            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(PERIOD_TYPE_PREFERENCES,R.id.action_period_month_filter);
            edit.putString(PERIOD_TITLE_PREFERENCES,currentPeriodTitle);
            edit.putLong(PERIOD_STARTDATE,startDate.getTime());
            edit.putLong(PERIOD_FINISHDATE,finishDate.getTime());
            edit.apply();

        }
        //записываем начальные данные для запуска приложения
        //CompositeSubscription subscriptions = new CompositeSubscription();
        basePresenter = new TurnoverPresenter(db,schedulers, model, activity, subscription);
        basePresenter.setStartDate(new Date(preferences.getLong(Constants.PERIOD_STARTDATE,0)));
        basePresenter.setFinishDate(new Date(preferences.getLong(Constants.PERIOD_FINISHDATE,0)));
        basePresenter.setUserId( preferences.getString(Constants.USER_ID_PREFERENCES,Constants.EMPTY_ID));
        basePresenter.setUserTitle(preferences.getString(Constants.USER_TITLE_PREFERENCES,activity.getString(R.string.anonymous)));

        return basePresenter;
    }

    @TurnoverScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @TurnoverScope
    @Provides
    TurnoverReportActivity provideContext() {
        return activity;
    }

    @TurnoverScope
    @Provides
    SoapObject provideSoapObject() {
        return new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_PIVOT_TABLE);
    }

}

