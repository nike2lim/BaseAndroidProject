package com.example.shlim.baseandroidproject.Retrofit;

public interface RetrofitCallBack <T>{
    void onError(Throwable t);

    void onSuccess(int code, T receivedData);

    void onFailure(int code);

}
