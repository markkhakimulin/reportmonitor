// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di;

import android.content.SharedPreferences;
import android.content.res.Resources;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import ru.bashmag.khakimulin.reportmonitor.core.BasePresenter;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.core.di.AppComponent;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity_MembersInjector;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverModel;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;
import rx.subscriptions.CompositeSubscription;

public final class DaggerTurnoverComponent implements TurnoverComponent {
  private Provider<DB> provideDBProvider;

  private Provider<SharedPreferences> provideSharedPreferenceProvider;

  private Provider<RxSchedulers> provideRxSchedulersProvider;

  private Provider<TimeoutHttpTransport> provideHttpTransportProvider;

  private Provider<SoapSerializationEnvelope> provideSoapSerializationEnvelopeProvider;

  private Provider<SoapObject> provideSoapObjectProvider;

  private Provider<TurnoverModel> provideModelProvider;

  private Provider<CompositeSubscription> provideCompositeSubscriptionProvider;

  private Provider<BasePresenter> provideBasePresenterProvider;

  private Provider<TurnoverPresenter> providePresenterProvider;

  private Provider<Resources> provideResourcesProvider;

  private MembersInjector<TurnoverReportActivity> turnoverReportActivityMembersInjector;

  private DaggerTurnoverComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.provideDBProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideDB(
            builder.appComponent);

    this.provideSharedPreferenceProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSharedPreference(
            builder.appComponent);

    this.provideRxSchedulersProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideRxSchedulers(
            builder.appComponent);

    this.provideHttpTransportProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideHttpTransport(
            builder.appComponent);

    this.provideSoapSerializationEnvelopeProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSoapSerializationEnvelope(
            builder.appComponent);

    this.provideSoapObjectProvider =
        DoubleCheck.provider(
            TurnoverModule_ProvideSoapObjectFactory.create(builder.turnoverModule));

    this.provideModelProvider =
        DoubleCheck.provider(
            TurnoverModule_ProvideModelFactory.create(
                builder.turnoverModule,
                provideDBProvider,
                provideHttpTransportProvider,
                provideSoapSerializationEnvelopeProvider,
                provideSoapObjectProvider));

    this.provideCompositeSubscriptionProvider =
        DoubleCheck.provider(
            TurnoverModule_ProvideCompositeSubscriptionFactory.create(builder.turnoverModule));

    this.provideBasePresenterProvider =
        DoubleCheck.provider(
            TurnoverModule_ProvideBasePresenterFactory.create(
                builder.turnoverModule,
                provideDBProvider,
                provideSharedPreferenceProvider,
                provideRxSchedulersProvider,
                provideModelProvider,
                provideCompositeSubscriptionProvider));

    this.providePresenterProvider =
        DoubleCheck.provider(TurnoverModule_ProvidePresenterFactory.create(builder.turnoverModule));

    this.provideResourcesProvider =
        new ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideResources(
            builder.appComponent);

    this.turnoverReportActivityMembersInjector =
        TurnoverReportActivity_MembersInjector.create(
            provideBasePresenterProvider,
            provideCompositeSubscriptionProvider,
            provideSharedPreferenceProvider,
            providePresenterProvider,
            provideResourcesProvider);
  }

  @Override
  public void inject(TurnoverReportActivity turnoverReportActivity) {
    turnoverReportActivityMembersInjector.injectMembers(turnoverReportActivity);
  }

  public static final class Builder {
    private TurnoverModule turnoverModule;

    private AppComponent appComponent;

    private Builder() {}

    public TurnoverComponent build() {
      if (turnoverModule == null) {
        throw new IllegalStateException(TurnoverModule.class.getCanonicalName() + " must be set");
      }
      if (appComponent == null) {
        throw new IllegalStateException(AppComponent.class.getCanonicalName() + " must be set");
      }
      return new DaggerTurnoverComponent(this);
    }

    public Builder turnoverModule(TurnoverModule turnoverModule) {
      this.turnoverModule = Preconditions.checkNotNull(turnoverModule);
      return this;
    }

    public Builder appComponent(AppComponent appComponent) {
      this.appComponent = Preconditions.checkNotNull(appComponent);
      return this;
    }
  }

  private static class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideDB
      implements Provider<DB> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideDB(AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public DB get() {
      return Preconditions.checkNotNull(
          appComponent.provideDB(), "Cannot return null from a non-@Nullable component method");
    }
  }

  private static
  class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSharedPreference
      implements Provider<SharedPreferences> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSharedPreference(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public SharedPreferences get() {
      return Preconditions.checkNotNull(
          appComponent.provideSharedPreference(),
          "Cannot return null from a non-@Nullable component method");
    }
  }

  private static class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideRxSchedulers
      implements Provider<RxSchedulers> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideRxSchedulers(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public RxSchedulers get() {
      return Preconditions.checkNotNull(
          appComponent.provideRxSchedulers(),
          "Cannot return null from a non-@Nullable component method");
    }
  }

  private static class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideHttpTransport
      implements Provider<TimeoutHttpTransport> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideHttpTransport(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public TimeoutHttpTransport get() {
      return Preconditions.checkNotNull(
          appComponent.provideHttpTransport(),
          "Cannot return null from a non-@Nullable component method");
    }
  }

  private static
  class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSoapSerializationEnvelope
      implements Provider<SoapSerializationEnvelope> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideSoapSerializationEnvelope(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public SoapSerializationEnvelope get() {
      return Preconditions.checkNotNull(
          appComponent.provideSoapSerializationEnvelope(),
          "Cannot return null from a non-@Nullable component method");
    }
  }

  private static class ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideResources
      implements Provider<Resources> {
    private final AppComponent appComponent;

    ru_bashmag_khakimulin_reportmonitor_core_di_AppComponent_provideResources(
        AppComponent appComponent) {
      this.appComponent = appComponent;
    }

    @Override
    public Resources get() {
      return Preconditions.checkNotNull(
          appComponent.provideResources(),
          "Cannot return null from a non-@Nullable component method");
    }
  }
}
