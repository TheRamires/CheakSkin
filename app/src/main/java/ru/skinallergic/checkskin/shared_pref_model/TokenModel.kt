package ru.skinallergic.checkskin.shared_pref_model


interface TokenModel  {
    fun loadRefreshToken(): String?
    fun loadAccesToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun saveAccesToken(accesToken: String)
    fun deleteAccesToken()

}