package ru.skinallergic.checkskin.components.healthdiary.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.MultipartManager
import ru.skinallergic.checkskin.components.healthdiary.data.AreaEntity
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.repositories.AffectedArreaRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.handlers.ToastyManager
import java.io.File
import javax.inject.Inject

class AffectedAreaRedactViewModel@Inject constructor(
        val repository: AffectedArreaRepository,
        val multipartManager: MultipartManager,
        val toastyManager: ToastyManager
        ): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    val saved =MutableLiveData<Boolean>()

    val map : MutableMap<Int,MutableMap<Int, AreaEntity>> = mutableMapOf()

    val newAreaLive=MutableLiveData<Int>()
    val newViewLive=MutableLiveData<Int>(0)
    val newKindsLive=MutableLiveData<MutableList<Int>>()

    fun getNewArea(): Int?=newAreaLive.value
    fun getNewView(): Int=newViewLive.value!!
    fun getNewKind(): List<Int>? = newKindsLive.value
    fun addNewKind(value: Int){
        Loger.log("value $value")
        var temp: MutableList<Int>? = newKindsLive.value
        if (temp==null){
            temp= mutableListOf()
        }
        Loger.log("old $temp")

        if (!temp.contains(value)){
            temp.add(value)
        }

        newKindsLive.value= temp!!
    }
    fun removeKind(value: Int){
        var temp: MutableList<Int>? = newKindsLive.value
        Loger.log("removeKind old $temp")
        if (temp==null){
            return
        }
        if (temp.contains(value)) {
            temp.remove(value)
        }
    }

    val front = MutableLiveData<Boolean>()

    val gettingDataLive=MutableLiveData<GettingData?>()

    val bitmaps: MutableList<Bitmap?> = arrayListOf(null, null, null)

    fun gettingData(date: String){
        baseRepository.getData(date, gettingDataLive, null)

    }
    fun checkReportField(files: List<File>): Boolean{
        return (getNewArea()!=null && getNewView()!=null && getNewKind()!=null && files.isNotEmpty())
    }

    fun addReport(date: Long, files: List<File>){
        val fieldsIsEmpty=!checkReportField(files)
        if (fieldsIsEmpty) {toastyManager.toastyyyy(
                "Выберите зоны, на которых есть сыпь, \nсделайте хотя бы одно фото \nи добавь описание:"
        );return}


        val newArea: RequestBody =multipartManager.createPartFromString(getNewArea()!!)
        val newView: RequestBody =multipartManager.createPartFromString(getNewView()!!)
        val newKind: RequestBody =multipartManager.createPartFromString(getNewKind()!![0]) //Временно только один элемент
        val fileName01="photo_1"; val fileName02="photo_2"; val fileName03="photo_3"
        val multiParts = mutableListOf<MultipartBody.Part>()
        for(count in files.indices){
            var name="nothing"
            when (count){
                0->name=fileName01
                1->name=fileName02
                2->name=fileName03
            }
            multiParts.add(
                    multipartManager.prepareFilePart(name, files[count]))
        }
        compositeDisposable.add(repository.add(date/1000, newArea, newView, newKind, multiParts)
                .subscribe {
                    saved.value=true
                    Loger.log(it.string()) })

    }

    fun editReport(){

    }

    fun isChanged(): Boolean{
        return true
    }

    fun addBitMapToList(img: Bitmap){
        for (i in bitmaps.indices){
            if (bitmaps[i]==null) {
                bitmaps[i]=img
                return
            } else if(i==bitmaps.size-1){
                bitmaps[i]=img
            }
        }
    }
    fun checkArea(): Boolean{
        if (getNewArea()==null){
            toastyManager.toastyyyy("Выберите пораженную область")
            Loger.log("Выберите пораженную область")
            return false
        } else return true
    }
    fun putKindsToMap(kinds: List<Int>){
        val area=getNewArea()!!
        val view=getNewView()

        val temp = map[area]
        if (temp==null){
            map[area] = hashMapOf(0 to AreaEntity(), 1 to AreaEntity())
        }

        Loger.log("area "+getNewArea())
        Loger.log("area "+area)

        map[area]!![view]!!.kind=kinds

        Loger.log("putKinds map $map")
    }
    fun putPhotoToMap(id:Int,bitmap: Bitmap){
        val area=getNewArea()!!
        val view=getNewView()

        val temp= map[area]?.get(view)
        if (temp==null){
            map[area] = hashMapOf(view to AreaEntity())
        }
        val bitmaps= map[area]!![view]!!.bitmaps
        if (bitmaps==null || bitmaps.size<3){
            map[area]!![view]!!.bitmaps= mutableListOf(null,null,null)
        }

        map[area]!![view]!!.bitmaps!![id] = bitmap

    }

    fun getKindsFromMap() : List<Int>? {
        val area=getNewArea()
        val view=getNewView()
        return map[area]?.get(view)?.kind
    }
    fun getPhotosFromMap(): List<Bitmap?>?{
        val area=getNewArea()
        val view=getNewView()
        return map[area]?.get(view)?.bitmaps
    }
}
