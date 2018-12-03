package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import java.util.ArrayList;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;

/**
 * Created by Mark Khakimulin on 18.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public interface ConversionView {

    void onDestroy();

    void onShowReport(ArrayList<ConversionData> arrayList);

    void setRefreshing(Boolean bool);

    String tag();
}
