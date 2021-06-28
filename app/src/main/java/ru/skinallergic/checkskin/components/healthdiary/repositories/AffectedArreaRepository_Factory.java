package ru.skinallergic.checkskin.components.healthdiary.repositories;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;
import ru.skinallergic.checkskin.Api.HealthyService;
import ru.skinallergic.checkskin.Api.TokenService;
import ru.skinallergic.checkskin.handlers.NetworkHandler;
import ru.skinallergic.checkskin.handlers.ToastyManager;
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AffectedArreaRepository_Factory implements Factory<AffectedArreaRepository> {
  private final Provider<HealthyService> service_Provider;

  private final Provider<TokenService> tokenServiceProvider;

  private final Provider<TokenModelImpls> tokenModel_Provider;

  private final Provider<NetworkHandler> networkHandlerProvider;

  private final Provider<ToastyManager> toastyManagerProvider;

  public AffectedArreaRepository_Factory(
      Provider<HealthyService> service_Provider,
      Provider<TokenService> tokenServiceProvider,
      Provider<TokenModelImpls> tokenModel_Provider,
      Provider<NetworkHandler> networkHandlerProvider,
      Provider<ToastyManager> toastyManagerProvider) {
    this.service_Provider = service_Provider;
    this.tokenServiceProvider = tokenServiceProvider;
    this.tokenModel_Provider = tokenModel_Provider;
    this.networkHandlerProvider = networkHandlerProvider;
    this.toastyManagerProvider = toastyManagerProvider;
  }

  @Override
  public AffectedArreaRepository get() {
    return new AffectedArreaRepository(
        service_Provider.get(),
        tokenServiceProvider.get(),
        tokenModel_Provider.get(),
        networkHandlerProvider.get(),
        toastyManagerProvider.get());
  }

  public static AffectedArreaRepository_Factory create(
      Provider<HealthyService> service_Provider,
      Provider<TokenService> tokenServiceProvider,
      Provider<TokenModelImpls> tokenModel_Provider,
      Provider<NetworkHandler> networkHandlerProvider,
      Provider<ToastyManager> toastyManagerProvider) {
    return new AffectedArreaRepository_Factory(
        service_Provider,
        tokenServiceProvider,
        tokenModel_Provider,
        networkHandlerProvider,
        toastyManagerProvider);
  }
}
