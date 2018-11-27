package com.example.shlim.baseandroidproject.fcm;


import android.content.Context;

import com.example.shlim.baseandroidproject.BaseApplication;
import com.example.shlim.baseandroidproject.utils.DeviceUtil;
import com.example.shlim.baseandroidproject.utils.LogUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class BaseFCMIDService extends FirebaseInstanceIdService {
    private static final String TAG = BaseFCMIDService.class.getSimpleName(); // 디버그 태그

    private String registrationID;                                              // 인증 id
    private BaseApplication mApp;                                               // 전역 (Application) 변수
    private Context mContext;                                                   // 콘텍스트

    @Override
    public void onTokenRefresh() {
        // 콘텍스트
        mContext = getApplicationContext();

        // 전역 (Application) 변수
        if (mApp == null) mApp = (BaseApplication) mContext;

        // 인증 id
        registrationID = FirebaseInstanceId.getInstance().getToken();
        LogUtil.d(TAG,"onTokenRefresh() -> registrationID: " + registrationID);

        //registraiton id 저장
        mApp.getConfig().setFCMToken(registrationID);

        // 단말기 전화번호
        String devicePhoneNumber = DeviceUtil.getDevicePhoneNumber(getApplicationContext());
        LogUtil.d(TAG,"onTokenRefresh() -> devicePhoneNumber : " + devicePhoneNumber);

        //TODO  단말기 번호를 통해 서버에 registrationID 등록

        super.onTokenRefresh();
    }
}
