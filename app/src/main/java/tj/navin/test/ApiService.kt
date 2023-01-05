package tj.navin.test

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /*
      Create Post
   */

    // Add JSON
    @Headers("Authorization: Bearer f74dfae196c0543536a7638b5da0e623ce80a351cada7f80ae0611c8b7fe6cca")
    @POST("/public/v2/users")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    /*
        GET Posts
    */

    @GET("/public/v2/posts")
    suspend fun getPosts(): Response<ResponseBody>

    // Get Users
    @Headers("Authorization: Bearer f74dfae196c0543536a7638b5da0e623ce80a351cada7f80ae0611c8b7fe6cca")
    @GET("/public/v2/users")
    suspend fun getUsers(): Response<ResponseBody>

    // GET One
    @Headers("Authorization: Bearer f74dfae196c0543536a7638b5da0e623ce80a351cada7f80ae0611c8b7fe6cca")
    @GET("/public/v2/users/{Id}")
    suspend fun getOneUser(@Path("Id") getById: String): Response<ResponseBody>

    /*
       PUT METHOD
    */
    @Headers("Authorization: Bearer f74dfae196c0543536a7638b5da0e623ce80a351cada7f80ae0611c8b7fe6cca")
    @PUT("/public/v2/users/{Id}")
    suspend fun updateUser(@Body requestBody: RequestBody): Response<ResponseBody>




}
