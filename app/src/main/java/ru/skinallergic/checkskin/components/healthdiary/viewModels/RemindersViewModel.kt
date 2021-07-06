package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RemindersViewModel @Inject constructor(): ViewModel() {
    val timeLive = MutableLiveData<Date>()

    var remindsLive = MutableLiveData<ArrayList<EntityReminders>>()
    var entity = ObservableField<EntityReminders>()
    fun clearEntityObservable(){entity.set(null)}
    var dateObservable = ObservableField<String>()

    val creatingReminder=MutableLiveData(EntityReminders())
    fun clearCurrent(){creatingReminder.value=EntityReminders()}

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
            list.add(EntityReminders(i, "Парацетамол$i", "выпить$i", Date(10800),
                    1, 1))
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
