package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp;

import java.util.ArrayList;

import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ReportListModel {

    ReportListActivity context;

    public ReportListModel(ReportListActivity context) {
        this.context = context;
    }


    Observable<ArrayList<ReportItem>> provideReportList() {

        ArrayList<ReportItem> reports = new ArrayList<>();
        reports.add(new ReportItem("Конверсия","Конверсия посетителей",ConversionReportActivity.class,Constants.ReportType.conversion));
        reports.add(new ReportItem("Наполняемость","Наполняемость чеков",ConversionReportActivity.class,Constants.ReportType.fullness));
        reports.add(new ReportItem("Показатели продаж","Плановые и фактические показатели",TurnoverReportActivity.class,Constants.ReportType.turnover));
        return Observable.just(reports);
    }


    public void goToReportActivity(ReportItem reportItem) {
        context.goToReportActivity(reportItem.getReportClass(),reportItem.getReportType());
    }
}
