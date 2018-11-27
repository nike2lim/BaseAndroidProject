package com.example.shlim.baseandroidproject.item;

import android.content.ContentValues;

public class ItemZoneInfo {
    int _id;
    String name;
    String address;
    String latitude;
    String longitude;
    boolean zoneOut;

    public void setId(int id) {
        this._id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setZoneOut(boolean zoneOut) {
        this.zoneOut = zoneOut;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean getZoneOut() {
        return zoneOut;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
//        contentValues.put("_id", getId());                    // primary key auto increase
        contentValues.put("name", getName());
        contentValues.put("address", getAddress());
        contentValues.put("latitude", getLatitude());
        contentValues.put("longitude", getLongitude());
        contentValues.put("zoneOut", getZoneOut());
        return contentValues;
    }
}
