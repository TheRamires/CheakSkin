package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.skinallergic.checkskin.components.home.viewmodels.DocumentViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.LpuViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.MapViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.ReviesViewModel;

@Module
abstract public class HomeViewModuleModules {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel homeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DocumentViewModel.class)
    abstract ViewModel documentViewModel(DocumentViewModel documentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LpuViewModel.class)
    abstract ViewModel lpuViewModel(LpuViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    abstract ViewModel mapViewModel(MapViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReviesViewModel.class)
    abstract ViewModel reviewViewModel(ReviesViewModel homeViewModel);


}
