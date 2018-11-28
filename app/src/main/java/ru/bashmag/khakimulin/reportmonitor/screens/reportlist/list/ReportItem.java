package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list;

import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ReportItem {
    String name;
    String desc;
    boolean isNew = false;
    Class classReport;
    Constants.ReportType type;

    public ReportItem(String n, String d, Class cr, Constants.ReportType t) {
        name = n;
        desc = d;
        classReport = cr;
        type = t;
    }

    public Class getReportClass() {
        return classReport;
    }
    public Constants.ReportType getReportType() {
        return type;
    }
}
