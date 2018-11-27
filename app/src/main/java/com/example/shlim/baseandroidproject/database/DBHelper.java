package com.example.shlim.baseandroidproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.example.shlim.baseandroidproject.common.Define;
import com.example.shlim.baseandroidproject.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by innochal on 2018-04-16.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();    // 디버그 태그

    private final Context mContext;
    private volatile static DBHelper mInstance;
    private static SQLiteDatabase db;
    private static String CLASSNAME = "DBHelper";

    private DBHelper(Context context)  throws Exception {
        super(context, Define.DB_NAME, null, Define.DB_VERSION);
        this.mContext = context;
        copyDatabaseFile();
    }

    /**
     * 초기화
     * @param context
     * @throws Exception
     */
    private static void initialize(Context context) throws Exception {
        if (mInstance == null) {
            synchronized (DBHelper.class) {
                if (mInstance == null) {
                    LogUtil.d(TAG, DBHelper.CLASSNAME + " Try to create instance of database (" + Define.DB_NAME + ")");
                    mInstance = new DBHelper(context);
                    try {
                        db = mInstance.getWritableDatabase();
                        LogUtil.d(TAG, "Creating or opening the database ( " + Define.DB_NAME + " ).");
                    } catch (SQLiteException se) {
                        LogUtil.e(TAG, "Cound not create and/or open the database ( " + Define.DB_NAME + " ) that will be used for reading and writing. : " + se);
                    }
                    LogUtil.d(TAG, DBHelper.CLASSNAME + " instance of database (" + Define.DB_NAME + ") created !");
                }
            }
        }
    }

    /**
     * instance 리턴
     * @param context
     * @return
     * @throws Exception
     */
    public static final DBHelper getInstance(Context context) throws Exception {
        initialize(context);
        return mInstance;
    }

    /**
     * DB Close
     */
    public void close() {
        if (mInstance != null) {
            LogUtil.d(TAG, DBHelper.CLASSNAME + "Closing the database [ " + Define.DB_NAME + " ].");
            db.close();
            mInstance = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d(TAG, DBHelper.CLASSNAME + "Create & Copy : " + Define.DB_NAME+" version : "+db.getVersion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d(TAG, "onUpgrade oldVersion : "+oldVersion+" newVersion : "+newVersion);

        try {
            switch (oldVersion) {
                case 1:
                    break;
            }
        }catch (Exception e) {
            LogUtil.d(TAG, e.getLocalizedMessage());
        }
    }

    /****
     * Method for select statements
     *
     * @param sql
     *            : sql statements
     * @return : cursor
     */
    public Cursor get(String sql) {
        return db.rawQuery(sql, null);
    }

    /****
     * Method for select statements
     *
     * @param sql
     *            : sql statements
     * @return : cursor
     */
    public Cursor get(String sql, String[] agrs) {
        return db.rawQuery(sql, agrs);
    }

    /***
     * Method to insert record
     *
     * @param table
     *            : table name
     * @param values
     *            : ContentValues instance
     * @return : long (rowid)
     */
    public long insert(String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    /***
     * Method to update record
     *
     * @param table
     *            : table name
     * @param values
     *            : ContentValues instance
     * @param whereClause
     *            : Where Clause
     * @return ; int
     */
    public int update(String table, ContentValues values, String whereClause) {
        return db.update(table, values, whereClause, null);
    }

    /***
     * Method to delete record
     *
     * @param table
     *            : table name
     * @param whereClause
     *            : Where Clause
     * @return : int
     */
    public int delete(String table, String whereClause) {
        return db.delete(table, whereClause, null);
    }

    /***
     * Method to run sql
     *
     * @param sql
     */
    public void exec(String sql) {
        db.execSQL(sql);
    }

    public void exec(String sql, String[] args) {
        db.execSQL(sql, args);
    }

    /****
     * logCursorInfo : Cursor에서 리턴받는 Result
     *
     * @param c
     */
    public void logCursorInfo(Cursor c) {
        LogUtil.d(TAG, "*** Cursor Begin *** " + "Results:" + c.getCount() + " Colmns: " + c.getColumnCount());

        // Column Name print
        String rowHeaders = "|| ";
        for (int i = 0; i < c.getColumnCount(); i++) {
            rowHeaders = rowHeaders.concat(c.getColumnName(i) + " || ");
        }

        LogUtil.d(TAG, "COLUMNS " + rowHeaders);
        // Record Print
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            String rowResults = "|| ";
            for (int i = 0; i < c.getColumnCount(); i++) {
                rowResults = rowResults.concat(c.getString(i) + " || ");
            }

            LogUtil.d(TAG, "Row " + c.getPosition() + ": " + rowResults);

            c.moveToNext();
        }
//		c.close();
        LogUtil.d(TAG, "*** Cursor End ***");
    }


    /**
     * asset에 있는 DB 파일을 복사한다.
     */
    public void copyDatabaseFile() {
        LogUtil.d(TAG, "DBHelpser copyDatabaseFile()");
        InputStream myInput = null;
        OutputStream myOutput = null;
        SQLiteDatabase database = null;
        if (!checkDataBaseExistence()) {
            database = getReadableDatabase();
            try {
                myInput = mContext.getAssets().open(Define.DB_NAME);
                String outFileName = Define.DB_PATH;
                myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    myOutput.flush();
                    myOutput.close();
                    myInput.close();
                    if (database != null && database.isOpen()) {
                        database.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * asset에 있는 DB 복사 시 db 유효성 검사
     * @return
     */
    private boolean checkDataBaseExistence() {

        SQLiteDatabase dbToBeVerified = null;

        try {
            String dbPath = Define.DB_PATH;
            LogUtil.d(TAG, dbPath);
            dbToBeVerified = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            LogUtil.e(TAG, "checkDataBaseExistence exception : " +  e.getMessage());
        }
        if (dbToBeVerified != null) {
            dbToBeVerified.close();
        }
        return dbToBeVerified != null ? true : false;
    }


    /**
     * 로컬 저장되어 있는 DB를 단말의 /shlim 폴더로 복사한다.
     */
    public void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = Environment.getDataDirectory().getPath() + File.separator + "com.example.shlim.baseandroidproject"+ File.separator+"databases"+File.separator+ Define.DB_NAME;
                String backupDBPath = "/shlim/"+ Define.DB_NAME;
                String expeortDirctoryPath = Environment.getExternalStorageDirectory() + "/shlim/";

                File exportF = new File(expeortDirctoryPath);
                if(false == exportF.exists()) {
                    exportF.mkdir();
                }

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(mContext, "Backup Successful!", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Backup Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
