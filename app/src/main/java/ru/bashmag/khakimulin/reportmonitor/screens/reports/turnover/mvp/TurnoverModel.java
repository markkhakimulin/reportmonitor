package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp;

import android.content.Context;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_FROM_1C;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_TO_1C;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverModel {

    private SoapSerializationEnvelope envelope;
    private HttpTransportSE httpTransportSE;
    private SoapObject soapObject;
    private final String method = Constants.SOAP_METHOD_GET_STORES;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soap_action = String.format("%s#Reports:%s",namespace,method);
    private TurnoverReportActivity context;
    private Boolean isDummy = false;

    public TurnoverModel(HttpTransportSE httpTransportSE,
                         SoapSerializationEnvelope envelope,
                         SoapObject soapObject,
                         TurnoverReportActivity context) {

        this.httpTransportSE = httpTransportSE;
        this.envelope = envelope;
        this.soapObject = soapObject;
        this.context = context;
    }

    public TurnoverModel(TurnoverReportActivity context) {
        this.context = context;
        isDummy = true;
    }

    Observable<ArrayList<TurnoverReportData>> getReportList(Date startDate, Date finishDate, List<String> stores) {


        if (isDummy) {
            return Observable.fromCallable(new Callable<ArrayList<TurnoverReportData>>() {
                @Override
                public ArrayList<TurnoverReportData> call() throws Exception {

                    ArrayList<TurnoverReportData> list = new ArrayList<>();
                    list.add(new TurnoverReportData(
                            new TurnoverData("123123","Балашиха",12234,13355,new Date()),
                            new TurnoverData("123123","Балашиха",551,125,new Date()),
                            new ConversionData("123123","Балашиха",125,25,50,new Date())));
                    list.add(new TurnoverReportData(
                            new TurnoverData("456456","Балашиха-2",4554,5665,new Date()),
                            new TurnoverData("456456","Балашиха-2",322,455,new Date()),
                            new ConversionData("456456","Балашиха-2",33,15,45,new Date())));
                    list.add(new TurnoverReportData(
                            new TurnoverData("789789","Бутово",666,333,new Date()),
                            new TurnoverData("789789","Бутово",120,150,new Date()),
                            new ConversionData("789789","Бутово",1204,59,120,new Date())));
                    return list;
                }
            });
        }

        return Observable.fromCallable(new Callable<ArrayList<TurnoverReportData>>() {
            @Override
            public ArrayList<TurnoverReportData> call() throws Exception {
                String errorMessage = "";
                DateFormat dateFormat = new SimpleDateFormat(FORMATDATE_TO_1C);


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

                httpTransportSE.call(soap_action,envelope);

                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject conversionListSoapObject = (SoapObject) response.getProperty("ConversionList");
                int count = conversionListSoapObject.getPropertyCount();


                ArrayList<TurnoverReportData> list = new ArrayList<>();

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

                    list.add(new TurnoverReportData());
                }
                return list;
            }
        });
    }


    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(context);
    }

}
