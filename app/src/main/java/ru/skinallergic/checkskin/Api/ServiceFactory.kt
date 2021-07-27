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
    fun makeNotificationService(isDebug: Boolean): NotificationService{
        return ServiceConfig.makeService().create(NotificationService::class.java)
    }
    fun makeFoodService(isDebug: Boolean): FoodService{
        return ServiceConfig.makeService().create(FoodService::class.java)
    }

    fun makeDocService(isDebug: Boolean): DocService{
        return ServiceConfig.makeService().create(DocService::class.java)
    }
    fun makeLpuService(isDebug: Boolean): LpuService{
        return ServiceConfig.makeService().create(LpuService::class.java)
    }
}