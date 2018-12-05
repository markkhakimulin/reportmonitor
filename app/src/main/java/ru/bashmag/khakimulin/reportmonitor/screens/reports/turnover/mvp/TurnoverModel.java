package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Dummy;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_FROM_1C;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_TO_1C;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverModel {

    private DB db;
    private SoapSerializationEnvelope envelope;
    private TimeoutHttpTransport httpTransportSE;
    private SoapObject soapObject;
    private final String method = Constants.SOAP_METHOD_GET_PIVOT_TABLE;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soap_action = String.format("%s#Reports:%s",namespace,method);
    private final String soapActionGetExchangeStatuses = String.format("%s#Reports:%s",namespace,Constants.SOAP_METHOD_GET_EXCHANGE_STATUSES);
    private final String soapActionPingPong = String.format("%s#Reports:%s",namespace,Constants.SOAP_METHOD_PING_PONG);
    private TurnoverReportActivity context;
    private Boolean isDummy = false;

    public TurnoverModel(DB db, TimeoutHttpTransport httpTransportSE,
                         SoapSerializationEnvelope envelope,
                         SoapObject soapObject,
                         TurnoverReportActivity context) {
        this.db = db;
        this.httpTransportSE = httpTransportSE;
        this.envelope = envelope;
        this.soapObject = soapObject;
        this.context = context;
    }

    public TurnoverModel(TurnoverReportActivity context) {
        this.context = context;
        isDummy = true;
    }


    Observable<Store> getStore(String id) {
        return Observable.fromCallable(new Callable<Store>() {
            @Override
            public Store call() throws Exception {
                return db.storeDao().getById(id);
            }
        });
    }


    Observable<ArrayList<TurnoverReportData>> getReportList(Date startDate, Date finishDate, List<String> stores) {


        if (isDummy) {
            return Observable.fromCallable(new Callable<ArrayList<TurnoverReportData>>() {
                @Override
                public ArrayList<TurnoverReportData> call() throws Exception {
                    Thread.sleep(1000);
                    return Dummy.turnoverDummyContent();
                }
            });
        }

        return Observable.fromCallable(new Callable<ArrayList<TurnoverReportData>>() {
            @Override
            public ArrayList<TurnoverReportData> call() throws Exception {
                String errorMessage = "";
                ArrayList<TurnoverReportData> list = new ArrayList<>();

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

                try {

                    httpTransportSE.call(soap_action, envelope);
                }catch (Exception e) {
                    SoapFault responseFault = (SoapFault) envelope.bodyIn;
                    String errorTitle = "",errorCode = "";
                    if (responseFault != null) {
                        if (e instanceof XmlPullParserException) {
                            errorTitle = "Ошибка разбора ответа";
                        }
                        if (e instanceof HttpResponseException) {

                            errorTitle = "Ошибка ответа от сервера";

                        }
                        errorCode = responseFault.faultcode;
                        errorMessage = responseFault.faultstring;
                    }
                    throw new Exception(String.format("%s [%s]:%s ",errorTitle,errorCode,errorMessage));
                }

                SoapObject response = (SoapObject) envelope.getResponse();//PivotTable
                SoapObject turnover = (SoapObject) response.getProperty("Turnover");
                SoapObject turnoverLast = (SoapObject) response.getProperty("TurnoverLast");
                SoapObject conversionLast = (SoapObject) response.getProperty("ConversionLast");

                int count = turnover.getPropertyCount();

                if (turnover.getPropertyCount() == 0) {
                    throw new Exception("Нет данных за выбранный период");
                }

                dateFormat = new SimpleDateFormat(FORMATDATE_FROM_1C, Locale.getDefault());

                try {


                    HashMap<String,TurnoverData> turnoverDataHashMap = new HashMap<>();
                    HashMap<String,TurnoverData> turnoverLastDataHashMap = new HashMap<>();
                    HashMap<String,ConversionData> conversionLastDataHashMap = new HashMap<>();
                    for (int i = 0; i < count; i++) {

                        SoapObject to = (SoapObject) turnover.getProperty(i);
                        if (to.getPropertyCount() == 0) {
                            continue;
                        }
                        TurnoverData turnoverData = new TurnoverData();
                        turnoverData.date = dateFormat.parse(to.getPropertyAsString(Constants.DATE));
                        turnoverData.fact = Double.valueOf(to.getPropertyAsString(Constants.FACT));
                        turnoverData.plan = Double.valueOf(to.getPropertyAsString(Constants.PLAN));
                        turnoverData.storeId = ((SoapObject) to.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                        turnoverData.storeTitle = ((SoapObject) to.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);
                        turnoverDataHashMap.put(turnoverData.storeId,turnoverData);

                    }
                    count = turnoverLast.getPropertyCount();
                    for (int i = 0; i < count; i++) {

                        SoapObject tolast = (SoapObject) turnoverLast.getProperty(i);
                        if (tolast.getPropertyCount() == 0) {
                            continue;
                        }
                        TurnoverData turnoverDataLast = new TurnoverData();
                        turnoverDataLast.date = dateFormat.parse(tolast.getPropertyAsString(Constants.DATE));
                        turnoverDataLast.fact = Double.valueOf(tolast.getPropertyAsString(Constants.FACT));
                        turnoverDataLast.plan = Double.valueOf(tolast.getPropertyAsString(Constants.PLAN));
                        turnoverDataLast.storeId = ((SoapObject) tolast.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                        turnoverDataLast.storeTitle = ((SoapObject) tolast.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);
                        turnoverLastDataHashMap.put(turnoverDataLast.storeId,turnoverDataLast);
                    }
                    count = conversionLast.getPropertyCount();
                    for (int i = 0; i < count; i++) {

                        SoapObject colast = (SoapObject) conversionLast.getProperty(i);
                        if (colast.getPropertyCount() == 0) {
                            continue;
                        }
                        ConversionData conversionDataLast = new ConversionData();
                        conversionDataLast.date = dateFormat.parse(colast.getPropertyAsString(Constants.DATE));
                        conversionDataLast.visitors = Integer.valueOf(colast.getPropertyAsString(Constants.VISITORS));
                        conversionDataLast.items = Integer.valueOf(colast.getPropertyAsString(Constants.ITEMS));
                        conversionDataLast.cheques = Integer.valueOf(colast.getPropertyAsString(Constants.CHEQUES));
                        conversionDataLast.storeId = ((SoapObject) colast.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                        conversionDataLast.storeTitle = ((SoapObject) colast.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);
                        conversionLastDataHashMap.put(conversionDataLast.storeId,conversionDataLast);
                    }

                    for (String storeId:turnoverDataHashMap.keySet()) {

                        TurnoverReportData turnoverReportData = new TurnoverReportData();
                        turnoverReportData.turnover = turnoverDataHashMap.get(storeId);

                        turnoverReportData.turnoverLast = turnoverLastDataHashMap.containsKey(storeId)
                                ?turnoverLastDataHashMap.get(storeId):
                                new TurnoverData();
                        turnoverReportData.conversionLast = conversionLastDataHashMap.containsKey(storeId)
                                ?conversionLastDataHashMap.get(storeId):
                                new ConversionData();

                        list.add(turnoverReportData);

                    }

                }
                catch (Exception e) {
                    throw e;
                }

                return list;
            }
        });
    }


    Observable<ArrayList<Store> > updateStoreList(ArrayList<String> stores) {

        return Observable.fromCallable(new Callable<ArrayList<Store> >() {
            @Override
            public ArrayList<Store>  call() throws Exception {
                String errorMessage = "";

                SoapObject soapObjectStoreList = new SoapObject(Constants.SOAP_NAMESPACE,"StoreList");
                for (String storeId: stores) {
                    SoapObject soapStoreObject = new SoapObject(Constants.SOAP_NAMESPACE, "Store");
                    soapStoreObject.addProperty(Constants.ID, storeId);
                    //остальные необязательно
                    soapObjectStoreList.addSoapObject(soapStoreObject);
                }

                SoapObject soapObject = new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_EXCHANGE_STATUSES);
                soapObject.addProperty("Stores",soapObjectStoreList);
                envelope.setOutputSoapObject(soapObject);
                httpTransportSE.call(soapActionGetExchangeStatuses, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject exchangeStatusList = (SoapObject) response.getProperty("ExchangeStatusList");

                if (response.hasProperty("Error")) {
                    try {
                        errorMessage = ((SoapPrimitive) response.getProperty("Error")).getValue().toString();
                    } catch (Exception e) {
                        //нет ошибки
                    }
                    ;
                }
                int count = exchangeStatusList.getPropertyCount();

                SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATDATE_FROM_1C, Locale.getDefault());
                Calendar period = Calendar.getInstance();

                ArrayList<Store> list = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();

                for (int i = 0; i < count; i++) {

                    SoapObject exchangeStatus = (SoapObject) exchangeStatusList.getProperty(i);

                    SoapObject so = (SoapObject) exchangeStatus.getProperty(Constants.STORE);
                    Store store = new Store();
                    store.id = so.getPropertyAsString(Constants.ID);
                    store.code = so.getPropertyAsString(Constants.CODE);
                    store.description = so.getPropertyAsString(Constants.DESCRIPTION);

                    /*String userId = "";
                    if (so.hasProperty(Constants.USER_ID)) {
                        userId = so.getPropertyAsString(Constants.USER_ID);
                    };*/

                    Date date =  dateFormat.parse(exchangeStatus.getPropertyAsString(Constants.DATE));
                    Date dateLoad =  dateFormat.parse(exchangeStatus.getPropertyAsString(Constants.DATE_LOAD));
                    Date dateUnload =  dateFormat.parse(exchangeStatus.getPropertyAsString(Constants.DATE_UNLOAD));

                    long loadDifferenceHours   = (long)(period.getTimeInMillis() - dateLoad.getTime()) /3600000;
                    long unloadDifferenceHours   = (long)(period.getTimeInMillis() - dateUnload.getTime()) /3600000;
                    store.actual = Math.max(loadDifferenceHours,unloadDifferenceHours) > 2 ? 0 : 1;
                    store.last = date.getTime();
                    store.loaded = dateLoad.getTime();
                    store.unloaded = dateUnload.getTime();
                    //store.user_id = userId;

                    list.add(store);
                    ids.add(store.id);
                }

                return list;
            }
        });
    }

    Observable<Boolean> pingPong() {

        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                envelope.setOutputSoapObject(new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_PING_PONG));
                httpTransportSE.call(soapActionPingPong, envelope,5000);
                SoapObject response = (SoapObject) envelope.getResponse();
                httpTransportSE.defaultTimeout();
                return response.getPropertyAsString("Result").equalsIgnoreCase("OK");
            }
        });
    }
    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(context);
    }

}
