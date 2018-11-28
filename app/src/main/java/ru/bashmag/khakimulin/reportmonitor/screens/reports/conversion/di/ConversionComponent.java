package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionDailyFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@ConversionScope
@Component(modules = {ConversionModule.class}, dependencies = {AppComponent.class})
public interface ConversionComponent {
    void inject(ConversionReportActivity activity);
    void inject(ConversionListFragment fragment);
    void inject(ConversionDailyFragment fragment);
}
