package com.example.shlim.baseandroidproject.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.shlim.baseandroidproject.utils.LogUtil;


public class BaseConfig {
    private static final String TAG = BaseConfig.class.getSimpleName();   // 디버그 태그

    private static BaseConfig mInstance;                                  // 인스턴스
    private Context mContext;                                             // 컨피크
    private SharedPreferences mPreferences;                               // 사용자 살정

    public static BaseConfig getInstance(Context context) {
        LogUtil.d(TAG,"getInstance() mInstance:" + mInstance);
        if(null == mInstance) {
            mInstance = new BaseConfig(context);
        }
        return mInstance;
    }

    private BaseConfig(Context context){
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    /**
     * FCM Tocken을 저장한다.
     * @param token
     */
    public void setFCMToken(String token) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Define.PREFER_FCM_TOKEN , token);
        editor.commit();
    }

    /**
     * FCM Token을 가져온다.
     * @return
     */
    public String getFCMToken() {
        String token = mPreferences.getString(Define.PREFER_FCM_TOKEN, "");
        return token;
    }



}
