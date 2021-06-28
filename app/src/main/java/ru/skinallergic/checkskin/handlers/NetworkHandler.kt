package ru.skinallergic.checkskin.handlers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import javax.inject.Inject


class NetworkHandler @Inject constructor(val context: Context, val toastyManager: ToastyManager, val handleOnce: HandleOnce) {

    val isConnected get() = context.networkInfo?.isConnected

    fun check(): Boolean {
        return when (isConnected) {
            true -> {
                handleOnce.clear()
                true
            }
            false, null -> {
                val message=context.getString(R.string.error_network)
                Loger.log(message)
                if (handleOnce.itWasNotHandled(message)){
                    toastyManager.toastyyyy(message)
                }
                false
            }
        }
    }
}

val Context.networkInfo: NetworkInfo? get() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
