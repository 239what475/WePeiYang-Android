package com.twt.service.ecard.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.cache.hawk
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * 感觉设计的不是很好...
 */
data class ECardFullInfo(val personInfo: ECardPersonInfo, val transactionInfoList: List<TransactionInfo>, val todayCost: Float, val cache: Boolean)

val ecardCacheKey = "ECARD_FULL_INFO_CACHE"
val ecardFullInfoCache = Cache.hawk<ECardFullInfo>(ecardCacheKey)

object LiveEcardManager {

    private val eCardFullInfoLiveData = object : MutableLiveData<RefreshState<ECardFullInfo>>() {
        override fun onActive() {
            super.onActive()
            refreshEcardFullInfo()
        }
    }


    fun getEcardLiveData(): LiveData<RefreshState<ECardFullInfo>> = eCardFullInfoLiveData

    init {
        launch(UI) {
            val cache = ecardFullInfoCache.get().await()
            cache?.let { eCardFullInfoLiveData.postValue(RefreshState.Success(it.copy(cache = true))) } // 第一次Load Cache
        }
    }

    fun refreshEcardFullInfo(forceReload: Boolean = true) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            eCardFullInfoLiveData.postValue(RefreshState.Failure(throwable))
        }

        launch(UI + coroutineExceptionHandler) {

            eCardFullInfoLiveData.postValue(RefreshState.Refreshing())

            if (!forceReload) {
                ecardFullInfoCache.get().await()?.let { eCardFullInfoLiveData.postValue(RefreshState.Success(it)) } // Load Cache
            }

            val profileDeferred = EcardService.getEcardProfile()
            val historyDeferred = EcardService.getEcardTransaction()

            val profile = profileDeferred.await().data
                    ?: throw IllegalStateException("校园卡状态数据为空 联系开发者解决")
            val history = historyDeferred.await().data
                    ?: throw IllegalStateException("校园卡历史数据为空 联系开发者解决")

            val eCardFullInfo = ECardFullInfo(personInfo = profile.castToECardPersonInfo(), transactionInfoList = history.transaction, todayCost = profile.amount.toFloat(), cache = false)
            ecardFullInfoCache.set(eCardFullInfo)
            eCardFullInfoLiveData.postValue(RefreshState.Success(eCardFullInfo))
        }
    }

}