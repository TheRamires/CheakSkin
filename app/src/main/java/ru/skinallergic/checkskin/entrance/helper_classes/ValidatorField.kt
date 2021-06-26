package ru.skinallergic.checkskin.entrance.helper_classes

import ru.skinallergic.checkskin.entrance.data.UserEntity
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.utils.CHOOSE_DIAGNOSIS
import ru.skinallergic.checkskin.utils.CHOOSE_REGIONS
import ru.skinallergic.checkskin.utils.GENDER_NONE
import java.util.regex.Pattern
import javax.inject.Inject

class ValidatorField @Inject constructor(val toastyManager: ToastyManager) {
    var turnValidateName: Boolean=false

    //val regx="^[а-яА-Я][а-яА-Я0-9-_.]{1,20}$"
    val regx="^[а-яА-Я][а-яА-Я0\\s.]{1,20}$"

    var pattern = Pattern.compile(regx)

    fun validateName(text: String):Boolean{
        if (turnValidateName) {
            var matcher = pattern.matcher(text)
            var matches = matcher.matches()
            return matches
        } else return true
    }

    fun validate(currentUser: UserEntity) :Boolean{
        if (!validateName(currentUser.name!!) && turnValidateName){
            toastyManager.toastyyyy("Имя должно содержать буквы только русского алфавита")
            return false
        } else if (currentUser.name.equals("") || currentUser.name.equals(" ")){
            toastyManager.toastyyyy("Введите имя")
            return false
        } else {
            return currentUser.name != "" &&
                    !currentUser.regionId?.toString().equals("") &&
                    !currentUser.regionId?.toString().equals(CHOOSE_REGIONS) &&
                    !currentUser.diagnosisId?.toString().equals("") &&
                    !currentUser.diagnosisId?.toString().equals(CHOOSE_DIAGNOSIS) &&
                    !currentUser.gender!!.equals(GENDER_NONE)
        }
    }
}