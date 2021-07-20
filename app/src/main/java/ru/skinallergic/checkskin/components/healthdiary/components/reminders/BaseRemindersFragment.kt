package ru.skinallergic.checkskin.components.healthdiary.components.reminders

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindCommonViewModel
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.text.SimpleDateFormat
const val TIME_PATTERN="HH:mm"
const val REMIND_IN_AN_NEVER=0
const val REMIND_IN_AN_5_MIN=300
const val REMIND_IN_AN_30_MIN=1800
const val REMIND_IN_AN_HOUR=3600
const val REPEAT_NEVER=0
const val REPEAT_EVERY_DAY=1
const val REPEAT_EVERY_WEEK=2
const val REPEAT_EVERY_TWO_WEEK=3
const val REPEAT_EVERY_MONTH=4
const val REPEAT_EVERY_YEAR=5
abstract class BaseRemindersFragment : Fragment(){
    val typeList = listOf("Визит к врачу","Лечение")
    val simpleTimeParser = SimpleDateFormat(TIME_PATTERN)

    val viewModelFactory = App.getInstance().appComponent.viewModelFactory
    val dateViewModel :DateViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel::class.java)
    }
    val viewModel: RemindersViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(RemindersViewModel::class.java)
    }

    val viewModelCommon: RemindCommonViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(RemindCommonViewModel::class.java)
    }

    fun initBackGround(imageView: ImageView){
        val offset=+170f
        /*val height=900
        val width=900

        val params = imageView.layoutParams
        params.height=height
        params.width=width

        val drawable=ResourcesCompat.getDrawable(resources, R.drawable.ic_background_alarm_clock_1, null)
        //imageView.background=drawable
        imageView.setImageDrawable(drawable)
        imageView.layoutParams = params*/
        imageView.y=offset
    }

    open fun backStack(view: View) {
        Navigation.findNavController(view).popBackStack()
    }
    companion object {
        val simpleTimeParser = SimpleDateFormat(TIME_PATTERN)
    }
}