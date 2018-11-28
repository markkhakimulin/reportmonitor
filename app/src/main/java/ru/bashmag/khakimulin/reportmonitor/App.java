package ru.bashmag.khakimulin.reportmonitor;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppModule;
import ru.bashmag.khakimulin.reportmonitor.core.di.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initialiseLogger();
        initAppComponent();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }
    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    private void initialiseLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    //TODO  decide what to log in release version
                }
            });
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
