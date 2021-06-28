package ru.skinallergic.checkskin.components.tests.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.tests.data.Repository
import ru.skinallergic.checkskin.components.tests.data.TestResult
import ru.skinallergic.checkskin.handlers.ToastyManager
import java.util.*
import javax.inject.Inject

class TestResultViewModel @Inject constructor(
        private val repository: Repository,
        val toastyManager: ToastyManager) : ViewModel() {

    val resultsLive = MutableLiveData<List<TestResult>?>()
    var goToVerificationNumber : MutableLiveData<Boolean>

    init {
        goToVerificationNumber = MutableLiveData<Boolean>()
        repository.expiredRefreshToken=goToVerificationNumber
    }

    val compositeDisposable = CompositeDisposable()

    fun sendResult(test: String?, result: String?) {
        Loger.log("send result $result")
        repository.sendResult(test!!, result!!)
    }

    fun getResult(test: String?) {
        Loger.log("getResult")
        repository.getResult(test!!, resultsLive)
    }

    override fun onCleared() {
        super.onCleared()
        repository.compositeDisposable.dispose()
        compositeDisposable.dispose()
    }

    fun clearResults(){
        resultsLive.value=null
    }
    fun giveMeFive(all: List<TestResult>):MutableList<TestResult>?{
        Loger.log("give me five")
        val fiveResults: MutableList<TestResult> = ArrayList()
        val temp = all
        if (temp.isEmpty()){
            return null
        }
        Collections.sort(temp, kotlin.Comparator { o1, o2 ->o2.created!!.compareTo(o1.created!!)  })
        /*val start: Int = temp.size - 1
        val end = start - 4*/
        val start: Int =0
        val end: Int =4
        Loger.log("giveMeFive $temp")

        for (i in start..end) {
            if (i==temp.size){break}
            if (i>=0){
                val temp: TestResult = temp[i]
                Loger.log("giveMeFive $temp")
                fiveResults.add(temp)
            }
        }

        fiveResults.reverse()
        return fiveResults
    }
}
