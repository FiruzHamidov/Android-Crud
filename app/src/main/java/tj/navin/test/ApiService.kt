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
        GET Post
    */

    @GET("/public/v2/posts")
    suspend fun getPosts(): Response<ResponseBody>

    // GET One
    @GET("/public/v2/users/29856")
    suspend fun getOneUser(): Response<ResponseBody>



    // Get post with ID
    @GET("/public/v2/posts/{Id}")
    suspend fun getWithId(@Path("Id") employeeId: String): Response<ResponseBody>

    /*
       PUT METHOD
    */
    @Headers("Authorization: Bearer f74dfae196c0543536a7638b5da0e623ce80a351cada7f80ae0611c8b7fe6cca")
    @PUT("/public/v2/users/29856")
    suspend fun updateUser(@Body requestBody: RequestBody): Response<ResponseBody>




}
