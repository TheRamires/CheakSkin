package ru.skinallergic.checkskin.components.fooddiary.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;
import ru.skinallergic.checkskin.components.fooddiary.data.FoodRepositoriy;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class FoodDiaryViewModel extends ViewModel {
    private FoodRepositoriy repo=new FoodRepositoriy();
    public MutableLiveData<List<EntityFoodPosition>> foodLive=new MutableLiveData<>();

    public void refreshFoodList(String date, String timeOfMeal){
        repo.getFoodPOsitions(date, timeOfMeal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@io.reactivex.annotations.NonNull List<EntityFoodPosition> list)-> {
                        foodLive.setValue(list);

                        for (EntityFoodPosition entity: list) {
                            Loger.log(entity.getName()+" "+entity.getWeight());
                        }
                });
    }
}