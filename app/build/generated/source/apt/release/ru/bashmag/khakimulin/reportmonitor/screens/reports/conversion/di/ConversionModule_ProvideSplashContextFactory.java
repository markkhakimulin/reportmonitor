// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;

public final class ConversionModule_ProvideSplashContextFactory
    implements Factory<ConversionReportActivity> {
  private final ConversionModule module;

  public ConversionModule_ProvideSplashContextFactory(ConversionModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public ConversionReportActivity get() {
    return Preconditions.checkNotNull(
        module.provideSplashContext(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<ConversionReportActivity> create(ConversionModule module) {
    return new ConversionModule_ProvideSplashContextFactory(module);
  }

  /** Proxies {@link ConversionModule#provideSplashContext()}. */
  public static ConversionReportActivity proxyProvideSplashContext(ConversionModule instance) {
    return instance.provideSplashContext();
  }
}
