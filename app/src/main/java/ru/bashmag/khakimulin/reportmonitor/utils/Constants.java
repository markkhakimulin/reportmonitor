package ru.bashmag.khakimulin.reportmonitor.utils;

import java.util.HashMap;
import java.util.Set;

public class Constants {

    public final static String SOAP_URL = "http://192.168.0.98/reports/ws/reports.1cws";

    public final static String SOAP_NAMESPACE = "http://www.bashmag.ru/reports";
    public final static String SOAP_METHOD_GET_STORES = "getStores";
    public final static String SOAP_METHOD_GET_CONVERSION = "getConversion";
    public final static String SOAP_METHOD_GET_CONVERSION_DAILY = "getConversionDaily";

    public final static String FORMATDATE_TO_1C = "yyyyMMdd";
    public final static String FORMATDATE_FROM_1C = "yyyy-MM-dd'T'hh:mm:ss";
    public final static String FORMATDATE_HOUR = "hh:'00' a";
    public final static String FORMATDATE = "dd.MM.yyyy";


    public final static String  PREFERENCES = "ru.bashmag.khakimulin.reportmonitor.preference";

    public final static String  PERIOD_TYPE_PREFERENCES = "CONVERSION_PERIOD_PREF";
    public final static String  PERIOD_TITLE_PREFERENCES = "CONVERSION_PERIOD_TITLE_PREF";
    public final static String  PERIOD_STARTDATE = "CONVERSION_PERIOD_STARTDATE_PREF";
    public final static String  PERIOD_FINISHDATE = "CONVERSION_PERIOD_FINISHDATE_PREF";
    public final static String  CHOSEN_STORES = "CONVERSION_CHOSEN_STORES_PREF";

    public final static String  DB = "ru.bashmag.khakimulin.reportmonitor.database";

    //table fields
    public final static String  ID = "ID";
    //stores
    public final static String  CODE = "Code";
    public final static String  DESCRIPTION = "Description";
    //conversion
    public final static String  DATE = "Date";
    public final static String  VISITORS = "Visitors";
    public final static String  CHEQUES = "Cheques";
    public final static String  ITEMS = "Items";
    public final static String  STORE = "Store";

    public enum ReportType {
        conversion,
        fullness,
        turnover
    }

}
