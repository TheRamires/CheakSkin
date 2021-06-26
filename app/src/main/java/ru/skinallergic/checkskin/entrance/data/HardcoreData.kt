package ru.skinallergic.checkskin.entrance.data

import ru.skinallergic.checkskin.utils.CHOOSE_DIAGNOSIS
import ru.skinallergic.checkskin.utils.CHOOSE_REGIONS
import javax.inject.Inject

class HardcoreData @Inject constructor() {

    fun getRegions():List<String>{

        /* var region1= FieldSpinnerEntity(1, "Краснодарский край")
         var region2= FieldSpinnerEntity(2, "Остальная Россия")
         var region3= FieldSpinnerEntity(3, "Остальной мир")*/
        val list=ArrayList<String>()
        list.add(CHOOSE_REGIONS)
        list.add("Краснодарский край")
        list.add("Остальная Россия")
        list.add("Остальной мир")

        return list
    }

    fun getDiagnosis():List<String>{
        /*var diagnosis1= FieldSpinnerEntity(1, "Псориаз")
        var diagnosis2= FieldSpinnerEntity(2, "Атопический дерматит")
        var diagnosis3= FieldSpinnerEntity(3, "Другой диагноз")*/
        val list=ArrayList<String>()
        list.add(CHOOSE_DIAGNOSIS)
        list.add("Псориаз")
        list.add("Атопический дерматит")
        list.add("Другой диагноз")

        return  list
    }
}