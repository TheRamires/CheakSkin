package ru.skinallergic.checkskin.di;

import ru.skinallergic.checkskin.handlers.ToastyManager;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;

import dagger.Module;
import dagger.Provides;

@Module
public class ValidatorFieldModule {

    @Provides
    ValidatorField provideValidatorField(ToastyManager toastyManager){
        return new ValidatorField(toastyManager);
    }
}
