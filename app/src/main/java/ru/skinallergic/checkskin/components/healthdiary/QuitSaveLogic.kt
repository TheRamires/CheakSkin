package ru.skinallergic.checkskin.components.healthdiary

import androidx.fragment.app.FragmentManager
import ru.skinallergic.checkskin.components.profile.ActionFunction
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment
import ru.skinallergic.checkskin.components.profile.NavigationFunction

class QuitSaveLogic {
    fun logic(condition: Boolean, popBack: () -> Unit, save: () -> Unit, fragmentManager: FragmentManager){
        if (condition) {
            quitSaveDialog({ popBack() }, { save() }, fragmentManager
            )
        } else {
            popBack()
        }
    }

    fun quitSaveDialog(popBack: () -> Unit, save: () -> Unit, fragmentManager: FragmentManager) {
        val negative = object : ActionFunction {
            override fun action() {
                popBack()
            }
        }
        val positive = object : ActionFunction {
            override fun action() {
                save()
            }
        }
        val navigation = object : NavigationFunction {
            override fun navigate() {
                //empty
            }
        }
        val dialog = DialogTwoFunctionFragment(
                "Сохранить изменения", negative, positive, navigation)
        dialog.show(fragmentManager, "dialog")
    }
}

interface BackNavigation {
    fun nav()
}