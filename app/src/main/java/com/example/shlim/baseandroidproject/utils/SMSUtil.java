package com.example.shlim.baseandroidproject.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by innochal on 2017. 6. 30..
 */

public class SMSUtil {
    public static final String TAG = SMSUtil.class.getSimpleName();  // 디버그 태그

    /**
     * SMS 전송
     *
     * @param toNumber
     * @param msg
     */
    public static boolean sendMMS(Context cxt, String toNumber, String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(toNumber)) {
            return false;
        }
        SmsManager smsManager = SmsManager.getDefault();



        //TODO CountryISOUtiㅣ을 사용해서 App에서 설정해서 가지고 있고,  설정된 정보를 읽어서 해당하는 국가 번호로 전달 필요함, 기본으로 82(한국)으로 함
//        String code = hashMap.get(Define.KEY_COUNTRY_CODE);
        String code = "82";

        if(toNumber.startsWith("0")) {
            toNumber = toNumber.substring(1);
        }
        toNumber = "+" + code + toNumber;

        ArrayList<String> parts = smsManager.divideMessage(msg);
        smsManager.sendMultipartTextMessage(toNumber, null, parts, null, null);

        return true;
    }
}
