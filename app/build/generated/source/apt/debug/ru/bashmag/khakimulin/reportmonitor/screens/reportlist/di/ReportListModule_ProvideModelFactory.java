// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListModel;

public final class ReportListModule_ProvideModelFactory implements Factory<ReportListModel> {
  private final ReportListModule module;

  public ReportListModule_ProvideModelFactory(ReportListModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public ReportListModel get() {
    return Preconditions.checkNotNull(
        module.provideModel(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<ReportListModel> create(ReportListModule module) {
    return new ReportListModule_ProvideModelFactory(module);
  }

  /** Proxies {@link ReportListModule#provideModel()}. */
  public static ReportListModel proxyProvideModel(ReportListModule instance) {
    return instance.provideModel();
  }
}
