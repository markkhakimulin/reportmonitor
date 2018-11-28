// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.login.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;

public final class LoginModule_ProvideSplashContextFactory implements Factory<LoginActivity> {
  private final LoginModule module;

  public LoginModule_ProvideSplashContextFactory(LoginModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public LoginActivity get() {
    return Preconditions.checkNotNull(
        module.provideSplashContext(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<LoginActivity> create(LoginModule module) {
    return new LoginModule_ProvideSplashContextFactory(module);
  }

  /** Proxies {@link LoginModule#provideSplashContext()}. */
  public static LoginActivity proxyProvideSplashContext(LoginModule instance) {
    return instance.provideSplashContext();
  }
}