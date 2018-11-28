package ru.bashmag.khakimulin.reportmonitor.screens.login.di;

import dagger.Component;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@LoginScope
@Component(modules = {LoginModule.class}, dependencies = {AppComponent.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}