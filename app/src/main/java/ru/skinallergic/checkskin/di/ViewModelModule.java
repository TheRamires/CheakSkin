package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;

import ru.skinallergic.checkskin.components.healthdiary.components.StateRedactFragment;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StatisticsViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggerRedactViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggersViewModel;
import ru.skinallergic.checkskin.components.tests.viewModels.TestResultViewModel;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.news.NewsViewModel;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;
import ru.skinallergic.checkskin.splash_screen.SplashViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModelImpl.class)
    abstract ViewModel accountViewModel(AccountViewModelImpl accountViewModel);

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(DateViewModel.class)
    abstract ViewModel dateViewModel(DateViewModel dateViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel splashViewModel(SplashViewModel registrationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract ViewModel newsViewModel(NewsViewModel newsViewModel);

    //tests---------------------------------------------------------------------------

    @Binds
    @IntoMap
    @ViewModelKey(TestsViewModel.class)
    abstract ViewModel testsViewModel(TestsViewModel testsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TestResultViewModel.class)
    abstract ViewModel testsResultViewModel(TestResultViewModel testsViewModel);
}
