// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp.SalesModel;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

public final class SalesModule_ProvideBasePresenterFactory implements Factory<BasePresenter> {
  private final SalesModule module;

  private final Provider<DB> dbProvider;

  private final Provider<RxSchedulers> schedulersProvider;

  private final Provider<SalesModel> modelProvider;

  private final Provider<CompositeSubscription> subscriptionProvider;

  public SalesModule_ProvideBasePresenterFactory(
      SalesModule module,
      Provider<DB> dbProvider,
      Provider<RxSchedulers> schedulersProvider,
      Provider<SalesModel> modelProvider,
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
      SalesModule module,
      Provider<DB> dbProvider,
      Provider<RxSchedulers> schedulersProvider,
      Provider<SalesModel> modelProvider,
      Provider<CompositeSubscription> subscriptionProvider) {
    return new SalesModule_ProvideBasePresenterFactory(
        module, dbProvider, schedulersProvider, modelProvider, subscriptionProvider);
  }

  /**
   * Proxies {@link SalesModule#provideBasePresenter(DB, RxSchedulers, SalesModel,
   * CompositeSubscription)}.
   */
  public static BasePresenter proxyProvideBasePresenter(
      SalesModule instance,
      DB db,
      RxSchedulers schedulers,
      SalesModel model,
      CompositeSubscription subscription) {
    return instance.provideBasePresenter(db, schedulers, model, subscription);
  }
}