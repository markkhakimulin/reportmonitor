package ru.bashmag.khakimulin.reportmonitor.screens.login.mvp;

import android.content.Context;
import android.text.TextUtils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class LoginModel {


    private DB db;
    private SoapSerializationEnvelope envelope;
    private TimeoutHttpTransport httpTransportSE;
    private Context context;
    private final String namespace = Constants.SOAP_NAMESPACE;
    private final String soapActionLogin = String.format("%s#Reports:%s",namespace,Constants.SOAP_METHOD_LOGIN);
    private final String soapActionPingPong = String.format("%s#Reports:%s",namespace,Constants.SOAP_METHOD_PING_PONG);



    public LoginModel(DB db,
                      TimeoutHttpTransport httpTransportSE,
                      SoapSerializationEnvelope envelope,
                      Context appContext) {
        this.db = db;
        this.envelope = envelope;
        this.httpTransportSE = httpTransportSE;
        this.context = appContext;

    }

    Observable<User> login(String hash) {

        return Observable.fromCallable(new Callable<User>() {
            @Override
            public User call() throws Exception {
                String errorMessage = "";

                SoapObject so = new SoapObject(Constants.SOAP_NAMESPACE, Constants.SOAP_METHOD_LOGIN);
                so.addProperty("Hash",hash);

                envelope.setOutputSoapObject(so);
                httpTransportSE.call(soapActionLogin, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();//UserResponse
                String result = response.getPropertyAsString("Result");
                String userID = response.getPropertyAsString("ID");
                if (response.hasProperty("Error")) {
                    try {
                        errorMessage = ((SoapPrimitive) response.getProperty("Error")).getValue().toString();
                    } catch (Exception e)
                    {//нет ошибки
                    }
                }
                if (result.equalsIgnoreCase("OK") && TextUtils.isEmpty(errorMessage)) {
                    //clear whole table
                    User user =  db.userDao().getById(userID);
                    if (user != null) {
                        return user;
                    }
                }

                return null;
            }
        });
    }
    Observable<List<User>> getUserList() {

        return Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return db.userDao().getAll();
            }
        });
    }

    Observable<User> getUserById(String userId) {

        return Observable.fromCallable(new Callable<User>() {
            @Override
            public User call() throws Exception {
                User user = db.userDao().getById(userId);
                if (user != null) {
                    return user;
                }

                return null;
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