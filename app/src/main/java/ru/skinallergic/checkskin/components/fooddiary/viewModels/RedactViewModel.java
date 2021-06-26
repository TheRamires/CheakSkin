package ru.skinallergic.checkskin.components.fooddiary.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;
import ru.skinallergic.checkskin.components.fooddiary.data.FoodRepositoriy;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;

public class RedactViewModel extends ViewModel {
    public String date="";
    public String timeOfMeal="";
    private FoodRepositoriy repo=new FoodRepositoriy();
    public MutableLiveData<List<EntityFoodPosition>> entityList=new MutableLiveData<>();

    public void getEntityList(){
        Loger.log("date "+date+" timeOfMeal "+timeOfMeal);
        repo.getFoodPOsitions(date,timeOfMeal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull List<EntityFoodPosition> list)-> {
                        entityList.setValue(list);

                });
    }
}
