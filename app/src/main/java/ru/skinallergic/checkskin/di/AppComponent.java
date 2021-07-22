package ru.skinallergic.checkskin.di;

import android.content.Context;

import ru.skinallergic.checkskin.NotificationService;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;
import ru.skinallergic.checkskin.handlers.ToastyManager;

@Component(modules = {
        RemoteModule.class,
        SharedPrefModule.class,
        ViewModelModule.class,
        HomeViewModuleModules.class,
        HealthyViewModelModules.class,
        FoodViewModelModules.class
})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder withContext(Context context);

        AppComponent build();
    }
    /*
        void inject(MainActivity activity);
        void inject(SecondActivity activity);
        */
    MyViewModelFactory getViewModelFactory();
    ValidatorField getValidatorField();

    public ToastyManager getToastyManager();
    //public NotificationService notificationService();
}