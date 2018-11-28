package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp;

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

import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;
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
public class SalesModel {

    private SoapSerializationEnvelope envelope;
    private HttpTransportSE httpTransportSE;
    private SoapObject soapObject;
    private final String method = Constants.SOAP_METHOD_GET_SALES;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soap_action = String.format("%s#Reports:%s",namespace,method);
    private SalesReportActivity context;
    private Boolean isDummy = false;

    public SalesModel(HttpTransportSE httpTransportSE,
                      SoapSerializationEnvelope envelope,
                      SoapObject soapObject,
                      SalesReportActivity context) {

        this.httpTransportSE = httpTransportSE;
        this.envelope = envelope;
        this.soapObject = soapObject;
        this.context = context;
    }

    public SalesModel(SalesReportActivity context) {
        this.context = context;
        isDummy = true;
    }

    Observable<ArrayList<SalesData>> getReportList(Date startDate, Date finishDate, List<String> stores) {


        if (isDummy) {
            return Observable.fromCallable(new Callable<ArrayList<SalesData>>() {
                @Override
                public ArrayList<SalesData> call() throws Exception {

                    ArrayList<SalesData> list = new ArrayList<>();
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,new Date())}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    list.add(new SalesData("123123","Балашиха","Алеша","Продавец",new TurnoverData[]{
                            new TurnoverData("123123","Балашиха",12234,13355,"Обувь"),
                            new TurnoverData("123123","Балашиха",551,125,"Аксессуары"),
                            new TurnoverData("123123","Балашиха",551,125,"Всякая хрень")}));
                    return list;
                }
            });
        }

        return Observable.fromCallable(new Callable<ArrayList<SalesData>>() {
            @Override
            public ArrayList<SalesData> call() throws Exception {
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
                SoapObject conversionListSoapObject = (SoapObject) response.getProperty("SalesList");
                int count = conversionListSoapObject.getPropertyCount();


                ArrayList<SalesData> list = new ArrayList<>();

                dateFormat = new SimpleDateFormat(FORMATDATE_FROM_1C, Locale.getDefault());
                for (int i = 0; i < count; i++) {

                    SoapObject so = (SoapObject) conversionListSoapObject.getProperty(i);
                    SalesData data = new SalesData();
                    data.position = so.getPropertyAsString(Constants.POSITION);
                    data.employee = so.getPropertyAsString(Constants.EMPLOYEE);
                    data.storeId = ((SoapObject)so.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                    data.storeTitle = ((SoapObject)so.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);

                    SoapObject turnoverListSoapObject = (SoapObject)so.getProperty(Constants.TURNOVERLIST);
                    TurnoverData[] turnoverDataList = new TurnoverData[turnoverListSoapObject.getPropertyCount()];
                    for (int j = 0;j<turnoverListSoapObject.getPropertyCount();j++) {

                        SoapObject to = (SoapObject) turnoverListSoapObject.getProperty(j);
                        turnoverDataList[j] = new TurnoverData();
                        turnoverDataList[j].storeTitle = ((SoapObject)to.getProperty(Constants.STORE)).getPropertyAsString(Constants.DESCRIPTION);
                        turnoverDataList[j].storeId = ((SoapObject)to.getProperty(Constants.STORE)).getPropertyAsString(Constants.ID);
                        turnoverDataList[j].group = to.getPropertyAsString(Constants.GROUP);
                        turnoverDataList[j].fact = Double.valueOf(to.getPropertyAsString(Constants.FACT));
                        turnoverDataList[j].plan =  Double.valueOf(to.getPropertyAsString(Constants.PLAN));
                    }
                    data.turnoverDataList = turnoverDataList;

                    list.add(data);
                }
                return list;
            }
        });
    }


    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(context);
    }

}
