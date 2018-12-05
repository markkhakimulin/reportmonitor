package ru.bashmag.khakimulin.reportmonitor.screens.splash.di;

import android.content.Context;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashModel;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Module
public class SplashModule {

    private BasePresenter basePresenter;
    private SplashActivity splashContext;

    public SplashModule(SplashActivity context) {
        this.splashContext = context;
    }

    @SplashScope
    @Provides
    BasePresenter provideBasePresenter(DB db,
                                       RxSchedulers schedulers,
                                       SplashModel model,
                                       CompositeDisposable subscription) {
        this.basePresenter = new SplashPresenter(db, model, this.splashContext, schedulers, subscription);
        return this.basePresenter;
    }

    @SplashScope
    @Provides
    CompositeDisposable provideCompositeSubscription() {
        return new CompositeDisposable();
    }

    @SplashScope
    @Provides
    SplashPresenter providePresenter() {
        return (SplashPresenter) this.basePresenter;
    }

    @SplashScope
    @Provides
    SplashActivity provideSplashContext() {
        return this.splashContext;
    }

    @SplashScope
    @Provides
    SplashModel provideSplashModel(DB db,
                                   TimeoutHttpTransport httpTransportSE,
                                   SoapSerializationEnvelope envelope,
                                   Context ctx) {
        return new SplashModel(db, httpTransportSE, envelope, ctx);
    }



}
