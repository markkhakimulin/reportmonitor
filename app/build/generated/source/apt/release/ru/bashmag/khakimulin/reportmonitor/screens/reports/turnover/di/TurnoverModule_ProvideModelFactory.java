// Generated by dagger.internal.codegen.ComponentProcessor (https://google.github.io/dagger).
package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import ru.bashmag.khakimulin.reportmonitor.core.TimeoutHttpTransport;
import ru.bashmag.khakimulin.reportmonitor.db.DB;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverModel;

public final class TurnoverModule_ProvideModelFactory implements Factory<TurnoverModel> {
  private final TurnoverModule module;

  private final Provider<DB> dbProvider;

  private final Provider<TimeoutHttpTransport> httpTransportSEProvider;

  private final Provider<SoapSerializationEnvelope> envelopeProvider;

  private final Provider<SoapObject> soapObjectProvider;

  public TurnoverModule_ProvideModelFactory(
      TurnoverModule module,
      Provider<DB> dbProvider,
      Provider<TimeoutHttpTransport> httpTransportSEProvider,
      Provider<SoapSerializationEnvelope> envelopeProvider,
      Provider<SoapObject> soapObjectProvider) {
    assert module != null;
    this.module = module;
    assert dbProvider != null;
    this.dbProvider = dbProvider;
    assert httpTransportSEProvider != null;
    this.httpTransportSEProvider = httpTransportSEProvider;
    assert envelopeProvider != null;
    this.envelopeProvider = envelopeProvider;
    assert soapObjectProvider != null;
    this.soapObjectProvider = soapObjectProvider;
  }

  @Override
  public TurnoverModel get() {
    return Preconditions.checkNotNull(
        module.provideModel(
            dbProvider.get(),
            httpTransportSEProvider.get(),
            envelopeProvider.get(),
            soapObjectProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<TurnoverModel> create(
      TurnoverModule module,
      Provider<DB> dbProvider,
      Provider<TimeoutHttpTransport> httpTransportSEProvider,
      Provider<SoapSerializationEnvelope> envelopeProvider,
      Provider<SoapObject> soapObjectProvider) {
    return new TurnoverModule_ProvideModelFactory(
        module, dbProvider, httpTransportSEProvider, envelopeProvider, soapObjectProvider);
  }

  /**
   * Proxies {@link TurnoverModule#provideModel(DB, TimeoutHttpTransport, SoapSerializationEnvelope,
   * SoapObject)}.
   */
  public static TurnoverModel proxyProvideModel(
      TurnoverModule instance,
      DB db,
      TimeoutHttpTransport httpTransportSE,
      SoapSerializationEnvelope envelope,
      SoapObject soapObject) {
    return instance.provideModel(db, httpTransportSE, envelope, soapObject);
  }
}
