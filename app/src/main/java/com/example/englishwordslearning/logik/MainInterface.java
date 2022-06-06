package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.englishwordslearning.database.WordsDataBaseHelper;

import java.util.ArrayList;

public class MainInterface {

    private static MainInterface mainInterface;

    private final WordsDataBaseHelper wordsDataBaseHelper;
    //  private final SQLiteDatabase mySQLiteDatabase;
    private ProcessOfLearning processOfLearning;

    public static MainInterface getMainInterface() {
        return mainInterface;
    }

    public static MainInterface getMainInterface(Context context) {
        if (mainInterface == null) {
            mainInterface = new MainInterface(context);
        }
        return mainInterface;
    }


    /**
     * Приватный конструктор
     * получаем экземпляр базы данных
     * получаем экземпляр главного процесса
     * загружаем ArrayList<WordCard> с библиотекой всех слов в главный процесс
     * @param context
     */
    private MainInterface(Context context) {

        wordsDataBaseHelper = WordsDataBaseHelper.getWordsDataBaseHelper(context);
        processOfLearning = ProcessOfLearning.getProcessOfLearning(context);


      //  processOfLearning.setAllOfWordsOfDictionary(wordsDataBase.loadDictionaryFromSQLiteDataBase());
    }

    /**
     * Возвращает базу данных
     * используется для получения курсора
     * и построения List View в Create Activity
     * Рекомендуется переработать построение ListView с помощью Array адаптера
     * @return SQLiteDataBase
     */
    public SQLiteDatabase getSQLiteDatabase() {
        return wordsDataBaseHelper.getMainDataBase();
    }

    /**
     * Метод добавляет карточку в базу данных
     * с нулевыми показателями всех полей
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param EnglishWord английское слово
     * @param russianWord русское слово
     */
    public void addNewWord(String EnglishWord, String russianWord,View view) {
        processOfLearning.addNewWord(EnglishWord, russianWord, view);
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает ArrayList содержащий все слова библиотеки
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        processOfLearning.deleteCurrentWord(targetWord);
    }

    
    /**
    * Метод сбрасывает весь прогресс обучения
    * устанавливает 0 во все значения
    * не трогает слова
    */
    public void resetAllProgress(){
        processOfLearning.cleanAllProgress();
    }




/*
    public int getCountOfRepeatWord() {
        return processOfLearning.getCountOfRepeatWord();
    }*/
/*
    public void setCountOfRepeatWord(int count) {
        processOfLearning.setCountOfRepeatWord(count);
    }
*/
    public int getTheNumberOfWordsBeingStudied() {return processOfLearning.getCountOfCurrentLearnWords();}

    public void setNumberOfCurrentLearnWords(int count) {processOfLearning.setCountOfCurrentLearnWords(count);}






    /**
     * ----------------------------------------- Learn Activity ------------------------------------
     */

  /*  public void showWordForLearn(View view){
        processOfLearning.showWord(view);
    }*/

    public void updateWordsDictionary(){
        processOfLearning.updateWordsDictionary();

    }
/*
    public void createButtonsForLearning( View view,Context context){
        processOfLearning.createButtons(view,context);
    }
*//*
    public  WordCard getWordThatNeedsToBeTranslated() {
        return processOfLearning.getWordThatNeedsToBeTranslated();
    }
*/
    public String getCurrentTableName(){
        return processOfLearning.getCurrentTableName();
    }


    public void startLearning(Context context) {
    }


    public int getNumberOfAllWords() {
        return processOfLearning.getAllOfWordsOfDictionarySize();
    }

  /*  public int isTypeOfLearn() {
        return processOfLearning.getTypeOfLearn();
    }
*/
 /*   public void setTypeOfLearn(int isChecked) {
        processOfLearning.setTypeOfLearn(isChecked);

    }*/

    /*
    public void updateButtons() {
        processOfLearning.updateButtons();
    }*/

    public ArrayList<WordCard> getAllWordsFromCurrentDictionary(){
        return processOfLearning.getAllOfWordsOfDictionary();
    }

    public int getNumberOfUnlearnedWords(){
        return processOfLearning.getNumberOfUnlearnedWords();
    }
}
