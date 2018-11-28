package ru.bashmag.khakimulin.reportmonitor.core.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import dagger.Module;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.AppRxSchedulers;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @AppScope
    @Provides
    public HttpTransportSE provideHttpTransportSE() {
        return new HttpTransportSE(Constants.SOAP_URL,60000);
    }
    @AppScope
    @Provides
    public Resources provideResources() {
        return mApplication.getResources();
    }

    @AppScope
    @Provides
    Context provideContext() {
        return mApplication;
    }

    @AppScope
    @Provides
    DB provideDBHelper() {
        return Room.databaseBuilder(mApplication, DB.class, Constants.DB)
                .build();
    }
    @AppScope
    @Provides
    public SharedPreferences provideSharedPreferences() {
        return mApplication.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    }

    @AppScope
    @Provides
    public RxSchedulers provideRxSchedulers() {
        return new AppRxSchedulers();
    }

    @AppScope
    @Provides
    SoapSerializationEnvelope provideSoapSerializationEnvelope() {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        return envelope;
    }


}
