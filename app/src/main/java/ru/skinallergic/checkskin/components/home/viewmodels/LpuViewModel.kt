package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import ru.skinallergic.checkskin.components.home.data.LpuEntity
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity
import ru.skinallergic.checkskin.components.home.repositories.LpuRepository
import ru.skinallergic.checkskin.handlers.ToastyManager
import javax.inject.Inject

class LpuViewModel @Inject constructor(val repo: LpuRepository, val toastyManager: ToastyManager): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repo

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }
    val lpuData=MutableLiveData<List<LpuEntity>>()
    val favorites=MutableLiveData<List<LpuEntity>>()
    val oneLpu=MutableLiveData<LpuOneEntity>()

    fun getLpuList():MutableLiveData<List<LpuEntity>> {
        compositeDisposable.add(
                repo.getLpuList()
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe ({
                            Loger.log("lpuList "+it)
                            lpuData.value=it
                        },{})
        )
        return lpuData
    }
    fun getLpuFavorites() : MutableLiveData<List<LpuEntity>> {
        compositeDisposable.add(
                repo.getLpuFavorites()
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe ({
                            Loger.log("-------------------favorites Lpu ${it}")
                            favorites.value=it
                        },{})
        )
        return favorites
    }
    fun getOneLpu(id: Int): MutableLiveData<LpuOneEntity>{
        compositeDisposable.add(
                repo.getOneLpu(id)
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe ({
                            Loger.log("-------------------getOneLpu Lpu ${it}")
                            oneLpu.value=it
                        },{})


        )
        return oneLpu
    }
    fun addFavorite(id: Int, favorite: Boolean){
        compositeDisposable.add(
                repo.addFavorite(id,favorite)
                        ?.subscribe ({
                            if (it=="Ok"){
                                when(favorite){
                                    true->toastyManager.toastyyyy("Добавлено в избранные")
                                }
                            }
                        },{})
        )
    }
}