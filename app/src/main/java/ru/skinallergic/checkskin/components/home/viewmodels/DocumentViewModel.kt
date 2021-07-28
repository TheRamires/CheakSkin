package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.MutableLiveData
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel
import ru.skinallergic.checkskin.components.home.data.DocEntity
import ru.skinallergic.checkskin.components.home.repositories.DocumentRepository
import javax.inject.Inject

class DocumentViewModel @Inject constructor(val repo: DocumentRepository):  BaseViewModel() {
    override var baseRepository: BaseHealthyRepository = repo

    init {
        baseRepository.compositeDisposable = this.compositeDisposable
        baseRepository.expiredRefreshToken = this.expiredRefreshToken
    }

    val docs = MutableLiveData<List<DocEntity>>()

    fun requestDocs() : MutableLiveData<List<DocEntity>>{
        compositeDisposable.add(
                repo.getDocs()
                        ?.doOnSubscribe { progressBar.set(true) }
                        ?.doOnComplete { progressBar.set(false) }
                        ?.subscribe {
                            docs.value=it
                        }
        )
        return docs
    }
}