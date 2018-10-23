package ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SplashModel {


    private DB db;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE httpTransportSE;
    private Context context;
    private SoapObject soapObject;
    private final String method = Constants.SOAP_METHOD_GET_STORES;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soap_action = String.format("%s#Reports:%s",namespace,method);



    public SplashModel(DB db,
                       HttpTransportSE httpTransportSE,
                       SoapSerializationEnvelope envelope,
                       SoapObject soapObject,
                       Context appContext) {
        this.db = db;
        this.envelope = envelope;
        this.httpTransportSE = httpTransportSE;
        this.context = appContext;
        this.soapObject = soapObject;

    }

    Observable<String> getStoreList() {

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String errorMessage = "";

                    envelope.setOutputSoapObject(soapObject);
                    httpTransportSE.call(soap_action, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    int count = response.getPropertyCount();
                    if (count > 0) {
                        //clear whole table
                        List<Store> storeList = db.storeDao().getAll();
                        if (storeList.size() > 0)
                            db.storeDao().deleteAll(storeList);
                    }

                    for (int i = 0; i < count; i++) {

                        SoapObject so = (SoapObject) response.getProperty(i);
                        Store store = new Store();
                        store.id = so.getPropertyAsString(Constants.ID);
                        store.code = so.getPropertyAsString(Constants.CODE);
                        store.description = so.getPropertyAsString(Constants.DESCRIPTION);

                        db.storeDao().insert(store);
                    }

                return errorMessage;
            }
        });
    }


    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(context);
    }



}