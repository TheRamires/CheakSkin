package ru.skinallergic.checkskin.components.home.repositories

import ru.skinallergic.checkskin.Api.DocService
import ru.skinallergic.checkskin.Api.HomeService
import ru.skinallergic.checkskin.Request
import javax.inject.Inject

class DocumentRepository @Inject constructor(val request: Request, val homeService: HomeService,
                                             val docService: DocService
) {

}