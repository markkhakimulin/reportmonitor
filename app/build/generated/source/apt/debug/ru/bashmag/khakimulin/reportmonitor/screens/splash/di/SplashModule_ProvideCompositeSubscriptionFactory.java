// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.splash.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import rx.subscriptions.CompositeSubscription;

public final class SplashModule_ProvideCompositeSubscriptionFactory
    implements Factory<CompositeSubscription> {
  private final SplashModule module;

  public SplashModule_ProvideCompositeSubscriptionFactory(SplashModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public CompositeSubscription get() {
    return Preconditions.checkNotNull(
        module.provideCompositeSubscription(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<CompositeSubscription> create(SplashModule module) {
    return new SplashModule_ProvideCompositeSubscriptionFactory(module);
  }

  /** Proxies {@link SplashModule#provideCompositeSubscription()}. */
  public static CompositeSubscription proxyProvideCompositeSubscription(SplashModule instance) {
    return instance.provideCompositeSubscription();
  }
}