package com.example.foodie_guardv0.retrofitt
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.dataclass.Review
import com.example.foodie_guardv0.dataclass.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url



interface ApiService {
    @GET("restaurant")
    fun getRestaurant(): Call<List<Restaurant>>

    @GET ("restaurant/name/{name}")
    fun getRestaurantByName(@Path("name") name: String):Call<List<Restaurant>>

    @GET ("restaurant/{id}")
    fun getRestaurantById(@Path("id") id: Int): Call<Restaurant>

    @GET ("reviews/{idRes}")
    fun getReviewsByIdRes(@Path("idRes") id: Int): Call<List<Review>>

    @GET("dishes")
    fun getDishes(): Call<List<Dish>>

    @GET("dishes/{idRed}")
    fun getDishesForRest(@Path("idRed") id: Int): Call<List<Dish>>

    @GET("dishes/filter/{allergens}")
    fun getDishesFiltered(@Path("allergens") name: String): Call<List<Dish>>

    @POST ("user")
    fun createUser(@Body user: User): Call<Void>

    @POST ("login")
    fun postUser(@Body email: Map<String,String>):Call<ActualUser>

    @PUT("user/password")
    fun changePassword (@Body body:Map<String,String>) :Call<Void>

    @GET("/email")
    fun sendConfirmationEmail(
        @Query("recipient") recipient: String
    ): Call<Void>
}




