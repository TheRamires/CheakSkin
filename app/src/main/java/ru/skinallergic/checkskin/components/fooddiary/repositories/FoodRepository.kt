package ru.skinallergic.checkskin.components.fooddiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import ru.skinallergic.checkskin.Api.FoodService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWr
import ru.skinallergic.checkskin.components.fooddiary.data.FoodWriter
import ru.skinallergic.checkskin.components.healthdiary.remote.BaseResponse
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

    fun getAllergens(page: Int):  Observable<BaseResponse<List<AllergicEntity>>> {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token ->
            service.getAllergens(page, token)
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

        }!!
    }

    fun addAllergens(name: String): Observable<String>? {
        val NAME = "name"
        val map = mapOf(NAME to name)
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token ->
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
                    .map { it.message!! }
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

    fun getFoodDiaryByDate(date: String): Observable<List<FoodEntity?>>? {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token ->
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
                    .map {
                        it.data!! }
        }!!
    }

    fun getFoodDiarySearch(search: String): Observable<List<FoodEntity?>>?  {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token ->
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
                        .map { it.data!! }

        }!!
    }

    fun getFoodDiarySearchByDate(date: String, search: String): Observable<List<FoodEntity?>>?  {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token ->
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
                    .map { it.data!! }
        }!!
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
    fun addMeal(bodyWrite: FoodWriter):Observable<String>{
        Loger.log("addMeal by Repo 1")
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        Loger.log("addMeal by Repo 2 $accesToken")
        return accesToken?.let {
            Loger.log("addMeal by Repo 3")
            service.addMeal(it,bodyWrite)
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
                    .map { it.message!! }

        }!!
    }
    fun redactMeal(id: Int, bodyWrite: FoodWriter):Observable<String> {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token->
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
                    .map { it.message!! }
        }!!
    }
    fun deleteMeal(id: Int):Observable<String> {
        networkHandler.check()
        val accesToken = tokenModel_.loadAccesToken()
        return accesToken?.let { token->
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
                    .map { it.message!! }
        }!!
    }
}