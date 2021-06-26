package ru.skinallergic.checkskin.components.healthdiary.components.reminders

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.R

abstract class BaseRemindersFragment : Fragment(){

    fun initBackGround(imageView: ImageView){
        val height=900
        val width=900
        val offset=+170f

        val params = imageView.layoutParams
        params.height=height
        params.width=width

        val drawable=ResourcesCompat.getDrawable(resources, R.drawable.ic_background_alarm_clock_1, null)
        imageView.background=drawable
        imageView.layoutParams = params
        imageView.y=offset
    }

    open fun backStack(view: View) {
        Navigation.findNavController(view).popBackStack()
    }
}