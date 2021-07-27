package ru.skinallergic.checkskin.components.fooddiary.viewModels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicWriter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.repositories.FoodRepository
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import javax.inject.Inject

class AllergenesViewModel @Inject constructor(val repository: FoodRepository): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repository

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }
    val productList = MutableLiveData<ArrayList<AllergicWriter>>(arrayListOf())

    var oldList = ArrayList<AllergicWriter>(arrayListOf())
    var newList =ArrayList<AllergicWriter>(arrayListOf())

    val isLoaded = MutableLiveData<Any>()
    val isAdded = MutableLiveData<Boolean>()
    val isDeleted = MutableLiveData<Boolean>()

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
        }
        return addList
    }
    fun getDeleteList( ): List<AllergicWriter>{
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
        return removeList
    }

    fun getAllergens(){
        repository.getAllergens(1) //Сделать пагинацию
        oldList= arrayListOf()
        newList=oldList
    }
    fun saveCondition(): Boolean{
        return true
    }

    fun addAllergens(list: List<AllergicWriter>, backSave: MutableLiveData<Boolean>?=null){
        for (entity in list){
            compositeDisposable.add(
                    repository.addAllergens(entity.name)
                            ?.subscribe ({
                                         if (it=="Ok"){
                                             //backSave?.value=true
                                         }
                            },{})
            )
        }
    }

    fun deleteAllergens(id: Int){
        repository.deleteAllergens(id)

    }
    fun conditionOfAdding(list: List<AllergicWriter>): Boolean{
        println("list $list")
        if (list.isEmpty()){return true}
        val isFully=list[list.size-1].isFully()
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