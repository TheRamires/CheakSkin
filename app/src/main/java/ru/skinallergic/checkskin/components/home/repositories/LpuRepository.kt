package ru.skinallergic.checkskin.components.home.repositories

import ru.skinallergic.checkskin.Api.HomeService
import ru.skinallergic.checkskin.Request
import ru.skinallergic.checkskin.components.home.data.LPU
import ru.skinallergic.checkskin.components.news.pojo.NewsEntity
import ru.skinallergic.checkskin.type.Either
import ru.skinallergic.checkskin.type.Failure
import java.util.*
import javax.inject.Inject

class LpuRepository  @Inject constructor(val request: Request, val service: HomeService) {

    fun getLpuList (): Either<Failure, Any> {
        return request.make(service.getLpuList())
    }
}