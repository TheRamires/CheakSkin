package ru.skinallergic.checkskin.components.healthdiary.repositories

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import ru.skinallergic.checkskin.Api.HealthyService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.data.EntityAffected
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import java.io.File
import java.util.*
import javax.inject.Inject

class AffectedArreaRepository  @Inject constructor(
        val service_: HealthyService,
        val tokenService: TokenService,
        tokenModel_: TokenModelImpls,
        networkHandler: NetworkHandler,
        toastyManager: ToastyManager
): HealthyDiaryRepository(service_,tokenService,tokenModel_,networkHandler,toastyManager ) {

    fun loadAffectedsLists(affectedLive1: MutableLiveData<List<EntityAffected>?>,
                           affectedLive2: MutableLiveData<List<EntityAffected>?>) {
        //forExample
        val enity11 = EntityAffected(1, "торс", "body_torso")
        val enity12 = EntityAffected(2, "нога", "body_leg")
        val enity13 = EntityAffected(3, "торс", "body_torso")
        val enity14 = EntityAffected(4, "торс", "body_torso")
        val enity21 = EntityAffected(5, "нога", "body_leg")
        val enity22 = EntityAffected(6, "нога", "body_leg")
        val list1: MutableList<EntityAffected> = ArrayList()
        list1.add(enity11)
        list1.add(enity12)
        list1.add(enity13)
        list1.add(enity14)
        val list2: MutableList<EntityAffected> = ArrayList()
        list2.add(enity21)
        list2.add(enity22)
        affectedLive1.value = list1
        affectedLive2.value = list2
    }
    fun add(date:Long,
            newArea: RequestBody,
            newView: RequestBody,
            newKind: RequestBody,
            files: List<MultipartBody.Part>,
    ): Observable<ResponseBody> {
        val accesToken=tokenModel_.loadAccesToken()!!
        return when (files.size){
            0-> service_.addRashReport(accesToken,date,newArea,newView,newKind,files[0])
            1-> service_.addRashReport(accesToken,date,newArea,newView,newKind,files[0],files[1])
            2-> service_.addRashReport(accesToken,date,newArea,newView,newKind,files[0],files[1],files[2])
            else-> service_.addRashReport(accesToken,date,newArea,newView,newKind,files[0],files[1],files[2])
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
    fun redact(){
        networkHandler.check()
        service_.redactRashReport()
    }
}