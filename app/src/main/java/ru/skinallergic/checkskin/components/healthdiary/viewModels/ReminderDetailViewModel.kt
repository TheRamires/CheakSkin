package ru.skinallergic.checkskin.components.healthdiary.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter
import ru.skinallergic.checkskin.handlers.ToastyManager
import javax.inject.Inject

class ReminderDetailViewModel @Inject constructor(val toastyManager: ToastyManager) : ViewModel() {
    val reminderDetail = ObservableField<ReminderEntity>()

    fun setObservable(entity: ReminderWriter){
        val reminderEntity = ReminderEntity(
                start_at = entity.start_at,
                text = entity.text,
                kind = entity.kind,
                remind = entity.remind,
                repeat_mode = entity.repeat_mode
        )
        reminderDetail.set(reminderEntity)
    }

    private fun get():ReminderEntity{
        return reminderDetail.get()?: ReminderEntity(start_at = null,text = null,repeat_mode = null,kind=null,remind = null)
    }
    private fun set(entity: ReminderEntity){
        reminderDetail.set(entity)
    }

    fun setStartAt(start: Long) {
        val entity =get()
        entity.start_at=start/1000
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