package ru.skinallergic.checkskin.entrance.helper_classes

import android.widget.CheckBox
import java.util.regex.Matcher
import java.util.regex.Pattern

object ValidatorNumber{
    var isChecked=true
    var isEntered=false

    fun cheackNumber(number: Any):Boolean{
        val number=number.toString()
        isEntered = number.length==18 && number[17].isDigit()
        return isEntered
    }

    fun cheackBox(ch1: CheckBox, ch2: CheckBox){
        isChecked=ch1.isChecked && ch2.isChecked
    }

    fun validate()= isChecked && isEntered

    fun helpExtractNumber(text: Any):String{
        val text=text.toString()
        val pattern = Pattern.compile("[0-9]+")
        val matcher: Matcher = pattern.matcher(text)
        var number= StringBuilder()

        while (matcher.find()) {
            number.append(matcher.group())
        }
        return number.toString()
    }
}