package com.example.shlim.baseandroidproject.Retrofit;



import com.example.shlim.baseandroidproject.Retrofit.RequestBody.RequestPut;
import com.example.shlim.baseandroidproject.Retrofit.ResponseBody.ResponseGet;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroBaseApiService {
    final String Base_URL = "http://jsonplaceholder.typicode.com";

    @GET("/posts/{userId}")
    Call<ResponseGet> getFirst(@Path("userId") String id);

    @GET("/posts")
    Call<List<ResponseGet>> getSecond(@Query("userId") String id);

    @FormUrlEncoded
    @POST("/posts")
    Call<ResponseGet> postFirst(@FieldMap HashMap<String, Object> parameters);

    @PUT("/posts/1")
    Call<ResponseGet> putFirst(@Body RequestPut parameters);

    @FormUrlEncoded
    @PATCH("/posts/1")
    Call<ResponseGet> patchFirst(@Field("title") String title);

    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();
}
