package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AddingFoodViewModel;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AllergenesViewModel;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.MealViewModel;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.RedactFoodViewModel;

@Module
abstract public class FoodViewModelModules {
    @Binds
    @IntoMap
    @ViewModelKey(AddingFoodViewModel.class)
    abstract ViewModel addingFoodViewModel(AddingFoodViewModel addingFoodViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MealViewModel.class)
    abstract ViewModel mealViewModel(MealViewModel mealViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FoodDiaryViewModel.class)
    abstract ViewModel foodDiaryViewModel(FoodDiaryViewModel foodDiaryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AllergenesViewModel.class)
    abstract ViewModel allergenesViewModel(AllergenesViewModel allergenesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RedactFoodViewModel.class)
    abstract ViewModel redactFoodViewModel(RedactFoodViewModel redactFoodViewModel);

}
