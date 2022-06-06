package com.example.englishwordslearning.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.englishwordslearning.logik.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExternalDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Данная база данных используется для загрузки внешней базы
     * После этого данную базу нужно загрузить в основную базу
     * <p>
     * ExternalDatabaseHelper externalDatabaseHelper = new ExternalDatabaseHelper(context);
     * SQLiteDatabase externalDatabase = externalDatabaseHelper.getExternalDatabase();
     * Cursor wordCursor = externalDatabase.query("words", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
     * while (wordCursor.moveToNext()) {
     * Проверка и вставка
     * }
     * wordCursor.close();
     * <p>
     * <p>
     * Нужно проверить каждое слово из этой базы и добавлять только те слова, которые отсутствуют
     * в основной базе
     */
    private static final String TAG = "External_log -> ";
    private static final int DB_VERSION = 1;
    /**
    DB_NAME это имя файла который лежит в assets
     */



    //TODO
   // private static final String DB_NAME = "super.db";
    private static final String DB_NAME = Constants.EXTERNAL_DATABASE_NAME;
    private static String DB_PATH = "";
   // private final static String TABLE_NAME = "words";
    private final Context context;
    private SQLiteDatabase externalDatabase;

  //  public static String getTableName() {
  //      return TABLE_NAME;
  //  }

    public SQLiteDatabase getExternalDatabase() {
        return externalDatabase;
    }

    public ExternalDatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";

        this.context = context;

        copyDataBase();

        openDataBase();
    }


    /**
     * copyDataBase                                                                   copyDataBase
     */
    private void copyDataBase() {
        try {
            copyDBFile();
        } catch (IOException mIOException) {
            throw new Error("ErrorCopyingDataBase");
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = context.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    /**
     * openDataBase                                                                   openDataBase
     */
    public boolean openDataBase() throws SQLException {
        externalDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        this.getWritableDatabase();
        return externalDatabase != null;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void updateDataBase() throws IOException {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (dbFile.exists())
            dbFile.delete();
        copyDataBase();
    }
}
