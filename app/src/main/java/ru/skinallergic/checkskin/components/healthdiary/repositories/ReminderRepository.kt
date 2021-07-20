package ru.skinallergic.checkskin.components.healthdiary.repositories

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import ru.skinallergic.checkskin.Api.NotificationService
import ru.skinallergic.checkskin.Api.TokenService
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.handlers.NetworkHandler
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.shared_pref_model.TokenModelImpls
import ru.skinallergic.checkskin.utils.TOKENEXPIRED
import javax.inject.Inject

class ReminderRepository @Inject constructor(
        val service: NotificationService,
        tokenService: TokenService,
        tokenModel: TokenModelImpls,
        val networkHandler: NetworkHandler,
        val toastyManager: ToastyManager
): BaseHealthyRepository(tokenService, tokenModel,networkHandler,toastyManager) {

    override fun redact(date: String, data: WritingData, saved: MutableLiveData<Boolean>, progressBar: ObservableField<Boolean>) { }

    override fun getData(date: String, liveData: MutableLiveData<GettingData?>, splashScreenOn: ObservableField<Boolean>?) {}

    fun getRemind(date: String){
        networkHandler.check()
        val accesToken=tokenModel_.loadAccesToken()
        accesToken?.let {token->
            service.getReminder(date, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { getRemind(date) }
                        }}
                    .subscribe {
                        Loger.log("getRemind $it")
                    }
        }
    }
    fun newRemind(reminderWriter: ReminderWriter): Observable<ResponseBody> {
        networkHandler.check()
        val accesToken=tokenModel_.loadAccesToken()
        return accesToken?.let {token->
            service.newReminder(token,reminderWriter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { newRemind(reminderWriter) }
                        }}
        }!!

    }
    fun redactRemind(reminderWriter: ReminderWriter){
        networkHandler.check()
        val accesToken=tokenModel_.loadAccesToken()
        accesToken?.let {token->
            service.redactReminder(token,reminderWriter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { redactRemind(reminderWriter) }
                        }}
                    .subscribe {
                        Loger.log("redactRemind $it")
                    }
        }

    }
    fun offRemind(date: Long){
        val map = mapOf<String, Long>("timestamp" to date)
        networkHandler.check()
        val accesToken=tokenModel_.loadAccesToken()
        accesToken?.let {token->
            service.offReminder(token,map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { offRemind(date) }
                        }}
                    .subscribe {
                        Loger.log("offRemind $it")
                    }
        }

    }
    fun deleteRemind(date: String){
        networkHandler.check()
        val accesToken=tokenModel_.loadAccesToken()
        accesToken?.let {token->
            service.deleteReminder(date,token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Loger.log("OnError "+it)
                        if (it.message == TOKENEXPIRED) {
                            toastyManager.toastyyyy("Токен устарел, ошибка 401, идет обновление")
                            Loger.log("Токен устарел, ошибка 401, идет обновление")
                            refreshToken { deleteRemind(date) }
                        }}
                    .subscribe {
                        Loger.log("deleteRemind $it")
                    }
        }
    }
}