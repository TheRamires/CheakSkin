package ru.skinallergic.checkskin.di;

import android.content.Context;

import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;

@Component(modules = {
        RemoteModule.class,
        SharedPrefModule.class,
        ViewModelModule.class,
        HomeViewModuleModules.class,
        HealthyViewModelModules.class
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
}