package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.ReportListActivity;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
@ReportListScope
@Component(modules = {ReportListModule.class},dependencies = {AppComponent.class} )
public interface ReportListComponent {
    void inject (ReportListActivity reportListActivity);
}
