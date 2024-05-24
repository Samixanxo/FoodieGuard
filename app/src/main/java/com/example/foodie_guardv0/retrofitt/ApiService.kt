package com.example.foodie_guardv0.retrofitt
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.dataclass.ResponseErrors
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.dataclass.Review
import com.example.foodie_guardv0.dataclass.User
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("restaurant")
    fun getRestaurant(): Call<List<Restaurant>>

    @GET ("restaurant/name/{name}")
    fun getRestaurantByName(@Path("name") name: String):Call<List<Restaurant>>

    @GET ("restaurant/{id}")
    fun getRestaurantById(@Path("id") id: Int): Call<Restaurant>

    @GET ("reviews/{idRes}")
    fun getReviewsByIdRes(@Path("idRes") id: Int): Call<List<Review>>

    @GET ("reservation/{idRes}")
    fun getReservationsByIdRes(@Path("idRes") id: Int): Call<List<Reservation>>

    @GET ("reservationbyuser/{idUser}")
    fun getReservationsByIdUser(@Path("idUser") id: Int): Call<List<Reservation>>

    @GET("dishes")
    fun getDishes(): Call<List<Dish>>

    @GET("dishes/{idRed}")
    fun getDishesForRest(@Path("idRed") id: Int): Call<List<Dish>>

    @GET("dishes/filter/{allergens}")
    fun getDishesFiltered(@Path("allergens") name: String): Call<List<Dish>>

    @POST ("user")
    fun createUser(@Body user: User): Call<ResponseErrors>

    @POST("review")
    fun createReview(@Body newReview: Review): Call<Map<String, Any>>

    @POST ("viewToken")
    fun confirmUser(@Body body: Map<String, String>): Call<Boolean>

    @POST ("login")
    fun postUser(@Body email: Map<String,String>):Call<ActualUser>

    @PUT("user/password")
    fun changePassword (@Body body:Map<String,String>) :Call<Void>

    @PUT("updateUserInReservation/{reservationId}")
    fun updateUserInReservation(
        @Path("reservationId") reservationId: Int,
        @Query("newUserId") newUserId: Int
    ): Call<Void>

    @PUT("user/{id}/premium")
    fun changePremium(@Path("id")userId: Int): Call<Void>

    @DELETE("user/{id}")
    fun deleteUser (@Path("id") id: Int): Call<Void>

    @Multipart
    @POST("{userId}/uploadImage")
    fun updateImage(
        @Path("userId") userId: Int,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>

    @GET("/email")
    fun sendConfirmationEmail(
        @Query("recipient") recipient: String
    ): Call<Void>

    @GET("/confirmationEmail")
    fun sendConfirmationEmailToRestaurant(
        @Query("recipient") recipient: String
    ): Call<Void>

}




