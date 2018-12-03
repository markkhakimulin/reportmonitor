package ru.bashmag.khakimulin.reportmonitor.utils;

public class Constants {

    public static final String CHEQUES = "Cheques";
    public static final String CHOSEN_STORES = "CONVERSION_CHOSEN_STORES";
    public static final String CHOSEN_STORES_PREF = "CONVERSION_CHOSEN_STORES_PREF";
    public static final String CODE = "Code";
    public static final String DATE = "Date";
    public static final String DATE_LOAD = "DateLoad";
    public static final String DATE_UNLOAD = "DateUnLoad";
    public static final String DB = "ru.bashmag.khakimulin.reportmonitor.database";
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final String DESCRIPTION = "Description";
    public static final String EMPLOYEE = "Employee";
    public static final String EMPTY_ID = "00000000-0000-0000-0000-000000000000";
    public static final String EMPTY_PASS = "9390";
    public static final String ERROR = "Error";
    public static final String FACT = "Fact";
    public static final String FORMATDATE = "dd.MM.yyyy";
    public static final String FORMATDATE_FROM_1C = "yyyy-MM-dd'T'hh:mm:ss";
    public static final String FORMATDATE_HOUR = "HH:mm";
    public static final String FORMATDATE_TO_1C = "yyyyMMdd";
    public static final String GROUP = "ItemGroup";
    public static final String ID = "ID";
    public static final String ITEMS = "Items";
    public static final String FINISHDATE = "PERIOD_FINISH_DATE";
    public static final String STARTDATE = "PERIOD_START_DATE";
    public static final String PERIOD_FINISHDATE = "PERIOD_FINISHDATE_PREF";
    public static final String PERIOD_STARTDATE = "PERIOD_STARTDATE_PREF";
    public static final String PERIOD_TITLE_PREFERENCES = "PERIOD_TITLE_PREF";
    public static final String PERIOD_TYPE_PREFERENCES = "TYPE_PERIOD_PREF";
    public static final String PLAN = "Plan";
    public static final String POSITION = "Position";
    public static final String PREFERENCES = "ru.bashmag.khakimulin.reportmonitor.preference";
    public static final String SOAP_METHOD_GET_CONVERSION = "getConversion";
    public static final String SOAP_METHOD_GET_CONVERSION_DAILY = "getConversionDaily";
    public static final String SOAP_METHOD_GET_EXCHANGE_STATUSES = "getExchangeStatuses";
    public static final String SOAP_METHOD_GET_PIVOT_TABLE = "getPivotTable";
    public static final String SOAP_METHOD_GET_SALES = "getSales";
    public static final String SOAP_METHOD_GET_STORES = "getStores";
    public static final String SOAP_METHOD_GET_USERS = "getUsers";
    public static final String SOAP_METHOD_LOGIN = "login";
    public static final String SOAP_METHOD_PING_PONG = "pingPong";
    public static final String SOAP_NAMESPACE = "http://www.bashmag.ru/reports";
    public static final String SOAP_URL = "http://192.168.0.210/rozn/ws/reports.1cws";
    public static final String SOAP_URL_DUMMY = "http://192.168.0.98/reports/ws/reports.1cws";
    public static final String START_TYPE = "START_TYPE";
    public static final String STORE = "Store";
    public static final String STORES = "Stores";
    public static final String STORE_ID = "StoreID";
    public static final String TURNOVERLIST = "TurnoverList";
    public static final String USER = "User";
    public static final String USER_ID = "UserID";
    public static final String USER_ID_PREFERENCES = "USER_ID_PREF";
    public static final String USER_TITLE = "UserTitle";
    public static final String USER_TITLE_PREFERENCES = "USER_TITLE_PREF";
    public static final String VISITORS = "Visitors";
    public static final String USERLIST = "UserList";
    public static final String RESULT = "Result";
    public static final String STORELIST = "StoreList";
    public static final String EXCHANGESTATUSLIST = "ExchangeStatusList";

    public enum ReportType {
        conversion,
        fullness,
        turnover,
        sales
    }

}
