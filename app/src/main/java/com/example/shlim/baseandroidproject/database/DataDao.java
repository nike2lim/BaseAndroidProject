package com.example.shlim.baseandroidproject.database;

import android.content.Context;
import android.database.Cursor;

import com.example.shlim.baseandroidproject.R;
import com.example.shlim.baseandroidproject.database.listener.DataOperationListener;
import com.example.shlim.baseandroidproject.database.listener.DataOperationListenerGetObject;
import com.example.shlim.baseandroidproject.item.ItemZoneInfo;
import com.example.shlim.baseandroidproject.utils.LogUtil;

import java.util.ArrayList;

public class DataDao {
    private static final String TAG = DataDao.class.getSimpleName();    // 디버그 태그

    private DBHelper db;
    private volatile static DataDao instance;
    private Context mContext;

    private DataDao(Context context) {
        mContext = context;
        try {
            db = DBHelper.getInstance(mContext);
        } catch (Exception e) {
        }
    }

    public static final DataDao getInstance(Context context) {
        if (instance == null) {
            synchronized (DataDao.class) {
                if (instance == null) {
                    instance = new DataDao(context);
                }
            }
        }
        return instance;
    }

    public void close() {
        db.close();
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public void insertZoneInfo(ItemZoneInfo zoneInfo, DataOperationListener dataOperationListener) {
        long result = db.insert("zone", zoneInfo.getContentValues());
        if (dataOperationListener != null)
            dataOperationListener.done();
    }

    public void getZoneInfoList(final DataOperationListenerGetObject dataOperationListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                ArrayList<ItemZoneInfo> dataList = new ArrayList<ItemZoneInfo>();
                try {
                    String query = mContext.getString(R.string.getZoneInfoList);
                    cursor = db.get(query);
                    cursor.moveToFirst();
                    int numRows = cursor.getCount();
                    ItemZoneInfo zoneInfo;
                    for (int i = 0; i < numRows; i++) {
                        zoneInfo = new ItemZoneInfo();

                        zoneInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        zoneInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        zoneInfo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        zoneInfo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        zoneInfo.setZoneOut(cursor.getInt(cursor.getColumnIndex("zoneOut")) > 0);

                        dataList.add(zoneInfo);
                        cursor.moveToNext();
                    }
                    db.logCursorInfo(cursor);
                    dataOperationListener.done(dataList);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    LogUtil.e(TAG, exception.getLocalizedMessage());
                    dataOperationListener.error(exception);
                } finally {
                    closeCursor(cursor);
                }
            }
        }).start();
    }
}
