package com.example.shlim.baseandroidproject.item;

/**
 * Created by innochal on 2017-11-27.
 */

public class ItemCountryISO {
    String displayName;
    String isoCode;
    int countryRegionCode;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public void setCountryRegionCode(int countryRegionCode) {
        this.countryRegionCode = countryRegionCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public int getCountryRegionCode() {
        return countryRegionCode;
    }
}
