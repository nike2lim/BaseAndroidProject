package com.example.shlim.baseandroidproject.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class DeviceUtil {
    public static final String TAG = DeviceUtil.class.getSimpleName();  // 디버그 태그

    /**
     * 단말기 휴대전화번호
     *
     * @param context
     * @return
     */
    public static String getDevicePhoneNumber(Context context) {
        String phoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            phoneNumber = telephonyManager.getLine1Number();

            if (!TextUtils.isEmpty(phoneNumber)) {
                if (phoneNumber.startsWith("+82")) {
                    phoneNumber = "0" + phoneNumber.substring(3);
                }
            }
        }catch (SecurityException e) {
            LogUtil.d(TAG, e.getMessage());
        }catch (Exception e) {
            LogUtil.d(TAG, e.getMessage());
        }
        return phoneNumber;
    }

    /**
     * 국가코드
     * @param context
     * @return
     */
    public static String getSimCountryIso(Context context) {
        String simCountryIso;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        simCountryIso = telephonyManager.getSimCountryIso();
        LogUtil.d(TAG, "getSimCountryIso() -> simCountryIso : " + simCountryIso);
        return simCountryIso;
    }

    /**
     * 최상위 엑티비티명
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        return taskInfo.get(0).topActivity.getShortClassName();
    }

    /**
     * 스크린 잠금상태 확인
     * @return
     */
    public static boolean isLockscreen(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 계정정보
     * @param context
     * @return
     */
    public static ArrayList<String> getAccounts(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccounts();
        final int count = accounts.length;
        Account account = null;
        ArrayList<String> array = new ArrayList();
        for (int i = 0; i < count; i++) {
            account = accounts[i];
            LogUtil.d(TAG, "getAccounts() -> name : " + account.name + ", type : " + account.type);
            if (account.type.equals("com.google")) {
                array.add(account.name);
            }
        }
        return array;
    }


    /**
     * Ringer Mode를 리턴한다.
     * RINGER_MODE_VIBRATE, RINGER_MODE_NORMAL, RINGER_MODE_SILENT
     * @param cxt
     * @return
     */
    public static int getAudioRingerMode(Context cxt) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    /**
     * Ringer Mode를 설정한다.
     * @param cxt
     * @param mode
     */
    public static void setAudioRingerMode(Context cxt, int mode) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mode);
    }


    /**
     * STREAM_MUSIC Volume을 리턴한다.
     * @param cxt
     * @return
     */
    public static int getStremMusicVolume(Context cxt) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * STREAM_MUSIC Volume을 셋팅한다.
     * @param cxt
     */
    public static void setStremMusicVolume(Context cxt, int volume) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }


    /**
     * STREAM_MUSIC Volume을 적당하게 셋팅한다.
     * @param cxt
     */
    public static void setNormalStremMusicVolume(Context cxt) {
        AudioManager audioManager = (AudioManager) cxt.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) *0.75),
                AudioManager.FLAG_PLAY_SOUND);
    }

    /**
     * 네트워크 로밍 확인
     * @param cxt
     * @return
     */
    public static boolean isNetworkRoaming(Context cxt) {
        boolean isRoaming = false;
        TelephonyManager tel = (TelephonyManager)cxt.getSystemService(cxt.TELEPHONY_SERVICE);
        isRoaming = tel.isNetworkRoaming(); // 리턴값이 1 인 경우 Roaming 된 상태이며, 0이면 Roaming 되지 않은 상태입니다.
        return isRoaming;
    }

    /**
     * 로밍확인
     * @return
     */
    public static boolean isRoaming() {
        SmsManager smsManager = SmsManager.getDefault();
        ServiceState serviceState = new ServiceState();
        boolean isRoaming = serviceState.getRoaming();
        return isRoaming;
    }

}
