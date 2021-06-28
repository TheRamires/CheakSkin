package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.home.data.LPU
import ru.skinallergic.checkskin.components.home.interactors.LpuListCase
import ru.skinallergic.checkskin.components.home.repositories.LpuRepository
import ru.skinallergic.checkskin.type.Failure
import javax.inject.Inject

class LpuViewModel @Inject constructor(val lpuListCase: LpuListCase): BaseHomeViewModel() {
    val lpuData=MutableLiveData<List<LPU>>()

    fun getLpuList(){
        lpuListCase(0){
            it.subscribe ({
                it.either({
                    when(it){
                        is Failure.NetworkConnectionError->handleFailure(it)
                        is Failure.ServerError->handleFailure(it)
                        is Failure.AccessTokenExpired->handleFailure(it)
                    }
                    handleFailure(it)
                },{
                    //lpuData.value=it
                })
            },{Loger.log("unexpected exception, getLpuList =  "+it.message)
            })
        }
    }
}