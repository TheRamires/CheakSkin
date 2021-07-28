package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import ru.skinallergic.checkskin.components.home.data.LpuEntity
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity
import ru.skinallergic.checkskin.components.home.data.ReviewEntity
import ru.skinallergic.checkskin.components.home.data.ReviewWriter
import ru.skinallergic.checkskin.components.home.repositories.LpuRepository
import ru.skinallergic.checkskin.components.home.repositories.ReviewsRepository
import ru.skinallergic.checkskin.handlers.ToastyManager
import javax.inject.Inject

class ReviesViewModel @Inject constructor(val repo: LpuRepository, val toastyManager: ToastyManager): BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repo

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }

    val reviewList = MutableLiveData<List<ReviewEntity>>()
    val sent = MutableLiveData<Boolean>()

    var vote: Int?=null
    var description: String?=null

    fun saveCondition(): Boolean{
        if (vote!=null && description!=null){
            return true
        } else {
            toastyManager.toastyyyy("Поставьте оценку и оставьте комментарии")
            return false
        }
    }

    fun getReviews(id: Int):MutableLiveData<List<ReviewEntity>>{

        Loger.log("-----------------getReviews id "+id);
        compositeDisposable.add(
                repo.getReviews(id)
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe({
                                    reviewList.value=it
                            Loger.log("----------------- repo.getReviews list  "+it);

                        },{})
        )
        return reviewList
    }
    fun addReview(id: Int, text: String, vote: Int){
        val reviewWriter= ReviewWriter(text,vote)
        compositeDisposable.add(
                repo.addReviews(id,reviewWriter)
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe({
                            Loger.log(it)
                            if (it=="Ok"){
                                sent.value=true
                            }
                        },{})
        )
    }
}