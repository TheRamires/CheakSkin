package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>,
            Provider<ViewModel>> viewModels;

    @Inject
    public MyViewModelFactory(Map<Class<? extends ViewModel>,
            Provider<ViewModel>> viewModels) {
        this.viewModels = viewModels;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        Provider<ViewModel> viewModelProvider = viewModels.get(modelClass);

        if (viewModelProvider == null) {
            throw new IllegalArgumentException("model class "
                    + modelClass
                    + " not found");
        }

        return (T) viewModelProvider.get();
    }
}