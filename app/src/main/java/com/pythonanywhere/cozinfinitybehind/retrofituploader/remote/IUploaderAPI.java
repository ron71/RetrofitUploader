package com.pythonanywhere.cozinfinitybehind.retrofituploader.remote;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploaderAPI {

    @Multipart
    @POST("upload/upload.php")
    Call<String> uploadedFile(@Part MultipartBody.Part file);
}
