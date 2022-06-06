package com.example.englishwordslearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.englishwordslearning.logik.Constants;
import com.example.englishwordslearning.logik.ProcessOfLearning;
import com.example.englishwordslearning.logik.WordCard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsDataBaseHelper extends SQLiteOpenHelper {


    private final static String TAG = "WordsDataBaseHelper ";
    /**
     * Нужно менять DB_VERSION при загрузке новой внешней базы данных расположена в Constants
     */
    private static final int DB_VERSION = Constants.DB_VERSION_FOR_UPDATE;
   // private static final int DB_VERSION = 1;

    private final Context mContext;

    private static final String DB_NAME = "MAIN_DATABASE";

    // private static String TABLE_NAME = "MAIN_TABLE_DATABASE";

    private static ArrayList<String> tableNamesList;

    public static ArrayList<String> getTableNamesList() {
        return tableNamesList;
    }

    private ExternalDatabaseHelper externalDatabaseHelper;
    private SQLiteDatabase externalDatabase;

    private static SQLiteDatabase mainDataBase;

    private static WordsDataBaseHelper wordsDataBaseHelper;


    // public static String getTableName() {
    //     return TABLE_NAME;
    //  }

    public static SQLiteDatabase getMainDataBase() {
        return mainDataBase;
    }

    public static WordsDataBaseHelper getWordsDataBaseHelper() {
        return wordsDataBaseHelper;
    }

    public static WordsDataBaseHelper getWordsDataBaseHelper(Context context) {
        if (wordsDataBaseHelper == null) {
            wordsDataBaseHelper = new WordsDataBaseHelper(context);
        }
        return wordsDataBaseHelper;
    }

    private WordsDataBaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;

        WordsDataBaseHelper wordsDataBaseHelper = this;

        mainDataBase = wordsDataBaseHelper.getReadableDatabase();

        this.getReadableDatabase();

        if (tableNamesList == null) {
            createTableNamesList();
        }


        Log.i(TAG, mainDataBase.toString());

    }


    private void createTableNamesList() {
        externalDatabaseHelper = new ExternalDatabaseHelper(mContext);
        externalDatabase = externalDatabaseHelper.getExternalDatabase();
        Cursor namesCursor = externalDatabase.query(Constants.LIST_OF_TABLES_NAMES, new String[]{Constants.COLUMN_WHERE_CONTAINS_TABLES_NAMES}, null, null, null, null, null);
        tableNamesList = new ArrayList<>();
        while (namesCursor.moveToNext()) {
            tableNamesList.add(namesCursor.getString(0));
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        createTableNamesList();

        for (int i = 0; i < tableNamesList.size(); i++) {

            String tableName = tableNamesList.get(i);

            Log.i(TAG, i + "  " + tableName);

            createDB(db, tableName);
        }

        insertExternalDatabase(db);
        //ProcessOfLearning.setCurrentTableNum(1);
    }

    private void createDB(SQLiteDatabase db, String tableName) {
        db.execSQL("CREATE TABLE " + tableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT UNIQUE, RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertExternalDatabase(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertExternalDatabase(db);
    }

    private void insertExternalDatabase(SQLiteDatabase db) {
        String englishWord;
        String russianWord;
        ExternalDatabaseHelper externalDatabaseHelper = new ExternalDatabaseHelper(mContext);

        SQLiteDatabase externalDatabase = externalDatabaseHelper.getExternalDatabase();


        Cursor namesCursor = externalDatabase.query(Constants.LIST_OF_TABLES_NAMES, new String[]{Constants.COLUMN_WHERE_CONTAINS_TABLES_NAMES}, null, null, null, null, null);
        tableNamesList = new ArrayList<>();
        while (namesCursor.moveToNext()) {
            tableNamesList.add(namesCursor.getString(0));
            try {
                createDB(db, namesCursor.getString(0));
                Log.i(TAG, namesCursor.getString(0) + " table created");
            } catch (Exception e) {
                Log.i(TAG, namesCursor.getString(0) + " table already exists");
            }


        }


        for (int i = 0; i < tableNamesList.size(); i++) {
            if(!tableNamesList.get(i).equals(Constants.LIST_OF_TABLES_NAMES)){
            Cursor wordCursor = externalDatabase.query(tableNamesList.get(i), new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
            while (wordCursor.moveToNext()) {
                englishWord = wordCursor.getString(1).toLowerCase();
                russianWord = wordCursor.getString(2).toLowerCase();
                insertWord(db, tableNamesList.get(i), englishWord, russianWord);
            }
            wordCursor.close();}
        }
    }


   /* private void upgradeMyDataBase(SQLiteDatabase db, int i, int dbVersion) {
        if (dbVersion == 1) {
            db.execSQL("CREATE TABLE TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT , RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                    "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");
        }

    }*/


    /**
     * Вставка слов в базу данных
     * /Вставляется английское слово , русское слово , количество сколько раз слово было угадано , и выучено слово или нет 1 или 0
     */
    private void insertWord(SQLiteDatabase db, String tableName, String englishWord, String russianWord) {
        if (!testExistingWord(db, tableName, englishWord, russianWord)) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", englishWord);
            wordValue.put("RUSSIAN_WORD", russianWord);
            wordValue.put("RIGHT_ANSWER_COUNT", 0);
            wordValue.put("WRONG_ANSWER_STAT", 0);
            wordValue.put("NOW_LEARNING", 0);
            wordValue.put("IS_LEARNED", 0);
            db.insert(tableName, null, wordValue);
        }
    }

    /*  private void insertWord(SQLiteDatabase db, String tableName, String englishWord, String russianWord, int rightAnswer, int wrongAnswer, int nowLearning, int isLearned) {
          if (!testExistingWord(db, tableName, englishWord, russianWord)) {
              ContentValues wordValue = new ContentValues();
              wordValue.put("ENGLISH_WORD", englishWord);
              wordValue.put("RUSSIAN_WORD", russianWord);
              wordValue.put("RIGHT_ANSWER_COUNT", rightAnswer);
              wordValue.put("WRONG_ANSWER_STAT", wrongAnswer);
              wordValue.put("NOW_LEARNING", nowLearning);
              wordValue.put("IS_LEARNED", isLearned);
              db.insert(tableName, null, wordValue);
          }
      }
  */
    private void changeWord(SQLiteDatabase db, String tableName, String englishWord, String russianWord, int rightAnswerCount, int wrongAnswerStat, int nowLearning, int isLearned) {
        ContentValues wordValue = new ContentValues();
        wordValue.put("ENGLISH_WORD", englishWord);
        wordValue.put("RUSSIAN_WORD", russianWord);
        wordValue.put("RIGHT_ANSWER_COUNT", rightAnswerCount);
        wordValue.put("WRONG_ANSWER_STAT", wrongAnswerStat);
        wordValue.put("NOW_LEARNING", nowLearning);
        wordValue.put("IS_LEARNED", isLearned);
        db.update(tableName, wordValue, "ENGLISH_WORD = ?", new String[]{englishWord});
    }

 /*   private void insertWord(SQLiteDatabase db, String tableName, WordCard wordCard) {
        if (!testExistingWord(db, tableName, wordCard.getEnglishWord(), wordCard.getRussianWord())) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", wordCard.getEnglishWord());
            wordValue.put("RUSSIAN_WORD", wordCard.getRussianWord());
            wordValue.put("RIGHT_ANSWER_COUNT", wordCard.getRightAnswerCount());
            wordValue.put("WRONG_ANSWER_STAT", wordCard.getWrongAnswerCount());
            wordValue.put("NOW_LEARNING", wordCard.nowLearning());
            wordValue.put("IS_LEARNED", wordCard.isLearned());
            db.insert(tableName, null, wordValue);
        }
    }
*/

    /**
     * Проверка существования английского и русского слов
     * в базе данных
     *
     * @param database    база данных
     * @param englishWord англ слово
     * @param russianWord русское слово
     * @return возвращает false если нет ни русского ни английского слова
     * если в базе данных есть или русское или англ слово, то возвращает true
     * нельзя добавить одинаковые слова тоесть run - бег и run - бежать добавить не получится
     * Возможно придётся изменить это??????????????????????????
     */
    private boolean testExistingWord(SQLiteDatabase database, String tableName, String englishWord, String russianWord) {
        boolean resultEnglish = false;
        boolean resultRussian = false;
        Cursor englishCursor = database.query(tableName, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{englishWord}, null, null, null);
        if (englishCursor.moveToNext()) {
            resultEnglish = englishCursor.getCount() > 0;
        }

        Cursor russianCursor = database.query(tableName, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "RUSSIAN_WORD = ?", new String[]{russianWord}, null, null, null);
        if (russianCursor.moveToNext()) {
            resultRussian = russianCursor.getCount() > 0;
        }
        englishCursor.close();
        russianCursor.close();

        return resultEnglish && resultRussian;
    }

    private void deleteWord(SQLiteDatabase db, String tableName, long id) {
        db.delete(tableName, "_id = ?", new String[]{"" + id});
    }


    /**
     * Проверка на пустую строку и на то, чтобы английское слово было английским , а русское русским
     *
     * @param englishWord англ слово
     * @param russianWord русс слово
     * @return если слова соответствуют всем требованиям, то возвращает true
     */
    private boolean testGettingWords(String englishWord, String russianWord) {
        Pattern englishPattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher englishMatcher = englishPattern.matcher(englishWord);

        Pattern russianPattern = Pattern.compile("[а-я]", Pattern.CASE_INSENSITIVE);
        Matcher russianMatcher = russianPattern.matcher(russianWord);

        return englishMatcher.find() && russianMatcher.find();
    }


    /**
     *
     * ----------------------------------------------- PUBLIC METHODS -----------------------------------------------------------------
     *
     */

    /**
     * Метод для добавления слов
     *
     * @param englishWord
     * @param russianWord
     */
    public void addNewWord(String tableName, String englishWord, String russianWord, View view) {

        if (testGettingWords(englishWord, russianWord)) {
            //Приведение строк к нижнему регистру
            String correctedEnglishWord = englishWord.toLowerCase();
            String correctedRussianWord = russianWord.toLowerCase();
            //Запуск метода вставки в базу данных
            insertWord(mainDataBase, tableName, correctedEnglishWord, correctedRussianWord);


            Toast toast = Toast.makeText(view.getContext(), "Word added", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(view.getContext(), "Word did not added!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void changeExistsWord(WordCard wordCard) {
        changeWord(mainDataBase, ProcessOfLearning.currentTableName, wordCard.getEnglishWord(), wordCard.getRussianWord(), wordCard.getRightAnswerCount(), wordCard.getWrongAnswerCount(), wordCard.nowLearning(), wordCard.isLearned());
    }

    /**
     * Метод для удаления выделенного слова по id
     *
     * @param id
     */
    public void deleteCurrentWord(String tableName, long id) {
        String deletingWord = "";

        Cursor cursor = mainDataBase.query(tableName, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "_id = ?", new String[]{Long.toString(id)}, null, null, null);
        Log.i("---> ", Long.toString(id) + "   ");
        if (cursor.moveToNext()) {
            deletingWord = cursor.getString(1);
            Log.i("---> ", deletingWord);
        }
        cursor.close();
        deleteWord(mainDataBase, tableName, id);
        //   userDataBaseHelper.deleteCurrentWord(deletingWord);
    }


    /**
     * Этот метод загружает всю информацию из базы данных и преобразует её в
     * WordCard классы
     *
     * @return - HashSet обьектов типа WordCard со всеми словами из SQLiteDataBase
     */
  /*  public ArrayList<WordCard> loadDictionaryFromSQLiteDataBase() {

        ArrayList<WordCard> tempLibrary = new ArrayList<WordCard>();
        Cursor wordCursor = mainDataBase.query(TABLE_NAME, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            tempLibrary.add(new WordCard(wordCursor.getInt(0),wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6)));
        }
        wordCursor.close();

        return tempLibrary;


    }*/

}
