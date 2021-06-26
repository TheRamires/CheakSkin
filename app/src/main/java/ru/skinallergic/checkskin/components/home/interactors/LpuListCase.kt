package ru.skinallergic.checkskin.components.home.interactors

import io.reactivex.Observable
import ru.skinallergic.checkskin.components.home.repositories.LpuRepository
import ru.skinallergic.checkskin.type.Either
import ru.skinallergic.checkskin.type.Failure
import javax.inject.Inject

class LpuListCase @Inject constructor(
        val repo: LpuRepository
):UseCase<Any,Any>() {
    override fun run(params: Any): Either<Failure, Any> {
        return repo.getLpuList()
    }
}