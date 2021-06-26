package ru.skinallergic.checkskin.Api

import com.google.gson.Gson
import ru.skinallergic.checkskin.components.tests.viewModels.TestResultViewModel

object ServiceFactory {

    fun makeHomeService(isDebug: Boolean): HomeService {
        return ServiceConfig.makeService().create(HomeService::class.java)
    }

    fun makeTestService(isDebug: Boolean): TestResultService{
        return ServiceConfig.makeService().create(TestResultService::class.java)
    }

    fun makeHealthyService(isDebug: Boolean):HealthyService{
        return ServiceConfig.makeService().create(HealthyService::class.java)
    }
    fun makeTokenService (isDebug: Boolean): TokenService{
        return ServiceConfig.makeService().create(TokenService::class.java)
    }
}