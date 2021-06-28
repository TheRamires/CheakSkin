package ru.skinallergic.checkskin.di;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPrefModule {

    @Inject
    @Singleton
    @Provides
    SharedPreferences provideSharedPref(Context context){
        return context.getSharedPreferences("account", Context.MODE_PRIVATE);
    }
}
