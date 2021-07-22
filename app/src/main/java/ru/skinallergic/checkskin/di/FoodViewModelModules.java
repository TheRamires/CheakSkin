package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AddingFoodViewModel;
@Module
abstract public class FoodViewModelModules {
    @Binds
    @IntoMap
    @ViewModelKey(AddingFoodViewModel.class)
    abstract ViewModel addingFoodViewModel(AddingFoodViewModel addingFoodViewModel);
}
