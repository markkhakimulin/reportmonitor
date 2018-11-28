package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashModel;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Module
public class ConversionModule {


    private ConversionReportActivity context;

    public ConversionModule(ConversionReportActivity context) {
        this.context = context;
    }

    @ConversionScope
    @Provides
    ConversionReportActivity provideSplashContext() {
        return context;
    }

    @ConversionScope
    @Provides
    ConversionPresenter providePresenter(RxSchedulers schedulers, ConversionModel model, CompositeSubscription compositeSubscription) {
        return new ConversionPresenter(model,context, schedulers, compositeSubscription);
    }

    @ConversionScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @ConversionScope
    @Provides
    ConversionModel provideSplashModel(DB db,HttpTransportSE httpTransportSE,SoapSerializationEnvelope envelope) {
        return new ConversionModel(db, httpTransportSE, envelope, context);
    }


}
