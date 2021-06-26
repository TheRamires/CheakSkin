package ru.skinallergic.checkskin.components.profile

import ru.skinallergic.checkskin.view_models.AccountViewModelImpl.NewUser.entity

class NewUser {
    var name: String?
        get() = entity.name
        set(name) {
            entity.name = name!!
        }

    var number: String?
        get() = entity.tel
        set(number){
            if (number!![0].toString().equals("+")){
                entity.tel=number.substring(1)
            } else entity.tel=number

        }

    var regionId: Int?
        get() = entity.regionId
        set(id) {
            entity.regionId=id
        }

    var diagnosisId:Int?
        get() = entity.diagnosisId
        set(id) {
            entity.diagnosisId=id
        }

    var genderId: Int?
        get() = entity.gender
        set(gender) {
            entity.gender=gender!!
        }
}