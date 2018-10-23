package ru.bashmag.khakimulin.reportmonitor.core.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.inject.Named;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

@AppScope
@Component(modules = AppModule.class)
public interface AppComponent {

    HttpTransportSE provideHttpTransport();
    DB provideDB();
    Resources provideResources();
    SharedPreferences provideSharedPreference();
    Context provideContext();
    RxSchedulers provideRxSchedulers();
    SoapSerializationEnvelope provideSoapSerializationEnvelope();
   /* @Named("regular")
    Typeface getTypefaceRegular();

    @Named("light")
    Typeface getTypefaceLight();*/


}