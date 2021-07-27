package ru.skinallergic.checkskin.components.home.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import ru.skinallergic.checkskin.components.home.data.HomeRepositoriy;
import ru.skinallergic.checkskin.components.home.data.LPU;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.skinallergic.checkskin.components.home.data.ReviewEntity;

public class HomeViewModel extends ViewModel {
    private HomeRepositoriy repo;
    public MutableLiveData<List<LPU>> lpuLive=new MutableLiveData<>();
    public MutableLiveData<List<ReviewEntity>> reviewsLive=new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Inject
    public HomeViewModel (HomeRepositoriy repo){
        this.repo=repo;
    }

    public void getLPUList(){
        compositeDisposable.add(repo.getLPU().subscribe((List<LPU> lpus)-> {
            lpuLive.setValue(lpus);
        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}