package com.example.shlim.baseandroidproject.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import org.jsoup.Jsoup;

public class VersionChecker implements Comparable<VersionChecker> {
    private String version;
    private Context mContext;

    public VersionChecker(Context context) {
        mContext = context;
    }

    public final String get() {
        return this.version;
    }

    public String getMarketVersion(String packageName) {
        String newVersion;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return newVersion;
    }

    @Override
    public int compareTo(@NonNull VersionChecker that) {
        if(that == null)
            return 1;
        String[] thisParts = this.get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for(int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if(thisPart < thatPart)
                return -1;
            if(thisPart > thatPart)
                return 1;
        }
        return 0;
    }

    @Override public boolean equals(Object that) {
        if(this == that)
            return true;
        if(that == null)
            return false;
        if(this.getClass() != that.getClass())
            return false;
        return this.compareTo((VersionChecker) that) == 0;
    }

    class StoreVersionTask extends AsyncTask<Void, Void, String> {
        VersionChecker mAppVersionChecker;
        @Override
        protected String doInBackground(Void... params) {
            mAppVersionChecker = new VersionChecker(mContext);
            String playStoreAppVersion = mAppVersionChecker.getMarketVersion(mContext.getPackageName());
            return playStoreAppVersion;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String app_ver = null;
            try {
                app_ver = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mAppVersionChecker.version = app_ver;

            if (result == null)
                result = app_ver;
            final VersionChecker storeVersion = new VersionChecker(mContext);
            storeVersion.version = result;

//            runOnUiThread(new Runnable() {
//                public void run() {
                    if (mAppVersionChecker.compareTo(storeVersion) == -1) {

//                        needStoreUpdate = true;
//                        AppVersionNewImage.setVisibility(View.VISIBLE);
                    }
//                }
//            });
        }

    }
}
