package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentFoodCalendarBinding
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.util.*

class FoodCalendarFragment : BaseFoodFragment(){
    lateinit var backStack: ImageButton
    lateinit var searchButton: ImageView
    lateinit var datePicker: DatePicker
    lateinit var binding: FragmentFoodCalendarBinding
    lateinit var acceptButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFoodCalendarBinding.inflate(inflater)
        val view= binding.root
        backStack=binding.backBtn
        searchButton=binding.searchBtn
        datePicker = binding.datePicker
        acceptButton=binding.acceptButton

        return view
    }
    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener { popBack(it) }
        searchButton.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodCalendarFragment_to_searchFoodFragment2) }
        acceptButton.setOnClickListener { toAccept(it,dateViewModel) }

        setDate(datePicker, dateViewModel)

        oldSdkMode()
    }
    fun setDate(datePicker: DatePicker, dateViewModel: DateViewModel){
        val day = dateViewModel.getDate(DateViewModel.DAY_FORMAT).toInt()
        val month = dateViewModel.getDate(DateViewModel.MONTH_FORMAT).toInt() - 1
        val year = dateViewModel.getDate(DateViewModel.YEAR_FORMAT).toInt()
        datePicker.updateDate(year, month, day)
    }

    fun setDateLive(date: Date, dateViewModel: DateViewModel) {
        val realCurrent = Date()
        if (date.time > realCurrent.time) {
            dateViewModel.dateLive.setValue(realCurrent)
            toastyManager.toastyyyy("Дата не может быть позднее текущей")
        } else dateViewModel.dateLive.setValue(date)
    }

    fun getDate(dateViewModel: DateViewModel): String? {
        val monthOfYear = datePicker.month
        val dayOfMonth = datePicker.dayOfMonth
        val year = datePicker.year
        val month = String.format("%02d", monthOfYear + 1)
        val day = String.format("%02d", dayOfMonth)

//**********************************часовыой пояс
        //String dateString=day+"."+month+"."+year;
        //Date date=dateViewModel.simpleFormattingToDate(dateString);
        val dateString = "12:00 $day.$month.$year" //stump
        val date = dateViewModel.simpleFormattingToDateStump(dateString) //stump
        //**********************************часовыой пояс
        setDateLive(date, dateViewModel)
        Loger.log("unix " + dateViewModel.dateUnix)
        Loger.log("calendar " + dateViewModel.date)
        return dateString
    }

    fun toAccept(view: View,dateViewModel: DateViewModel) {
        getDate(dateViewModel)
        popBack(view)
    }
    fun oldSdkMode(){
        if (Build.VERSION.SDK_INT >= 26) {
            Loger.log("version " + Build.VERSION.SDK_INT)
            acceptButton.setVisibility(View.GONE)
            datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val month = String.format("%02d", monthOfYear + 1)
                val day = String.format("%02d", dayOfMonth)

                //**********************************часовыой пояс
                //String dateString=day+"."+month+"."+year;
                //Date date=dateViewModel.simpleFormattingToDate(dateString);
                val dateString = "12:00 $day.$month.$year" //stump
                val date = dateViewModel.simpleFormattingToDateStump(dateString) //stump
                setDateLive(date, dateViewModel)
                //**********************************часовыой пояс
                Loger.log("unix " + dateViewModel.dateUnix)
                Loger.log("calendar " + dateViewModel.date)
                popBack(view)
            }
        }
    }
}