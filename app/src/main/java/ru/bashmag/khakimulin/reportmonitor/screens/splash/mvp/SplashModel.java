package ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp;

import android.content.Context;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.db.tables.UserStore;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.DEFAULT_TIMEOUT;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_FROM_1C;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SplashModel {


    private DB db;
    private SoapSerializationEnvelope envelope;
    private TimeoutHttpTransport httpTransportSE;
    private Context context;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soapActionGetExchangeStatuses = String.format("%s#Reports:%s", Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_EXCHANGE_STATUSES);
    private final String soapActionGetUsers = String.format("%s#Reports:%s",Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_USERS);
    private final String soapActionPingPong = String.format("%s#Reports:%s",Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_PING_PONG);

    public SplashModel(DB db,
                       TimeoutHttpTransport httpTransportSE,
                       SoapSerializationEnvelope envelope,
                       Context appContext) {
        this.db = db;
        this.envelope = envelope;
        this.httpTransportSE = httpTransportSE;
        this.context = appContext;

    }

    Observable<String> getStores() {

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String errorMessage = "";

                SoapObject soapObject = new SoapObject(namespace, Constants.SOAP_METHOD_GET_EXCHANGE_STATUSES);
                SoapObject soapObjectStoreList = new SoapObject(Constants.SOAP_NAMESPACE,Constants.STORELIST);

                soapObject.addProperty(Constants.STORES,soapObjectStoreList);
                envelope.setOutputSoapObject(soapObject);

                httpTransportSE.call(soapActionGetExchangeStatuses, envelope,DEFAULT_TIMEOUT);

                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject exchangeStatusList = (SoapObject)response.getProperty(Constants.EXCHANGESTATUSLIST);
                if (response.hasProperty(Constants.ERROR)) {
                    if (!response.getPropertyAsString(Constants.ERROR).contentEquals(Constants.EmptyField)) {
                        errorMessage = response.getPropertyAsString(Constants.ERROR);
                    }
                }

                int count = exchangeStatusList.getPropertyCount();
                if (count > 0 && errorMessage.isEmpty()) {

                    SimpleDateFormat format = new SimpleDateFormat(FORMATDATE_FROM_1C, Locale.getDefault());
                    Long currentTime = Calendar.getInstance().getTimeInMillis();
                    ArrayList<String> storeIds = new ArrayList<>();

                    for (int i = 0; i < count; i++) {

                        SoapObject so = (SoapObject) exchangeStatusList.getProperty(i);
                        SoapObject storeObject = (SoapObject)  so.getProperty(Constants.STORE);
                        Store store = new Store();
                        store.id = storeObject.getPropertyAsString(Constants.ID);
                        store.code = storeObject.getPropertyAsString(Constants.CODE);
                        store.description = storeObject.getPropertyAsString(Constants.DESCRIPTION);
                        Date lastExchange = format.parse(so.getPropertyAsString(Constants.DATE));
                        Date lastLoad = format.parse(so.getPropertyAsString(Constants.DATE_LOAD));
                        Date lastUnLoad = format.parse(so.getPropertyAsString(Constants.DATE_UNLOAD));


                        Long loadTime = (currentTime - lastLoad.getTime())/3600000;
                        Long unLoadTime = (currentTime - lastUnLoad.getTime())/3600000;

                        Long max = Math.max(loadTime, unLoadTime);

                        store.actual = max <= 2 ? 1 : 0;
                        store.loaded = lastLoad.getTime();
                        store.unloaded = lastUnLoad.getTime();
                        store.last = lastExchange.getTime();

                        db.storeDao().insert(store);
                        storeIds.add(store.id);
                    }

                    db.storeDao().deleteAllExcept(storeIds);
                }
                return errorMessage;
            }
        });
    }

    Observable<Boolean> pingPong() {
        return Observable.fromCallable(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                envelope.setOutputSoapObject(new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_PING_PONG));
                httpTransportSE.call(soapActionPingPong,envelope, 5000);
                SoapObject response = (SoapObject) envelope.getResponse();
                return response.getPropertyAsString(Constants.RESULT).equalsIgnoreCase("OK");
            }
        });
    }

    public Observable<Boolean> isNetworkAvailable() {
        return Utils.isNetworkAvailableObservable(this.context);
    }

    Observable<String> getUsers() {
        return Observable.fromCallable(new Callable<String>() {
            public String call() throws Exception {
                String errorMessage = "";
                envelope.setOutputSoapObject(new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_GET_USERS));
                httpTransportSE.call(soapActionGetUsers,envelope, 10000);
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject userList = (SoapObject) response.getProperty(Constants.USERLIST);
                if (response.hasProperty(Constants.ERROR)) {
                    if (!response.getPropertyAsString(Constants.ERROR).contentEquals(Constants.EmptyField)) {
                        errorMessage = response.getPropertyAsString(Constants.ERROR);
                    }
                }

                int count = userList.getPropertyCount();
                if (count > 0 && errorMessage.isEmpty()) {

                    HashMap<String,ArrayList<String>> userStores = new HashMap<>();
                    ArrayList userIds = (ArrayList)db.userDao().getAllIds();

                    for (int i = 0;i< count;i++) {

                        SoapObject prop = (SoapObject)userList.getProperty(i);
                        String id = prop.getPropertyAsString(Constants.ID);
                        String storeId = prop.getPropertyAsString(Constants.STORE_ID);

                        if (!userStores.containsKey(id)) {
                            userStores.put(id,new ArrayList<>());
                        }

                        User user = new User();
                        user.id = prop.getPropertyAsString(Constants.ID);
                        user.code = prop.getPropertyAsString(Constants.CODE);
                        user.description = prop.getPropertyAsString(Constants.DESCRIPTION);

                        if (!userIds.isEmpty() && userIds.contains(id)) {
                            db.userDao().update(user);
                        } else {
                            db.userDao().insert(user);
                        }

                        userStores.get(id).add(storeId);
                    }

                    //add default
                    User user = new User();
                    user.id = Constants.EMPTY_ID;
                    user.description = context.getString(R.string.anonymous);
                    if (!userIds.isEmpty() && userIds.contains(Constants.EMPTY_ID)) {
                        db.userDao().update(user);
                    } else {
                        db.userDao().insert(user);
                    }


                    for (String userId:userStores.keySet()) {

                        ArrayList<String> storeIds = userStores.get(userId);

                        for (int i=0;i < storeIds.size();i++) {
                            UserStore userStore = new UserStore();
                            userStore.id = storeIds.get(i);
                            userStore.user_id = userId;
                            db.userStoreDao().insert(userStore);
                        }

                        db.userStoreDao().deleteAllExcept(userId, storeIds);
                    }
                }
                return errorMessage;
            }
        });
    }
}