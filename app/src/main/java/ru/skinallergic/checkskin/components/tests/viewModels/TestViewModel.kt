package ru.skinallergic.checkskin.components.tests.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.tests.data.EntityTest
import ru.skinallergic.checkskin.components.tests.data.EntityTestsHistory
import ru.skinallergic.checkskin.components.tests.data.Repository
import java.util.*
import javax.inject.Inject

class TestsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val testsLive = MutableLiveData<List<EntityTest>>()
    val historyLive = MutableLiveData<List<EntityTestsHistory>>()
    val questionId = MutableLiveData<Int>()
    val entityTestsObservable = ObservableField<EntityTest>()


    fun getDate() {
        //for example--------------------------------
        testsLive.value = repository.getTests()
    }

    fun getHistory() {
        val historyList: MutableList<EntityTestsHistory> = ArrayList()
        for (i in 0..4) {
            historyList.add(EntityTestsHistory(i, "Скрининговый опросник PEST $i", "15.05.2021", "Практический опыт показывает, что дальнейшее развитие различных форм деятельности обеспечивает актуальность соответствующих условий активизации! Задача организации, в особенности же постоянное информационно-техническое обеспечение нашей деятельности в значительной сте...",
                    "5 баллов"))
        }
        historyLive.value = historyList
    }
}