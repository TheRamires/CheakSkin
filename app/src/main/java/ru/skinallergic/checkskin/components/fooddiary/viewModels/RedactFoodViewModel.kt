package ru.skinallergic.checkskin.components.fooddiary.viewModels
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import javax.inject.Inject

class RedactFoodViewModel @Inject constructor(): ViewModel() {
    var oldList : List<ProductEntity> = listOf<ProductEntity>()
    var newList : List<ProductEntity> = mutableListOf<ProductEntity>()

    fun getAddList( ): List<ProductEntity>{
        val addList = mutableListOf<ProductEntity>()
        for (new in newList){
            var isNew =true
            for (old in oldList){
                if (old.id==new.id){
                    isNew=false
                }
            }
            if (isNew){addList.add(new)}
        }

        /*for (new in newList){
            if(!oldList.contains(new)){
                addList.add(new)
            }
        }*/
        return addList
    }
    fun getDeleteList( ): List<Int>{
        val removeList = mutableListOf<Int>()
        for (old in oldList){
            var deleted=true
            for (new in newList){
                if (old.id==new.id){
                    deleted=false
                }
            }
            if (deleted){removeList.add(old.id)}
        }

        /*for (old in oldList){
            if(!newList.contains(old)){
                removeList.add(old.id)
            }
        }*/
        return removeList
    }
    fun getRedactList( ): List<ProductEntity>{
        val redactList = mutableListOf<ProductEntity>()
        for (old in oldList){
            for (new in newList){
                if (old.id==new.id){

                    if (old.name==new.name && old.weight==new.weight && old.allergic==new.allergic){
                        continue
                    } else redactList.add(new)

                }
            }
        }
        return redactList
    }
    fun saveCondition(): Boolean{
        if (getAddList().isEmpty() && getDeleteList( ).isEmpty() && getRedactList( ).isEmpty()){
            return false
        } else return true
    }
}
fun List<ProductEntity>.redactName(entity: ProductEntity, name: String){
    val index=this.indexOf(entity)
    if (index>=0){
        this.get(index).name=name

    }
}
fun List<ProductEntity>.redactWeight(entity: ProductEntity, weight: String){
    val index=this.indexOf(entity)
    if (index>=0){
        this.get(index).weight=weight
    }
}
fun List<ProductEntity>.redactAllergic(entity: ProductEntity, allergic: Boolean){
    val index=this.indexOf(entity)
    if (index>=0){
        this.get(index).allergic=allergic
    }
}

