package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RemindersViewModel : ViewModel() {
    val timeLive = MutableLiveData<String>()

    var remindsLive = MutableLiveData<ArrayList<EntityReminders>>()
    var entity = ObservableField<EntityReminders>()
    var dateObservable = ObservableField<String>()

    // Текущее время

    fun getCurrencyDate() {
        // Текущее время
        val currentDate = Date()
        // Форматирование времени как "день.месяц.год"
        val dateFormat: DateFormat = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())
        val dateText = dateFormat.format(currentDate)
        dateObservable.set(dateText)
    }

    fun loadData() {
        //for example -----------------------------------------------------------------------------
        val list = ArrayList<EntityReminders>()
        for (i in 0..3) {
            list.add(EntityReminders(i, "Парацетамол$i", "выпить$i", "12:00",
                    "за $i часов", "раз в $i дней"))
        }
        remindsLive.value = list
    }

    fun deletePosition(position: Int) {
        //for example-------------------------------------------------------------------------
        val list_temp = remindsLive.value!!
        val list_new = ArrayList<EntityReminders>()
        for (entity in list_temp) {
            if (entity.id != position) {
                list_new.add(entity)
            }
        }
        //list.remove(position);
        remindsLive.value = list_new
    }
}
