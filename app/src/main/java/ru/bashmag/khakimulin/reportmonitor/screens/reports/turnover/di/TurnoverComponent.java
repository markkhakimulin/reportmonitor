package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.ReportListScope;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@TurnoverScope
@Component(modules = {TurnoverModule.class},dependencies = {AppComponent.class} )
public interface TurnoverComponent {
    void inject(TurnoverReportActivity turnoverReportActivity);
}
