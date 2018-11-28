// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;

public final class SalesModule_ProvideContextFactory implements Factory<SalesReportActivity> {
  private final SalesModule module;

  public SalesModule_ProvideContextFactory(SalesModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public SalesReportActivity get() {
    return Preconditions.checkNotNull(
        module.provideContext(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<SalesReportActivity> create(SalesModule module) {
    return new SalesModule_ProvideContextFactory(module);
  }

  /** Proxies {@link SalesModule#provideContext()}. */
  public static SalesReportActivity proxyProvideContext(SalesModule instance) {
    return instance.provideContext();
  }
}