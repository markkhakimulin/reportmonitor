package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@SalesScope
@Component(modules = {SalesModule.class},dependencies = {AppComponent.class} )
public interface SalesComponent {
    void inject(SalesReportActivity reportActivity);
}
