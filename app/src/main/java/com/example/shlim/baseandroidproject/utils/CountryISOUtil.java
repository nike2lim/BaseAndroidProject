package com.example.shlim.baseandroidproject.utils;
import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class CountryISOUtil {
    public static final String TAG = CountryISOUtil.class.getSimpleName();  // 디버그 태그

    public static final String KEY_COUNTRY_ISO_KR = "kr";                                                       // 국가코드

    public static String getCountryCode(String countryISO) {
        String code = "0";
        switch(countryISO) {
            case KEY_COUNTRY_ISO_KR:
                code = "82";
                break;
            default:
                break;
        }
        return code;
    }

  /**
     * SIM ISOCode에 해당하는 code를 리턴한다.(한국의 경우 82)
     * @param context
     * @return
     */
    public static String getSIMCountryCode(Context context) {
        String countryISO = DeviceUtil.getSimCountryIso(context);
        if (TextUtils.isEmpty(countryISO)) return "82";
//        return CountryISOUtil.getCountryCode(DeviceUtil.getSimCountryIso(mContext));
        int code = CountryISOUtil.getCountryCodeForISOCode(DeviceUtil.getSimCountryIso(context));
        return String.valueOf(code);
    }


  /**
     * 국가 코드에 해당하는 국가 번호 코드를 리턴한다.(한국의 경우 82)
     * @param countryISO
     * @return
     */
    public static int getCountryCodeForISOCode(String countryISO) {
        int result = -1;
        try {
            Class c = Class.forName("com.android.i18n.phonenumbers.PhoneNumberUtil");
            Method getInstance = c.getDeclaredMethod("getInstance");
            Method getCountryCodeForRegion = c.getDeclaredMethod("getCountryCodeForRegion", String.class);

            Object instance = getInstance.invoke(null);
            countryISO = countryISO.toUpperCase();
            Integer code = (Integer) getCountryCodeForRegion.invoke(instance, countryISO);
            result = code;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

	 /**
     * ISO Code에 해당하는 국가이름을 리턴한다.(kr-> 한국 리턴)
     * @param countryISO
     * @return
     */
    public static String getCountryNameForISOCode(String countryISO) {
        if(TextUtils.isEmpty(countryISO))   return null;
        String countryName = "";

        String[] isoCodes = Locale.getISOCountries();
        for(int i=0; i < isoCodes.length; i++) {
            if(isoCodes[i].equalsIgnoreCase(countryISO)) {
                Locale locale = new Locale( "en", isoCodes[i] );
                countryName = locale.getDisplayCountry();
                break;
            }
        }
        return countryName;
    }
}
