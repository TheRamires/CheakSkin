package ru.skinallergic.checkskin.components.healthdiary.components.reminders

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.text.SimpleDateFormat
const val TIME_PATTERN="HH:mm"
abstract class BaseRemindersFragment : Fragment(){
    val simpleTimeParser = SimpleDateFormat(TIME_PATTERN)

    val viewModelFactory = App.getInstance().appComponent.viewModelFactory
    val dateViewModel :DateViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel::class.java)
    }
    val viewModel: RemindersViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(RemindersViewModel::class.java)
    }

    fun initBackGround(imageView: ImageView){
        val height=900
        val width=900
        val offset=+170f

        val params = imageView.layoutParams
        params.height=height
        params.width=width

        val drawable=ResourcesCompat.getDrawable(resources, R.drawable.ic_background_alarm_clock_1, null)
        //imageView.background=drawable
        imageView.setImageDrawable(drawable)
        imageView.layoutParams = params
        imageView.y=offset
    }

    open fun backStack(view: View) {
        Navigation.findNavController(view).popBackStack()
    }
    companion object {
        val simpleTimeParser = SimpleDateFormat(TIME_PATTERN)
    }
}