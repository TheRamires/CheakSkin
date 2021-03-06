package ru.skinallergic.checkskin.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.HealthyDiaryViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ImageViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RatingViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderDetailViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StatisticsViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggerRedactViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggersViewModel;

@Module
abstract public class HealthyViewModelModules {

    //healthy diary-------------------------------------------------------------------
   /* @Binds
    @IntoMap
    @ViewModelKey(BaseHealthyViewModel.class)
    abstract ViewModel baseHealthyViewModel(BaseHealthyViewModel affectedAreaViewModel);*/

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel.class)
    abstract ViewModel statisticsViewModel(StatisticsViewModel statisticsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TriggersViewModel.class)
    abstract ViewModel triggersViewModel(TriggersViewModel triggersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TriggerRedactViewModel.class)
    abstract ViewModel triggerRedactViewModel(TriggerRedactViewModel triggerRedactViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AffectedAreaViewModel.class)
    abstract ViewModel affectedAreaViewModel(AffectedAreaViewModel affectedAreaViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AffectedAreaCommonViewModel.class)
    abstract ViewModel affectedAreaRedactViewModel(AffectedAreaCommonViewModel affectedAreaRedactViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StateViewModel.class)
    abstract ViewModel stateViewModel(StateViewModel stateViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TreatmentViewModel.class)
    abstract ViewModel treatmentViewModel(TreatmentViewModel treatmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel.class)
    abstract ViewModel ratingViewModel(RatingViewModel ratingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HealthyDiaryViewModel.class)
    abstract ViewModel healthyDiaryViewModel(HealthyDiaryViewModel healthyDiaryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel.class)
    abstract ViewModel imageViewModel(ImageViewModel imageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RemindersViewModel.class)
    abstract ViewModel remindersViewModel(RemindersViewModel remindersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RemindCommonViewModel.class)
    abstract ViewModel remindCommonViewModel(RemindCommonViewModel remindCommonViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReminderWriterViewModel.class)
    abstract ViewModel reminderWriterViewModel(ReminderWriterViewModel reminderWriterViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReminderDetailViewModel.class)
    abstract ViewModel reminderDetailViewModel(ReminderDetailViewModel reminderDetailViewModel);

}
