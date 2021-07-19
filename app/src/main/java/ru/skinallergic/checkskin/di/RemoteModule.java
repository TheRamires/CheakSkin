package ru.skinallergic.checkskin.di;

import ru.skinallergic.checkskin.Api.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skinallergic.checkskin.Api.HealthyService;
import ru.skinallergic.checkskin.Api.HomeService;
import ru.skinallergic.checkskin.Api.NotificationService;
import ru.skinallergic.checkskin.Api.ServiceFactory;
import ru.skinallergic.checkskin.Api.TestResultService;
import ru.skinallergic.checkskin.Api.TokenService;

@Module
public class RemoteModule {
    @Inject
    @Singleton
    @Provides
    ApiService provideApi(){
        return ApiService.Companion.create();
    }

    @Inject
    @Singleton
    @Provides
    HomeService provideHomeApi(){
        return ServiceFactory.INSTANCE.makeHomeService(true);
    }

    @Inject
    @Singleton
    @Provides
    TestResultService provideTestApi(){
        return ServiceFactory.INSTANCE.makeTestService(true);
    }

    @Inject
    @Singleton
    @Provides
    HealthyService healthyService (){return ServiceFactory.INSTANCE.makeHealthyService(true);}

    @Inject
    @Singleton
    @Provides
    TokenService tokenService (){return ServiceFactory.INSTANCE.makeTokenService(true);}

    @Inject
    @Singleton
    @Provides
    NotificationService notificationService (){return ServiceFactory.INSTANCE.makeNotificationService(true);}
}
