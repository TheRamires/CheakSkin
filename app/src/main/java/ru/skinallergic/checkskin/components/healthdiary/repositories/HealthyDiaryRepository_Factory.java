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
public final class HealthyDiaryRepository_Factory implements Factory<HealthyDiaryRepository> {
  private final Provider<HealthyService> serviceProvider;

  private final Provider<TokenService> tokenServiceProvider;

  private final Provider<TokenModelImpls> tokenModelProvider;

  private final Provider<NetworkHandler> networkHandlerProvider;

  private final Provider<ToastyManager> toastyManagerProvider;

  public HealthyDiaryRepository_Factory(
      Provider<HealthyService> serviceProvider,
      Provider<TokenService> tokenServiceProvider,
      Provider<TokenModelImpls> tokenModelProvider,
      Provider<NetworkHandler> networkHandlerProvider,
      Provider<ToastyManager> toastyManagerProvider) {
    this.serviceProvider = serviceProvider;
    this.tokenServiceProvider = tokenServiceProvider;
    this.tokenModelProvider = tokenModelProvider;
    this.networkHandlerProvider = networkHandlerProvider;
    this.toastyManagerProvider = toastyManagerProvider;
  }

  @Override
  public HealthyDiaryRepository get() {
    return new HealthyDiaryRepository(
        serviceProvider.get(),
        tokenServiceProvider.get(),
        tokenModelProvider.get(),
        networkHandlerProvider.get(),
        toastyManagerProvider.get());
  }

  public static HealthyDiaryRepository_Factory create(
      Provider<HealthyService> serviceProvider,
      Provider<TokenService> tokenServiceProvider,
      Provider<TokenModelImpls> tokenModelProvider,
      Provider<NetworkHandler> networkHandlerProvider,
      Provider<ToastyManager> toastyManagerProvider) {
    return new HealthyDiaryRepository_Factory(
        serviceProvider,
        tokenServiceProvider,
        tokenModelProvider,
        networkHandlerProvider,
        toastyManagerProvider);
  }
}
