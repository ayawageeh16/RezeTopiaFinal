package io.krito.com.rezetopia.helper;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Mona Abdallh on 5/31/2018.
 */

public interface  RetrofitUploadService {
    @Multipart
    @POST("reze/user_post.php")
    Call<ResponseBody> uploadVideo(@Part("description") String description, @Part("video") RequestBody video);
}
