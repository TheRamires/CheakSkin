package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicEntity
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicWriter
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import javax.inject.Inject

class AllergenesViewModel @Inject constructor(val repository: FoodRepository,
                                              val tokenModel: TokenModelImpls
): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }

    lateinit var productList : MutableLiveData<ArrayList<AllergicWriter>>

    lateinit var oldList :ArrayList<AllergicWriter>
    lateinit var newList :ArrayList<AllergicWriter>

    val isLoaded = MutableLiveData<Any>()
    val isAdded = MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

    fun initAndClear(){
        oldList =arrayListOf()
        newList =arrayListOf()
        productList = MutableLiveData<ArrayList<AllergicWriter>>(arrayListOf())
    }


    val backSave = MutableLiveData<Boolean>()

    fun getAddingList(): List<AllergicWriter>{
        val addList = mutableListOf<AllergicWriter>()
        for (new in newList){
            var isNew =true
            for (old in oldList){
                if (old.id==new.id){
                    isNew=false
                }
            }
            if (isNew){addList.add(new)}
            //check DeleteList Contains
            if (getDeleteList().contains(new)){
                addList.remove(new)
            }
        }
        return addList
    }
    fun getDeleteList( ): List<Int>{
        val removeList = mutableListOf<AllergicWriter>()

        for (old in oldList){
            var deleted=true
            for (new in newList){
                if (old.id==new.id){
                    deleted=false
                }
            }
            if (deleted){removeList.add(old)}
        }

        val finalListId= mutableListOf<Int>()
        for (position in removeList){
            finalListId.add(position.id)
        }

        return finalListId
    }

    fun getAllergens(){
        compositeDisposable.add(
                repository.getAllergens(1) //Сделать пагинацию
                        ?.subscribe {
                            val list=it.data
                            if (list != null) {
                                val finalList=convertToAllergicWriter(list)
                                productList.value=finalList
                                for (position in finalList){
                                    oldList.add(position)
                                    newList.add(position)
                                }
                            }
                        }
        )
    }


   /* fun getAllergens(){
        compositeDisposable.add(
                repository.getAllergens(1) //Сделать пагинацию
                        ?.subscribe { }
        )
        oldList= arrayListOf()
        newList=oldList
    }
*/

    fun saveAll(): Boolean{
        val addingList=getAddingList()
        if (addingList.isNotEmpty()){
            addAllergens(addingList)
        }

        val deleteList=getDeleteList()
        if (deleteList.isNotEmpty()){
            for (id in deleteList){
                deleteAllergens(id)
            }
        }
        return true
    }

    fun addAllergens(list: List<AllergicWriter>, backSave: MutableLiveData<Boolean>? = null){
        for (entity in list){
            compositeDisposable.add(
                    repository.addAllergens(entity.name)
                            ?.subscribe({
                                if (it == "Ok") {
                                    //backSave?.value=true
                                }
                            }, {})
            )
        }
    }

    fun deleteAllergens(id: Int){
        repository.deleteAllergens(id)

    }
    fun conditionOfAdding(list: List<AllergicWriter>): Boolean{
        println("list $list")
        if (list.isEmpty()){return true}
        val isFully=list[list.size - 1].isFully()
        return isFully
    }
}

fun MutableLiveData<ArrayList<AllergicWriter>>.changeName(name: String, id: AllergicWriter){

}

fun MutableLiveData<ArrayList<AllergicWriter>>.add(position: AllergicWriter){
    var newList  =this.value
    if (newList!=null){
        newList.add(position)
    }else {
        newList= arrayListOf(position)
    }
    this.value=newList
}

fun MutableLiveData<ArrayList<AllergicWriter>>.delete(position: AllergicWriter){
    var newList  =this.value
    println("newList $newList, posotopn $position")
    if (newList!=null && newList.contains(position)){
        newList.remove(position)
    }else {
        newList= arrayListOf(position)
    }
    this.value=newList
}
fun MutableLiveData<ArrayList<AllergicWriter>>.markSavedPosition(id: Int){
    var newList  =this.value
    if (newList != null) {
        for (position in newList){
            if (position.id==id){
                position.alreadySaved=true
            }
        }
    }
    this.value=newList
}

fun convertToAllergicWriter(list: List<AllergicEntity?>): ArrayList<AllergicWriter>{
    Loger.log("-------------------------------------convertToAllergicWriter")
    val allergicWriterList= arrayListOf<AllergicWriter>()
    for (allergicEntity in list){
        allergicWriterList.add(
                AllergicWriter(id = allergicEntity!!.id, name = allergicEntity.name.toString())
        )
    }
    Loger.log("-------------------------------------allergicWriterList $allergicWriterList")
    return allergicWriterList
}