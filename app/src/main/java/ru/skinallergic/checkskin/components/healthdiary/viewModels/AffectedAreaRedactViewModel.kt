package ru.skinallergic.checkskin.components.healthdiary.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.MultipartManager
import ru.skinallergic.checkskin.components.healthdiary.data.AreaEntity
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash
import ru.skinallergic.checkskin.components.healthdiary.repositories.AffectedArreaRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.handlers.ToastyManager
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
const val MESSAGE ="Выберите зоны, на которых есть сыпь, \nсделайте хотя бы одно фото \nи добавьте описание:"

const val FILE_NAME_01="photo_1"; const val FILE_NAME_02="photo_2"; const val FILE_NAME_03="photo_3"

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
    val loaded=MutableLiveData<Boolean>()

    var oldMap : List<Rash> = listOf()
    val newMap : MutableMap<Int,MutableMap<Int, AreaEntity>> = mutableMapOf()
    //to init in onCreate of AffectedAreaRedactBodyFragment --NONE!!
    //to init in getData function
    fun initNewMap (){
        if (oldMap.isEmpty()){return}

        newMap.clear()
        for (rash in oldMap){
            val area=rash.area!!
            val view: Int=rash.view!!
            val kinds: List<Int> = rash.kinds!!
            val photos : List<String?> = listOf(rash.photo_1,rash.photo_2,rash.photo_3)
            val filesPhoto=photos.fileFromStringPath()

            //************** вынести в отдельный метод
            val temp = newMap[area]
            if (temp==null){
                newMap[area] = hashMapOf()
            }
            val areaEntity=newMap[area]!![view]
            if (areaEntity==null){
                newMap[area]!![view]=AreaEntity()
            }
            newMap[area]!![view]=AreaEntity(kinds,filesPhoto)
        }
        Loger.log("**initNewMap \n oldMap $oldMap \n newMap $newMap")
    }
    fun MutableMap<Int,MutableMap<Int, AreaEntity>>.checkNull(){
        val temp=this

    }

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
    fun checkReportField(files: List<File?>?): Boolean{
        Loger.log("checkReportField. getNewArea ${getNewArea()}; getNewView ${getNewView()}; getNewKind ${ getNewKind()}; files $files")
        return (getNewArea()!=null && getNewView()!=null && getNewKind()!=null && files!!.isNotEmpty())
    }

    fun addReport(date: Long){
        val allAreas=newMap.keys
        if (allAreas.isEmpty()){toastyManager.toastyyyy(MESSAGE);return}
        for (area in allAreas){
            val allViews=newMap[area]?.keys
            if (allViews==null || allViews.isEmpty()){toastyManager.toastyyyy(MESSAGE);return}
            for (view in allViews){
                if(view ==null){continue}
                val areaEntity= newMap[area]!![view]
                val kind=areaEntity?.kind
                val files=areaEntity?.photos
                if (kind==null || bitmaps==null){return}
                Loger.log("areaEntity $areaEntity")

                Loger.log("***********☺ files 0 //************************************ $files")

                addReport(date,area,view, kind,files)
            }
        }
    }

    private fun addReport(date: Long, area: Int, view: Int, kinds: List<Int>, files: List<File?>?){
        val fieldsIsEmpty=!checkReportField(files)
        if (fieldsIsEmpty) {
            Loger.log(
                    "Выберите зоны, на которых есть сыпь, \nсделайте хотя бы одно фото \nи добавьте описание:")
            toastyManager.toastyyyy(
                "Выберите зоны, на которых есть сыпь, \nсделайте хотя бы одно фото \nи добавьте описание:"
        );return}

        val newArea: RequestBody =multipartManager.createPartFromString(area)
        val newView: RequestBody =multipartManager.createPartFromString(view)
        val newKinds: RequestBody =multipartManager.createPartFromString(kinds) //Временно только один элемент
        val multiParts = mutableListOf<MultipartBody.Part>()
/*
        val tempFiles=files?.checkHttp()
        Loger.log("************☺ tempFiles $tempFiles")
*/
        //remove all null**************
        val finalFiles = files.removeNulls()
        Loger.log("************☺ finalFiles $finalFiles")

        //ceate file name**************
        for(count in finalFiles.indices){
            var name="nothing"
            when (count){
                0->name= FILE_NAME_01
                1->name=FILE_NAME_02
                2->name=FILE_NAME_03
            }
            val tempFile=finalFiles.get(count)

            //val finalFile : File? = compareWithOldMap(tempFile) ?: continue
            val finalFile=tempFile

            Loger.log("************☺ last finalFile $finalFiles")
            if (finalFile!=null){
                multiParts.add(
                        multipartManager.prepareFilePart(name, finalFile))
            }
        }
        Loger.log("multiParts **$multiParts")
        //check changes****************
        val id=isOldPosition(area,view)
        if (id==null){
            addPosition(date, newArea, newView, newKinds, multiParts)
        } else{
            redactPosition(id, newArea, newView, newKinds, multiParts)
        }
    }
    private fun addPosition(date:Long,
                    newArea: RequestBody,
                    newView: RequestBody,
                    newKinds: RequestBody,
                    files: List<MultipartBody.Part>){

        Loger.log("files 2 //************************************ $files")
        compositeDisposable.add(repository.add(date/1000, newArea, newView, newKinds, files)
                .subscribe ({
                    saved.value=true
                    Loger.log(it.string())
                },{
                    Loger.log("throwable $it")
                }))
    }
    private fun redactPosition(id:Int,
                       newArea: RequestBody,
                       newView: RequestBody,
                       newKinds: RequestBody,
                       files: List<MultipartBody.Part>){
        var finalFiles = mutableListOf<File>()

        val observable =repository.redact(id, newArea, newView, newKinds, files)
        observable?.let {
            compositeDisposable.add(it.subscribe ({
                saved.value=true
                Loger.log(it.string())
            },{
                Loger.log("throwable $it")
            })
            )
        }
/*
        compositeDisposable.add(repository.redact(id, newArea, newView, newKinds, files)
                .subscribe ({
                    saved.value=true
                    Loger.log(it.string())
                },{
                    Loger.log("throwable $it")
                }))*/
    }

    fun data(date:Long){
        Loger.log("data start for view Model")
        compositeDisposable.add(
                repository.date((date/1000).toString())
                        .doOnSubscribe { splashScreenOn.set(true) }
                        .doOnComplete { splashScreenOn.set(false) }
                        .subscribe ({
                            val data: List<Rash>
                            Loger.log("date $it")
                            if (it.message==null){loaded.value = false}
                            else {
                                data=it.data?.rashes!!
                                oldMap=data
                                //initNewMap()
                                loaded.value = true
                            }
                        },{})
        )
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
    fun isOldPosition(area: Int, view: Int):Int?{
        for (position in oldMap){
            if (position.area==area && position.view==view){
                Loger.log("area $area, view $view, position $position")
                return position.id
            }
        }
        return null
    }

    fun putKindsToMap(kinds: List<Int>){
        val area=getNewArea()!!
        val view=getNewView()

        //************** вынести в отдельный метод
        val temp = newMap[area]
        if (temp==null){
            newMap[area] = hashMapOf()
        }
        val areaEntity=newMap[area]!![view]
        if (areaEntity==null){
            newMap[area]!![view]=AreaEntity()
        }

        Loger.log("area "+getNewArea())
        Loger.log("area "+area)

        newMap[area]!![view]!!.kind=kinds

        Loger.log("putKinds map $newMap")
    }

    fun putPhotoToMap(id:Int,bitmap: File){
        val area=getNewArea()!!
        val view=getNewView()
        //************** вынести в отдельный метод
        val temp = newMap[area]
        if (temp==null){
            newMap[area] = hashMapOf()
        }
        val areaEntity=newMap[area]!![view]
        if (areaEntity==null){
            newMap[area]!![view]=AreaEntity()
        }
        //************** вынести в отдельный метод
        /*val temp= newMap[area]?.get(view)
        if (temp==null){
            newMap[area]!![view] = AreaEntity()
            //newMap[area] = hashMapOf(view to AreaEntity())
        }*/
        val bitmaps= newMap[area]!![view]!!.photos
        if (bitmaps==null || bitmaps.size<3){
            newMap[area]!![view]!!.photos= mutableListOf(null,null,null)
        }
        newMap[area]!![view]!!.photos!![id] = bitmap
    }
    fun putSavedPhotoToMap(area: Int, view:Int,
                           photoPath01:String?,  photoPath02:String?,  photoPath03:String?,){
        for (rash in oldMap){
            if (rash.area==area && rash.view==view){
                rash.photo_1=photoPath01
                rash.photo_2=photoPath02
                rash.photo_3=photoPath03
            }
        }
    }

    fun getKindsFromMap() : List<Int>? {
        val area=getNewArea()
        val view=getNewView()
        return newMap[area]?.get(view)?.kind
    }
    fun getPhotosFromMap(): List<File?>?{
        val area=getNewArea()
        val view=getNewView()
        return newMap[area]?.get(view)?.photos
    }
}
fun <T>List<T>?.removeNulls() : List<T>{
    val finalList = mutableListOf<T>()
    this?.let {
        for (position in this){
            if (position!=null){
                finalList.add(position)
            }
        }
    }
    return finalList
}
fun List<String?>.fileFromStringPath(): MutableList<File?>{

    val fileList = mutableListOf<File?>()
    for (path in this){
        if(path==null){
            fileList.add(null)
        }else{
            val file = File(path)
            fileList.add(file)
        }
    }
    return fileList
}
fun File.checkHttp(): File?{
    if (regularHttp(this.absolutePath)){
        return null
    } else return this
}

fun List<File?>.checkHttp():List<File?>{
    val finalList= mutableListOf<File?>()
    for (file in this){
        if (file==null){continue}
        if (regularHttp(file.absolutePath)){
            finalList.add(null)
        } else finalList.add(file)
    }
    return finalList
}

fun regularHttp(path:String): Boolean{ // проверка, true если файл уже загружен с сервера, тогда 2ой раз его отпарвлять не надо
    val regex="http"
    val pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE)
    val matcher= pattern.matcher(path)
    return matcher.find()
}


