package Retrofit;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("sendEmail")
    Call<ResponseBody> sendEmailWithAttachment(
            @Part("to") RequestBody to,
            @Part("subject") RequestBody subject,
            @Part("message") RequestBody message,
            @Part MultipartBody.Part file
    );
}

