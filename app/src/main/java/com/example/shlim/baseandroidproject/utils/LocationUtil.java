package com.example.shlim.baseandroidproject.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;

public class LocationUtil {
    public static final String TAG = LocationUtil.class.getSimpleName();  // 디버그 태그

    /**
     *  주소로부터 위도, 경도를 리턴한다.
     * @param cxt

     * @return
     */
    public static String getLatLon(Context cxt, String address) {
        Geocoder geocoder = new Geocoder(cxt, Locale.getDefault());
        String latLon = "";
        try {
            List<Address> mResultLocation = geocoder.getFromLocationName(address, 1);
            double lat = mResultLocation.get(0).getLatitude();
            double lon = mResultLocation.get(0).getLongitude();
            latLon = lat + "," + lon;
        } catch (Exception e) {
            latLon = "";
        }
        return latLon;
    }

}
