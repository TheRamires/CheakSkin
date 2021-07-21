package ru.skinallergic.checkskin.components.profile
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

open class DialogNavFragment(
        val message: String,
        val navigationFunction: NavigationFunction,
):DialogFragment()   {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Нет"){ dialog, id ->  dialog.cancel()
                    }
                    .setPositiveButton("Да") { dialog, id -> dialog.cancel(); navigationFunction.navigate()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DialogFunctionFragment(
        val message: String,
        val actionFunction: ActionFunction,
        val navigationFunction: NavigationFunction,
): DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Нет"){ dialog, id ->  dialog.cancel()
                    }
                    .setPositiveButton("Да") { dialog, id -> dialog.cancel(); actionFunction.action(); navigationFunction.navigate()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
class DialogTwoFunctionFragment(
        val message: String,
        val negativeAction: ActionFunction,
        val positiveAction: ActionFunction,
        val navigationFunction: NavigationFunction,
): DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Нет"){ dialog, id -> negativeAction.action(); dialog.cancel()
                    }
                    .setPositiveButton("Да") { dialog, id -> dialog.cancel(); positiveAction.action(); navigationFunction.navigate()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DialogThreeDeleteFunctionFragment(
        val message: String,
        val negativeAction: ActionFunction,
        val positiveAction1: ActionFunction,
        val positiveAction2: ActionFunction,
        val navigationFunction: NavigationFunction,
): DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Отмена"){ dialog, id -> negativeAction.action(); dialog.cancel()
                    }
                    .setNeutralButton("Только это") {  dialog, id -> dialog.cancel(); positiveAction1.action(); navigationFunction.navigate()

                    }
                    .setPositiveButton("Удалить все") { dialog, id -> dialog.cancel(); positiveAction2.action(); navigationFunction.navigate()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DialogActionFragment(
        val message: String,
        val actionFunction: ActionFunction,
): DialogFragment()  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Нет"){ dialog, id ->  dialog.cancel()
                    }
                    .setPositiveButton("Да") { dialog, id -> dialog.cancel(); actionFunction.action()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

interface ActionFunction {
    fun action()
}

interface NavigationFunction {
    fun navigate()
}

class DialogOnlyOneFunc(
    val message: String,
    val positive: String,
    val negative: String,
    val positiveAction1: ActionFunction
):DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton(negative){ dialog, id -> ; dialog.cancel()
                    }
                    .setPositiveButton(positive) { dialog, id -> dialog.cancel(); positiveAction1.action()
                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DialogTwoFunctionAndMessages(
        val message: String,
        val button1: String,
        val positiveAction1: ActionFunction,
        val button2: String,
        val positiveAction2: ActionFunction,
): DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                    //.setMessage("вопрос")
                    .setNegativeButton("Отменить"){ dialog, id -> ; dialog.cancel()
                    }
                    .setNeutralButton(button1) { dialog, id -> dialog.cancel(); positiveAction1.action()
                    }
                    .setPositiveButton(button2){ dialog, id -> dialog.cancel(); positiveAction2.action()

                    }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}