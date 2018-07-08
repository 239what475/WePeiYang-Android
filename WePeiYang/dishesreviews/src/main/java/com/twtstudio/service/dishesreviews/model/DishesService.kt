package com.twtstudio.service.dishesreviews.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by User on 2018/5/2.
 */
interface DishesService {

    @GET("v1/food/home")
    fun getHomeInfo(): Deferred<CommonBody<DishesHomeBean>>

    @GET("v1/food/search")
    fun searchFood(@Query("foodName") food: String): Deferred<CommonBody<DishesSearchBean>>

    @GET("v1/food/canteen")
    fun getCanteenInfo(@Query("canteenId") id: Int): Deferred<CommonBody<DishesCanteenBean>>

    @GET("v1/food/food")
    fun getFood(@Query("foodId") foodId: Int): Deferred<CommonBody<DishesFoodBean>>

    @GET("v1/food/user/my_comment")
    fun getAccount(@Query("userId") userId: String): Deferred<CommonBody<DishesAccountBean>>

    @Multipart
    @POST("v1/food/comment")
    fun evaluate(@PartMap fields: @JvmSuppressWildcards Map<String, RequestBody>, @Part files: List<MultipartBody.Part>): Deferred<CommonBody<DishesEvaluateBean>>

    @FormUrlEncoded
    @POST("v1/food/food/create")
    fun addFood(@FieldMap fields: Map<String, String>): Deferred<CommonBody<DishesAddFoodBean>>

    @FormUrlEncoded
    @PUT("v1/food/food/praise")
    fun likeFood(@Field("foodId") foodId: Int): Deferred<CommonBody<DishesLikeBean>>

    //api有问题
    @FormUrlEncoded
    @PUT("/apiv1/food/food/collect")
    fun collectFood(@Field("foodId") foodId: Int, @Field("userId") userId: Int): Deferred<CommonBody<DishesCollectBean>>

    @FormUrlEncoded
    @PUT("/apiv1/food/food/collect/delete")
    fun deleteCollectedFood(@Field("foodId") foodId: Int, @Field("userId") userId: Int): Deferred<CommonBody<String>>
    @FormUrlEncoded
    @DELETE("v1/food/food/delete")
    fun deleteFood(@Field("foodId") foodId: Int): Deferred<CommonBody<String>>

    //分享api有问题
    companion object : DishesService by ServiceFactory()
}

