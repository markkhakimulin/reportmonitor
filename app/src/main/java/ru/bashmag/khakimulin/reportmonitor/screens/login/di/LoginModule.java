package ru.bashmag.khakimulin.reportmonitor.screens.login.di;

import android.content.Context;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.login.mvp.LoginModel;
import ru.bashmag.khakimulin.reportmonitor.screens.login.mvp.LoginPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Module
public class LoginModule {

    private LoginActivity context;
    private BasePresenter basePresenter;

    public LoginModule(LoginActivity context) {
        this.context = context;
    }

    @LoginScope
    @Provides
    LoginActivity provideSplashContext() {
        return context;
    }

    @LoginScope
    @Provides
    LoginPresenter providePresenter() {
        return (LoginPresenter)basePresenter;
    }
    @LoginScope
    @Provides
    BasePresenter provideBasePresenter(DB db, RxSchedulers schedulers, LoginModel model, CompositeSubscription subscription) {
        basePresenter =  new LoginPresenter(db,model,context, schedulers, subscription);
        return basePresenter;
    }

    @LoginScope
    @Provides
    LoginModel provideSplashModel(DB db, TimeoutHttpTransport httpTransportSE, SoapSerializationEnvelope envelope, Context ctx) {
        return new LoginModel(db, httpTransportSE, envelope, ctx);
    }
    @LoginScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }




}
