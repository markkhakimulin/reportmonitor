package ru.bashmag.khakimulin.reportmonitor.screens.splash.di;

import android.content.Context;
import android.graphics.Typeface;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppScope;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashModel;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.AppRxSchedulers;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Module
public class SplashModule {


    private SplashActivity splashContext;

    public SplashModule(SplashActivity context) {
        this.splashContext = context;
    }

    @SplashScope
    @Provides
    SplashActivity provideSplashContext() {
        return splashContext;
    }
    @SplashScope
    @Provides
    SplashPresenter providePresenter(RxSchedulers schedulers, SplashModel model) {
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        return new SplashPresenter(model,splashContext, schedulers, compositeSubscription);
    }

    @SplashScope
    @Provides
    SplashModel provideSplashModel(DB db,HttpTransportSE httpTransportSE,SoapSerializationEnvelope envelope,SoapObject soapObject, Context ctx) {
        return new SplashModel(db, httpTransportSE, envelope,soapObject, ctx);
    }

    @SplashScope
    @Provides
    SoapObject provideSoapObject() {

        return new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_STORES);
    }




}
