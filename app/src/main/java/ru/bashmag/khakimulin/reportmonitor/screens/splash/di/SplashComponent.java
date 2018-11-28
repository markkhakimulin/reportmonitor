package ru.bashmag.khakimulin.reportmonitor.screens.splash.di;

import android.graphics.Typeface;

import javax.inject.Named;

import dagger.Component;
import dagger.Provides;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppScope;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.SplashActivity;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@SplashScope
@Component(modules = {SplashModule.class}, dependencies = {AppComponent.class})
public interface SplashComponent {
    void inject(SplashActivity activity);
}
