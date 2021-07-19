package ru.skinallergic.checkskin.components.healthdiary.viewModels

import android.graphics.Bitmap
import androidx.databinding.ObservableField
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
import java.util.regex.Pattern
import javax.inject.Inject

const val MESSAGE ="Выберите зоны, на которых есть сыпь, \nсделайте хотя бы одно фото \nи добавьте описание."
const val FILE_NAME_01="photo_1"; const val FILE_NAME_02="photo_2"; const val FILE_NAME_03="photo_3"

class AffectedAreaCommonViewModel@Inject constructor(
        val repository: AffectedArreaRepository,
        val multipartManager: MultipartManager,
        val toastyManager: ToastyManager
): BaseViewModel() {

    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable=this.compositeDisposable
        baseRepository.expiredRefreshToken=this.expiredRefreshToken
    }
    var someChanged=false

    val saved =MutableLiveData<Boolean>()
    val loaded=MutableLiveData<Boolean>()
    val notSaving =MutableLiveData<Boolean>()

    var photoDirectoryInMemory: File?=null

    var oldMap : List<Rash> = listOf()
    var newMap : MutableMap<Int, MutableMap<Int, AreaEntity>> = mutableMapOf()
    //to init in onCreate of AffectedAreaRedactBodyFragment --NONE!!
    //to init in getData function
    fun clearMaps(){
        oldMap=listOf()
        newMap=mutableMapOf()
    }
    fun copyToNewMap (){
        //if (oldMap.isEmpty()){return}  //**delete for testing

        newMap.clear()
        for (rash in oldMap){
            val area=rash.area!!
            val view: Int=rash.view!!
            val kinds: List<Int> = rash.kinds!!
            val photos : List<String?> = listOf(rash.photo_1, rash.photo_2, rash.photo_3)
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
            newMap[area]!![view]=AreaEntity(kinds, filesPhoto)
        }
        Loger.log("**copyToNewMap \n oldMap $oldMap \n newMap $newMap")
    }
    fun MutableMap<Int, MutableMap<Int, AreaEntity>>.checkNull(){
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
    fun checkReportField(files: List<File?>?, area: Int?, view: Int?, kinds: List<Int?>?): Boolean{
        Loger.log("checkReportField. getNewArea ${area}; getNewView ${view}; getNewKind ${kinds}; files $files")
        return (area!=null && view!=null && kinds!=null && kinds.isNotEmpty() && files!=null && files?.isNotEmpty())
    }

    fun addReport(date: Long){
        val allAreas=newMap.keys
        if (allAreas.isEmpty()){toastyManager.toastyyyy(MESSAGE, true);return}
        if (!checkFullyFields(allAreas)){return} // **самая важная проверка -- "есть ли незаполненные поля"**

        for (area in allAreas){
            val allViews=newMap[area]?.keys
            if (allViews==null || allViews.isEmpty()){toastyManager.toastyyyy(MESSAGE, true);return}

            for (view in allViews){
                if(view ==null){continue}
                val areaEntity= newMap[area]!![view]
                val kind=areaEntity?.kind
                val files=areaEntity?.photos
                if (kind==null || files.isEmpty()){toastyManager.toastyyyy(MESSAGE, true);return}
                Loger.log("***********☺ files 0 //************************************ $files")

                addReport(date, area, view, kind, files)
            }
        }
    }
    fun checkFullyFields(allAreas: MutableSet<Int>): Boolean{
        for (area in allAreas){
            val allViews=newMap[area]?.keys
            if (allViews==null || allViews.isEmpty()){toastyManager.toastyyyy(MESSAGE, true);return false}

            for (view in allViews){
                if(view ==null){continue}
                val areaEntity= newMap[area]!![view]
                val kind=areaEntity?.kind
                val files=areaEntity?.photos
                if (kind==null || kind.isEmpty() ||files.isEmpty()){toastyManager.toastyyyy(MESSAGE, true);return false}

            }
        }
        return true
    }

    private fun <T>List<T?>?.isEmpty(): Boolean{
        if (this==null){return true}
        if (this.size==0){return true}
        var boolean=true
        for (position in this){
            if (position!=null){boolean=false}
        }
        return boolean
    }

    private fun addReport(date: Long, area: Int, view: Int, kinds: List<Int>, files: List<File?>?){
        val fieldsIsEmpty=!checkReportField(files, area, view, kinds)
        if (fieldsIsEmpty) {
            toastyManager.toastyyyy(MESSAGE, true);return}

        val newArea: RequestBody =multipartManager.createPartFromString(area)
        val newView: RequestBody =multipartManager.createPartFromString(view)
        val newKinds: RequestBody =multipartManager.createPartFromString(kinds) //Временно только один элемент
        val multiParts = mutableListOf<MultipartBody.Part>()

        //remove all null**************
        val finalFiles = files.removeNulls()

        //ceate file name**************
        for(count in finalFiles.indices){
            var name="nothing"
            when (count){
                0 -> name = FILE_NAME_01
                1 -> name = FILE_NAME_02
                2 -> name = FILE_NAME_03
            }
            val tempFile=finalFiles.get(count)

            val finalFile=tempFile
            Loger.log("finalFile "+finalFile)

            if (finalFile!=null){
                multiParts.add(
                        multipartManager.prepareFilePart(name, finalFile))
            }
        }
        //check changes****************
        val id=isOldPosition(area, view)
        if (id==null){
            addPosition(date, newArea, newView, newKinds, multiParts)
        } else{
            redactPosition(id, newArea, newView, newKinds, multiParts)
        }
    }

    private fun addPosition(date: Long,
                            newArea: RequestBody,
                            newView: RequestBody,
                            newKinds: RequestBody,
                            files: List<MultipartBody.Part>){
        compositeDisposable.add(repository.add(date / 1000, newArea, newView, newKinds, files)
                .doOnSubscribe { progressBar.set(true) }
                .doOnComplete { progressBar.set(false) }
                .doOnError { progressBar.set(false) }
                .subscribe({
                    saved.value = true
                    Loger.log(it.string())
                }, {
                    Loger.log("throwable $it")
                }))
    }

    private fun redactPosition(id: Int,
                               newArea: RequestBody,
                               newView: RequestBody,
                               newKinds: RequestBody,
                               files: List<MultipartBody.Part>){
        //Loger.log("redactPositions "+files[0]+files[1]+files[2])
        var finalFiles = mutableListOf<File>()

        val observable =repository.redact(id, newArea, newView, newKinds, files)
        observable?.let {
            compositeDisposable.add(it
                    .doOnSubscribe { progressBar.set(true) }
                    .doOnComplete { progressBar.set(false) }
                    .doOnError { progressBar.set(false) }
                    .subscribe({
                        saved.value = true
                        Loger.log(it.string())
                    }, {
                        Loger.log("redactPosition throwable $it")
                    })
            )
        }
    }

    fun delete(id: Int, area: Int, view: Int){  //area, view - testing
        Loger.log("delete **1  oldMap $oldMap")
        Loger.log("delete **1  newMap $newMap")
        compositeDisposable.add(repository.delete(id)
                .doOnSubscribe { splashScreenOn.set(true) }
                .doOnComplete { splashScreenOn.set(false) }
                .subscribe ({
                    toastyManager.toastyyyy("Позиция успешно удалена")
                    //****************************
                    Loger.log("oldMap 1 $oldMap")
                    copyToNewMap()
                    Loger.log("oldMap 2 $oldMap")
                    Loger.log("newMap 2 $newMap")

                            },{
                    toastyManager.toastyyyy("При удалении позиции произошел сбой")
                })
        )
    }

    fun notSaving(){
        toastyManager.toastyyyy(MESSAGE, true)
        notSaving.value=true

    }
    fun data(oldDate: Long, progress: ObservableField<Boolean>){
        letsGetData(oldDate, progress)
    }
    fun data(oldDate: Long){
        letsGetData(oldDate, splashScreenOn)
    }

    fun letsGetData(oldDate: Long, progress: ObservableField<Boolean>){

       /* val HOUR = (3600 * 1000).toLong() //Временное решение
        val newData=(oldDate/1000)+12*HOUR*/
        val newData=oldDate/1000
        Loger.log("data start for view Model $newData")
        compositeDisposable.add(
                repository.date((newData).toString())?.let {
                    it.doOnSubscribe { progress.set(true) }
                            .doOnComplete { progress.set(false) }
                            .subscribe({
                                val data: List<Rash>
                                Loger.log("**************data $it")
                                if (it.message == null) {
                                    Loger.log("**********1 *letsGetData**message  ${it.message}")
                                    loaded.value = false
                                } else {
                                    data = it.data?.rashes!!
                                    Loger.log("**********2 *letsGetData* data $data")
                                    Loger.log("*************2.1 *letsGetData**message loaded "+loaded.value)
                                    oldMap = emptyList()
                                    oldMap = data //**************************************************
                                    copyToNewMap()
                                    loaded.value=true
                                    Loger.log("*************2.2 *letsGetData**message loaded "+loaded.value)
                                }
                            }, {
                                splashScreenOn.set(false) //testing
                            })
                }
        )
    }
    fun someChanging(){
        someChanged=true
        //toastyManager.toastyyyy("someChanged $someChanged")
    }

    fun isChanged(): Boolean{
        //toastyManager.toastyyyy("someChanged $someChanged")
        if(someChanged==null){return false}
        return someChanged
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
    fun putKindsToMap(kinds: List<Int>, area: Int, view: Int){
        //************** вынести в отдельный метод
        val temp = newMap[area]
        if (temp==null){
            newMap[area] = hashMapOf()
        }
        val areaEntity=newMap[area]!![view]
        if (areaEntity==null){
            newMap[area]!![view]=AreaEntity()
        }

        Loger.log("area " + getNewArea())
        Loger.log("area " + area)

        newMap[area]!![view]!!.kind=kinds

        Loger.log("putKinds map $newMap")
    }

    fun putKindsToMap(kinds: List<Int>){
        val area=getNewArea()!!
        val view=getNewView()
        putKindsToMap(kinds, area, view)
    }

    fun putPhotoToMap(id: Int, file: File){
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
            newMap[area]!![view]!!.photos= mutableListOf(null, null, null)
        }
        newMap[area]!![view]!!.photos!![id] = file
    }
    fun putSavedPhotoToOldMap(
            area: Int, view: Int,
            photoPath01: String?, photoPath02: String?, photoPath03: String?,
    ){
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
    fun clearPhotoDirectory(path: File?): Boolean {
        if(path==null){return false }
        if (path.exists()) {
            val files = path.listFiles() ?: return true
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    clearPhotoDirectory(files[i])
                } else {
                    files[i].delete()
                }
            }
        }
        return path.delete()
    }

    override fun onCleared() {
        Loger.log("Common view model onCleared")
        super.onCleared()
        unsubscribe()
        photoDirectoryInMemory?.let { directory->
            clearPhotoDirectory(directory)
            photoDirectoryInMemory=null
        }
    }
    fun cleanPosition():Boolean?{

        if (newMap.get(getNewArea()) == null) {
            return true
        }
        if (newMap.get(getNewArea())?.get(getNewView()) == null) {
            return true
        }
        return false
    }
    fun allPositionIsNull(): Boolean{
        var boolean=true


        //в противном случае ищем null позицию в мэпе и по ее индексу выбираем imageView
        val fileList: MutableList<File?>? = newMap.get(
                getNewArea()
        )?.get(
                getNewView()
        )?.photos
        if (fileList==null){return true}
        for (i in fileList?.indices!!) {
            if (fileList[i] != null) {
                boolean=false
            }
        }
        return boolean
    }

    /*fun findCleanImageViewIndex():Int?{
        var result: Int?=null

        //в противном случае ищем null позицию в мэпе и по ее индексу выбираем imageView
        val fileList = newMap[getNewArea()]?.get(getNewKind())?.photos
        if (fileList==null){return null}
        for (i in fileList.indices) {
            if (fileList[i] == null) {
                result=i
            }
        }
        return result
    }*/

    fun clearComposite(){
        Loger.log("clearComposite")
        compositeDisposable.clear()
        splashScreenOn.set(false)
        progressBar.set(false)
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

fun regularHttp(path: String): Boolean{ // проверка, true если файл уже загружен с сервера, тогда 2ой раз его отпарвлять не надо
    val regex="http"
    val pattern=Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher= pattern.matcher(path)
    return matcher.find()
}

