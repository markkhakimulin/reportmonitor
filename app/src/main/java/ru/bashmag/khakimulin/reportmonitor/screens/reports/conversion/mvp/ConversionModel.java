package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp;

import android.content.Context;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_FROM_1C;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_TO_1C;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionModel {


    private DB db;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE httpTransportSE;
    private SoapObject soapObject;
    private Context context;

    public ConversionModel(DB db, HttpTransportSE httpTransportSE, SoapSerializationEnvelope envelope, Context context) {
        this.db = db;
        this.envelope = envelope;
        this.httpTransportSE = httpTransportSE;
        this.context = context;

    }

    Observable<ArrayList<Store>> getStores() {
     return Observable.fromCallable(new Callable<ArrayList<Store>>() {

         @Override
         public ArrayList<Store> call() throws Exception {
             return (ArrayList<Store>) db.storeDao().getAll();
         }
     });
    }

    Observable<ArrayList<ConversionData>> getConversion(Date startDate,Date finishDate,List<String> stores,String soapMethod) {

        return Observable.fromCallable(new Callable<ArrayList<ConversionData>>() {
            @Override
            public ArrayList<ConversionData> call() throws Exception {
                String errorMessage = "";
                DateFormat dateFormat = new SimpleDateFormat(FORMATDATE_TO_1C);

                soapObject = new SoapObject(Constants.SOAP_NAMESPACE, soapMethod);
                soapObject.addProperty("StartPeriod", dateFormat.format(startDate)+"000000");
                soapObject.addProperty("EndPeriod",dateFormat.format(finishDate)+"235959");

                SoapObject soapObjectStoreList = new SoapObject(Constants.SOAP_NAMESPACE,"StoreList");
                for (String storeId: stores) {
                    SoapObject soapStoreObject = new SoapObject(Constants.SOAP_NAMESPACE, "Store");
                    soapStoreObject.addProperty(Constants.ID, storeId);
                    //остальные необязательно
                    soapObjectStoreList.addSoapObject(soapStoreObject);
                }
                soapObject.addProperty("Stores",soapObjectStoreList);

                envelope.setOutputSoapObject(soapObject);

                httpTransportSE.call(String.format("%s#Reports:%s",Constants.SOAP_NAMESPACE,soapMethod), envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject conversionListSoapObject = (SoapObject) response.getProperty("ConversionList");
                int count = conversionListSoapObject.getPropertyCount();

                ArrayList<ConversionData> list = new ArrayList<>();

                dateFormat = new SimpleDateFormat(FORMATDATE_FROM_1C, Locale.getDefault());
                for (int i = 0; i < count; i++) {

                    SoapObject so = (SoapObject) conversionListSoapObject.getProperty(i);
                    ConversionData conversionData = new ConversionData();
                    conversionData.date = dateFormat.parse(so.getPropertyAsString(Constants.DATE));
                    conversionData.visitors = Integer.valueOf(so.getPropertyAsString(Constants.VISITORS));
                    conversionData.items = Integer.valueOf(so.getPropertyAsString(Constants.ITEMS));
                    conversionData.cheques = Integer.valueOf(so.getPropertyAsString(Constants.CHEQUES));
                    conversionData.storeId = ((SoapObject)so.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                    conversionData.storeTitle = ((SoapObject)so.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);

                    list.add(conversionData);
                }
                return list;
            }
        });
    }

    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(context);
    }



}