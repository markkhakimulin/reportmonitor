// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.core.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;

public final class AppModule_ProvideHttpTransportSEFactory
    implements Factory<TimeoutHttpTransport> {
  private final AppModule module;

  public AppModule_ProvideHttpTransportSEFactory(AppModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public TimeoutHttpTransport get() {
    return Preconditions.checkNotNull(
        module.provideHttpTransportSE(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<TimeoutHttpTransport> create(AppModule module) {
    return new AppModule_ProvideHttpTransportSEFactory(module);
  }
}