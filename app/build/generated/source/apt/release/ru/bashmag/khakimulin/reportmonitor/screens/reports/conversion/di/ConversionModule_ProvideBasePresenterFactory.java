// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionModel;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

public final class ConversionModule_ProvideBasePresenterFactory implements Factory<BasePresenter> {
  private final ConversionModule module;

  private final Provider<DB> dbProvider;

  private final Provider<RxSchedulers> schedulersProvider;

  private final Provider<ConversionModel> modelProvider;

  private final Provider<CompositeSubscription> subscriptionProvider;

  public ConversionModule_ProvideBasePresenterFactory(
      ConversionModule module,
      Provider<DB> dbProvider,
      Provider<RxSchedulers> schedulersProvider,
      Provider<ConversionModel> modelProvider,
      Provider<CompositeSubscription> subscriptionProvider) {
    assert module != null;
    this.module = module;
    assert dbProvider != null;
    this.dbProvider = dbProvider;
    assert schedulersProvider != null;
    this.schedulersProvider = schedulersProvider;
    assert modelProvider != null;
    this.modelProvider = modelProvider;
    assert subscriptionProvider != null;
    this.subscriptionProvider = subscriptionProvider;
  }

  @Override
  public BasePresenter get() {
    return Preconditions.checkNotNull(
        module.provideBasePresenter(
            dbProvider.get(),
            schedulersProvider.get(),
            modelProvider.get(),
            subscriptionProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<BasePresenter> create(
      ConversionModule module,
      Provider<DB> dbProvider,
      Provider<RxSchedulers> schedulersProvider,
      Provider<ConversionModel> modelProvider,
      Provider<CompositeSubscription> subscriptionProvider) {
    return new ConversionModule_ProvideBasePresenterFactory(
        module, dbProvider, schedulersProvider, modelProvider, subscriptionProvider);
  }

  /**
   * Proxies {@link ConversionModule#provideBasePresenter(DB, RxSchedulers, ConversionModel,
   * CompositeSubscription)}.
   */
  public static BasePresenter proxyProvideBasePresenter(
      ConversionModule instance,
      DB db,
      RxSchedulers schedulers,
      ConversionModel model,
      CompositeSubscription subscription) {
    return instance.provideBasePresenter(db, schedulers, model, subscription);
  }
}
