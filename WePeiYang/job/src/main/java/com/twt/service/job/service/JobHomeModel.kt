package com.twt.service.job.service

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

object JobHomeModel {

    fun getRecruits(type: Int, page: Int, callback: suspend (RefreshState<Unit>, List<HomeDataL>?, List<HomeDataL>?) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            JobService.getRecruits(type, page).awaitAndHandle {
                callback(RefreshState.Failure(it), null, null)
            }?.data?.let {
                when (type) {
                    // 把最大页数存下来，便于上拉刷新的时候判断
                    0 -> pagesOfMsg = it.page_count
                    1 -> pagesOfFair = it.page_count
                }
                callback(RefreshState.Success(Unit), it.important, it.common)
            }
        }
    }

    fun getNotioces(type: Int, page: Int, callback: suspend (RefreshState<Unit>, List<HomeDataR>?, List<HomeDataR>?, List<HomeDataR>?) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            JobService.getNotices(type, page).awaitAndHandle {
                callback(RefreshState.Failure(it), null, null, null)
            }?.data?.let {
                when (type) {
                    // 把最大页数存下来，便于上拉刷新的时候判断
                    0 -> pagesOfNotice = it.page_count
                    1 -> pagesOfDynamic = it.page_count
                }
                callback(RefreshState.Success(Unit), it.rotation, it.important, it.common)
            }
        }
    }
}
