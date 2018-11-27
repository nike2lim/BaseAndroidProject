package com.example.shlim.baseandroidproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;


import com.example.shlim.baseandroidproject.BaseApplication;
import com.example.shlim.baseandroidproject.R;
import com.example.shlim.baseandroidproject.Retrofit.ResponseBody.ResponseGet;
import com.example.shlim.baseandroidproject.Retrofit.RetrofitCallBack;
import com.example.shlim.baseandroidproject.Retrofit.RetrofitClient;
import com.example.shlim.baseandroidproject.common.RequestCode;
import com.example.shlim.baseandroidproject.database.DBHelper;
import com.example.shlim.baseandroidproject.database.DataDao;
import com.example.shlim.baseandroidproject.database.listener.DataOperationListener;
import com.example.shlim.baseandroidproject.database.listener.DataOperationListenerGetObject;
import com.example.shlim.baseandroidproject.item.ItemZoneInfo;
import com.example.shlim.baseandroidproject.utils.LocationUtil;
import com.example.shlim.baseandroidproject.utils.LogUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();   // 디버그 태그

    private Context mContext;
    private BaseApplication mApp;
    public RetrofitClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mApp = (BaseApplication)mContext;

        if(mApp.checkPermissionAll(this)) {
            addAnimationOperations();
        }

        retroClient = RetrofitClient.getInstance(this).createBaseApi();

    }

    boolean set = false;
    private void addAnimationOperations() {

        final ConstraintSet constraint = new ConstraintSet();

        final ConstraintLayout rootLayout = findViewById(R.id.root);
        constraint.clone(rootLayout);

        final ConstraintSet constraint2 = new ConstraintSet();
        constraint2.clone(this, R.layout.activity_main_alt);

        findViewById(R.id.animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(rootLayout);

                    ConstraintSet cons;
                    if(set) {
                        cons = constraint;
                    }else {
                        cons = constraint2;
                    }
                    cons.applyTo(rootLayout);
                    set = !set;
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCode.PERMISSION_REQUEST_ALL) {
            boolean complete = true;
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    complete = false;
                    break;
                }
            }

            if (complete) {
                addAnimationOperations();
            } else {
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.db_test:
                dbTest();
                break;
            case R.id.retrofit_test:
                retrofitTest();
                break;
        }
    }

    private void dbTest() {
        // TODO DB 테스트
        final DataDao dao = DataDao.getInstance(mContext);
        ItemZoneInfo info = new ItemZoneInfo();
        info.setName("이노첼");
        info.setAddress("서울 서초구 동산로 28");
        info.setLatitude("37.470144");
        info.setLongitude("127.042394");
        info.setZoneOut(true);
        dao.insertZoneInfo(info, new DataOperationListener() {
            @Override
            public void done() {
                dao.getZoneInfoList(new DataOperationListenerGetObject() {
                    @Override
                    public void done(Object object) {
                        ArrayList<ItemZoneInfo> list;
                        list = (ArrayList<ItemZoneInfo>) object;
                        if (null != list) {
                            for (ItemZoneInfo info : list) {
                                LogUtil.d(TAG, info.toString());

                                String latlong = LocationUtil.getLatLon(mContext, info.getAddress());
                                LogUtil.d(TAG, "------------ latlong ------------- = " + latlong);

                                //DB 출력 테스트
                                try {
                                    DBHelper.getInstance(mContext).exportDB();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                        double lat  = Double.parseDouble(info.getLatitude());
//                                        double lon = Double.parseDouble(info.getLongitude());
//                                        String add = LocationUtil.getAddress(mContext, lat, lon);
//
//                                        mApp.onAlertDialog("주소는 " +  add, Gravity.CENTER);
                            }
                        }

                    }

                    @Override
                    public void error(Exception exception) {

                    }
                });
            }

            @Override
            public void error(Exception exception) {

            }
        });
    }

    private void retrofitTest() {
        retroClient.getFirst("1", new RetrofitCallBack() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ResponseGet data = (ResponseGet) receivedData;
                String codeStr = String.valueOf(code);
                String id = String.valueOf(data.id);
                String userId = String.valueOf(data.userId);

                LogUtil.d(TAG, "codeStr = " + codeStr + ", id = " + id + ", userId = " + userId );
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
