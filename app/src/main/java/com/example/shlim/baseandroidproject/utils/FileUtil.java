package com.example.shlim.baseandroidproject.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dongnam on 2017. 5. 20..
 */

public class FileUtil {
    public static final String TAG = FileUtil.class.getSimpleName();    // 디버그 태그
    // TODO 상용 (로그파일 사용 유무)
//    private static boolean isEnable = true;                             // 로그사용 플래그 (사용)
    private static boolean isEnable = false;                            // 로그사용 플래그 (미사용)

    /**
     * 사이렌 리스트
     *
     * @param context
     * @return
     */
    public static String[] getSirenList(Context context) {
        LogUtil.i(TAG, "getSirenList() -> Start !!!");
        String[] list = null;
        try {
            AssetManager assetManager = context.getAssets();
            list = assetManager.list("siren");
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return list;
    }

    /**
     * 파일경로로 비트맵 가져오기
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getBitmapFromImagePath(String imagePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return bitmap;
    }

    /**
     * 이미지 기울기
     *
     * @param filepath
     * @return
     */
    public synchronized static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 비트맵 회전
     *
     * @param bitmap
     * @param degrees
     * @return
     */
    public synchronized static Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return bitmap;
    }

    /**
     * 파일 저장 경로
     *
     * @param context
     * @return
     */
    public static String getStoragePath(Context context) {
        String exist = Environment.getExternalStorageState();
        if (exist.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            return context.getCacheDir().getAbsolutePath() + "/";
        }
    }

    /**
     * 디렉토리 생성
     *
     * @param directoryPath
     * @return
     */
    public static boolean createDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (!file.exists()) file.mkdirs();
        return true;
    }

    /**
     * 파일 존재 유무
     *
     * @param filePath
     * @return
     */
    public static boolean isExist(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return file.exists();
        }
        return false;
    }

    /**
     * 기본 디렉토리 생성
     *
     * @param context
     * @return
     */
    public static boolean createDefaultDirectory(Context context) {
        // Innochal 디렉토리 생성
        String storagePath = FileUtil.getStoragePath(context);
        LogUtil.d(TAG, "onCreate() -> storagePath : " + storagePath);
        boolean isCreate = FileUtil.createDirectory(storagePath + "innochal");
        LogUtil.d(TAG, "onCreate() -> isCreate : " + isCreate);
        return isCreate;
    }

    /**
     * 파일 삭제
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    /**
     * URI -> File Path 변환
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromUri(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToNext();
                result = cursor.getString(cursor.getColumnIndex("_data"));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    /**
     * URI -> File Path 변환
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri uri) {

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equalsIgnoreCase(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equalsIgnoreCase(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equalsIgnoreCase(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 컬럼에서 데이터 읽기
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * 외부 저장소 문서 여부
     *
     * @param uri
     * @return
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 다운로드 문서 여부
     *
     * @param uri
     * @return
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 미디어 문서 여부
     *
     * @param uri
     * @return
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 구글 사진 여부
     *
     * @param uri
     * @return
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * File Path -> URI 변환
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getUriFromPath(Context context, String path) {
        Uri uri = null;
        Cursor cursor = null;

        try {
            Uri fileUri = Uri.parse(path);
            String filePath = fileUri.getPath();
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, "_data = '" + filePath + "'", null, null);
            if (cursor != null) {
                cursor.moveToNext();
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return uri;
    }

    /**
     * Above KitKat Ver.4.4
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    /**
     * Beatween Honeycomb (Ver.3.0) and Jelly Bean (Ver.4.3)
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromURI_API11To18(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri,
                    projection,
                    null,
                    null,
                    null);
            cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }

    /**
     * Below Honeycomb (Ver.3.0)
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromURI_BelowAPI11(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return result;
    }

    /**
     * 파일 이름 생성 (PCM)
     *
     * @return
     */
    public static String makeOutputFilePCMName(String path) {
        String result = "";
        File directory = new File(path);

        if (!directory.exists()) {
            try {
                directory.mkdirs();
            } catch (Exception e) {
                LogUtil.d(TAG, e.getMessage());
                return null;
            }
        } else {
            if (!directory.canWrite()) {
                LogUtil.e(TAG, "makeOutputFileName() -> makeOutputFile does not have write permission for directory: " + directory);
                return null;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String prefix = sdf.format(new Date());

        String prefix2 = "";
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            prefix2 = "_mono";
        }else {
            prefix2 = "_stereo";
        }
        result = path + "/" + prefix + prefix2 + ".pcm";
        return result;
    }

    /**
     * 파일 이름 생성 (MP3)
     *
     * @return
     */
    public static String makeOutputFileMP3Name(String path) {
        String result = "";
        File directory = new File(path);

        if (!directory.exists()) {
            try {
                directory.mkdirs();
            } catch (Exception e) {
                LogUtil.d(TAG, e.getMessage());
                return null;
            }
        } else {
            if (!directory.canWrite()) {
                LogUtil.e(TAG, "makeOutputFileName() -> makeOutputFile does not have write permission for directory: " + directory);
                return null;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String prefix = sdf.format(new Date());
        result = path + "/" + prefix + ".mp3";
        return result;
    }

    /**
     * 로그 파일에 쓰기
     *
     * @param tag
     * @param string
     */
    public static void writeLog(Context context, String tag, String string) {
        if (!isEnable) return;

        String path = FileUtil.getStoragePath(context) + "innochal/log.txt";
        LogUtil.d(TAG, "writeLog() -> path:" + path);

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                LogUtil.e(TAG, ioe.getMessage());
            }
        }
        if (file.exists()) {
            try {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(path, true));
                bfw.write(DateUtil.getLogTime());
                bfw.write(" ");
                bfw.write(tag);
                bfw.write(" ");
                bfw.write(string);
                bfw.write("\n");
                bfw.flush();
                bfw.close();
            } catch (FileNotFoundException ffe) {
                LogUtil.e(TAG, ffe.getMessage());
            } catch (IOException ioe) {
                LogUtil.e(TAG, ioe.getMessage());
            }
        }
    }


    /**
     * 안심영역 로그 파일에 쓰기
     *
     * @param tag
     * @param string
     */
    public static void writeLogSafetyZone(Context context, String tag, String string) {
//        if (!isEnable) return;
//
//        String path = FileUtil.getStoragePath(context) + "innochal/safetyzone_log.txt";
//        LogUtil.d(TAG, "writeLog() -> path:" + path);
//
//        File file = new File(path);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException ioe) {
//                LogUtil.e(TAG, ioe.getMessage());
//            }
//        }
//        if (file.exists()) {
//            try {
//                BufferedWriter bfw = new BufferedWriter(new FileWriter(path, true));
//                bfw.write(DateUtil.getLogTime());
//                bfw.write(" ");
//                bfw.write(tag);
//                bfw.write(" ");
//                bfw.write(string);
//                bfw.write("\n");
//                bfw.flush();
//                bfw.close();
//            } catch (FileNotFoundException ffe) {
//                LogUtil.e(TAG, ffe.getMessage());
//            } catch (IOException ioe) {
//                LogUtil.e(TAG, ioe.getMessage());
//            }
//        }
    }

    /**
     * 시리얼 번호 파일에 쓰기
     *
     * @param context
     * @param serialNumber
     */
    public static boolean writeSerialNumberToFile(Context context, String serialNumber) {
        String path = FileUtil.getStoragePath(context) + "innochal/serial.txt";
        LogUtil.d(TAG, "writeSerialNumber() -> path :" + path);

        boolean isSuccess = false;

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                LogUtil.e(TAG, ioe.getMessage());
            }
        }
        if (file.exists()) {
            try {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(path));
                bfw.write(serialNumber);
                bfw.flush();
                bfw.close();
                isSuccess = true;
            } catch (FileNotFoundException ffe) {
                LogUtil.e(TAG, ffe.getMessage());
            } catch (IOException ioe) {
                LogUtil.e(TAG, ioe.getMessage());
            }
        }
        return isSuccess;
    }

    /**
     * 시리얼 번호 파일에서 읽기
     *
     * @param context
     * @return
     */
    public static String readSerialNumberFromFile(Context context) {
        StringBuilder serialNumber = new StringBuilder();

        String path = FileUtil.getStoragePath(context) + "innochal/serial.txt";
        LogUtil.d(TAG, "readSerialNumber() -> path :" + path);

        File file = new File(path);
        if (!file.exists()) return serialNumber.toString().trim();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                serialNumber.append(line);
                serialNumber.append('\n');
            }
            br.close();
        } catch (IOException ioe) {
            LogUtil.e(TAG, ioe.getMessage());
        }
        return serialNumber.toString().trim();
    }

    /**
     * 시리얼 번호 파일 존재 유무
     *
     * @param context
     * @return
     */
    public static boolean isSerialNumberFile(Context context) {
        String path = FileUtil.getStoragePath(context) + "innochal/serial.txt";
        LogUtil.d(TAG, "readSerialNumber() -> path :" + path);

        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 시리얼 번호 파일 삭제
     *
     * @param context
     * @return
     */
    public static boolean deleteSerialNumberFromFile(Context context) {
        String path = FileUtil.getStoragePath(context) + "innochal/serial.txt";
        LogUtil.d(TAG, "readSerialNumber() -> path :" + path);

        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * resource id에 있는 text를 읽어 String으로 리턴한다.
     * @param context
     * @param resId
     * @return
     */
    public static String readTxt(Context context, int resId) {
        InputStream inputStream = context. getResources().openRawResource(resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
        }

        return byteArrayOutputStream.toString();
    }


    /**
     * TouchSori App 설정파일을 복사한다.
     */
    public static void expoertAppFile() {
        File file = new File("/data/data/kr.co.innochal.smartsecurity/");
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] list = file.listFiles();
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isDirectory()) {
                        File[] subList = list[i].listFiles();
                        for (int j = 0; j < subList.length; j++) {
                            if (subList[j].isDirectory()) {
//                                ToastUtil.show(this, "하위 폴더가 더있네???");
                            } else {
                                try {
                                    filecopy(subList[j].getAbsolutePath(), "/mnt/sdcard/shlim/" + subList[j].getName());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        try {
                            filecopy(list[i].getAbsolutePath(), "/mnt/sdcard/shlim/" + list[i].getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        try {
            filecopy("/data/user/0/kr.co.innochal.smartsecurity/files/touchsori.sqlite", "/mnt/sdcard/shlim/touchsori.sqlite");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void filecopy(String from, String to) throws Exception{
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;
        try{ fis = new FileInputStream(from);
            fos = new FileOutputStream(to);
            in = fis.getChannel(); out = fos.getChannel();
            in.transferTo(0, in.size(), out);
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(out != null) out.close();
            if(in != null)
                in.close();
            if(fis != null)
                fis.close();
            if(fos != null)
                fos.close();
        }
    }

}
