package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter
import ru.skinallergic.checkskin.handlers.ToastyManager
import java.util.*
import javax.inject.Inject

class ReminderWriterViewModel @Inject constructor(val toastyManager: ToastyManager) : ViewModel() {
    var reminderWriter = ObservableField<ReminderWriter>(ReminderWriter(null,null,null,null,null))

    fun getEntity(): ReminderWriter? {
        if (reminderWriter.get()?.start_at == null ||
                reminderWriter.get()?.text == null ||
                reminderWriter.get()?.repeat_mode == null ||
                reminderWriter.get()?.kind == null ||
                reminderWriter.get()?.remind == null) {
            toastyManager.toastyyyy("Заполните все поля")
            return null
        } else {
            return reminderWriter.get()
        }
    }
    private fun get():ReminderWriter{
        return reminderWriter.get()?: ReminderWriter(null,null,null,null,null)
    }
    private fun set(entity: ReminderWriter){
        reminderWriter.set(entity)
    }

    fun setStartAt(start: Long) {
        val entity =get()
        entity.start_at=start
        set(entity)
    }

    fun setText(text: String) {
        val entity =get()
        entity.text=text
        set(entity)

    }
    fun setRepeatMode(repeatMode: Int) {
        val entity =get()
        entity.repeat_mode=repeatMode
        set(entity)

    }
    fun setKind(kind: Int) {
        val entity =get()
        entity.kind=kind
        set(entity)

    }
    fun setRemind(remind: Int) {
        val entity =get()
        entity.remind=remind
        set(entity)
    }
}
