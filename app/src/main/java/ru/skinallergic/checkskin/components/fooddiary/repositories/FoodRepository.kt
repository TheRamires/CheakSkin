package ru.skinallergic.checkskin.components.fooddiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.FoodService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWr
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.healthdiary.repositories.BaseHealthyRepository
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

class FoodRepository @Inject constructor(
        val service: FoodService,
        tokenService: TokenService,
        tokenModel: TokenModelImpls,
        val networkHandler: NetworkHandler,
        val toastyManager: ToastyManager
): BaseHealthyRepository(tokenService, tokenModel,networkHandler,toastyManager) {

    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>, progressBar: ObservableField<Boolean>) {}

    override fun getData(date: String, liveData: MutableLiveData<GettingData?>, splashScreenOn: ObservableField<Boolean>?) {}

    fun getAllergens(page: Int) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->
            service.getAllergens(page.toString(), token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getAllergens(page) }
                        }
                    }
                    .subscribe { Loger.log("getAllergens ${it.string()}") }

        }
    }

    fun addAllergens(name: String) {
        val NAME = "name"
        val map = mapOf(NAME to name)
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->
            service.addAllergens(token, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { addAllergens(name) }
                        }
                    }
                    .subscribe { Loger.log("addAllergens ${it.string()}") }
        }
    }

    fun deleteAllergens(id: Int) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->
            service.deleteAllergens(id, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { deleteAllergens(id) }
                        }
                    }
                    .subscribe { Loger.log("deleteAllergens ${it.string()}") }
        }
    }

    fun getFoodDiaryByDate(date: String) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->{
                service.getFoodDiaryByDate(date, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError {
                            Loger.log("OnError " + it)
                            if (it.message == TOKENEXPIRED) {
                                toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                                Loger.log("Токен устарел, ошибка 401, идет обновление")
                                refreshToken { getFoodDiaryByDate(date) }
                            }
                        }
                        .subscribe { Loger.log("getFoodDiaryByDate ${it.string()}") }
            }
        }
    }
    fun getFoodDiarySearch(search: String) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->{
                service.getFoodDiarySearch(search, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError {
                            Loger.log("OnError " + it)
                            if (it.message == TOKENEXPIRED) {
                                toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                                Loger.log("Токен устарел, ошибка 401, идет обновление")
                                refreshToken { getFoodDiarySearch(search) }
                            }
                        }
                        .subscribe { Loger.log("getFoodDiarySearch ${it.string()}") }
            }
        }
    }
    fun getFoodDiarySearchByDate(date: String, search: String) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->{
            service.getFoodDiarySearchByDate(date, search, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getFoodDiarySearchByDate(date, search) }
                        }
                    }
                    .subscribe { Loger.log("getFoodDiarySearchByDate ${it.string()}") }
        }}
    }
    fun getFoodDiaryAll() {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->{
            service.getFoodDiaryAll(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getFoodDiaryAll() }
                        }
                    }
                    .subscribe { Loger.log("getFoodDiaryAll ${it.string()}") }
        }}
    }
    fun addMeal(bodyWrite: FoodWriter) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token ->{
            service.addMeal(token,bodyWrite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { addMeal(bodyWrite) }
                        }
                    }
                    .subscribe { Loger.log("addMeal ${it.string()}") }
        }}
    }
    fun redactMeal(id: Int, bodyWrite: FoodWriter) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token->
            service.redactMeal(id,token,bodyWrite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { redactMeal(id, bodyWrite) }
                        }
                    }
                    .subscribe { Loger.log("redactMeal ${it.string()}") }
        }
    }
    fun deleteMeal(id: Int) {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        accesToken?.let { token->
            service.deleteMeal(id,token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError " + it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { deleteMeal(id) }
                        }
                    }
                    .subscribe { Loger.log("deleteMeal ${it.string()}") }
        }
    }
}