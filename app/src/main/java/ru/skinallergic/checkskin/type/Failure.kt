package ru.skinallergic.checkskin.type

sealed class Failure:MyFailure()  {
    class NetworkConnectionError() : Failure(){
        override var message:String?="Network Connection Error"

    }
    data class ServerError(override var message:String?) : Failure()
    object AccessTokenExpired: Failure()
    object RefreshTokenExpired: Failure()
}
open class MyFailure{
    open var message:String?=null
}