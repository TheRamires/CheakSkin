package ru.skinallergic.checkskin.entrance.data

import ru.skinallergic.checkskin.entrance.pojo.Datass
import ru.skinallergic.checkskin.utils.GENDER_NONE

 data class UserEntity(
        var name:String?="",
        var region:String?="",
        var regionId: Int?=0,
        var diagnosis: String?="",
        var diagnosisId: Int?=0,
        var gender: Int?= GENDER_NONE,
        var tel: String?=""
){
  fun setData(datass: Datass) {
   name= datass.name.toString()
   region=datass.region?.name.toString()
   regionId=datass.region?.id
   diagnosis=datass.diagnosis?.name.toString()
   diagnosisId=datass.diagnosis?.id
   gender= datass.gender!!
   tel= datass.tel.toString()
  }
 }
