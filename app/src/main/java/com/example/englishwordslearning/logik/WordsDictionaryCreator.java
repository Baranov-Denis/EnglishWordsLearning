package com.example.englishwordslearning.logik;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.englishwordslearning.database.WordsDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class WordsDictionaryCreator {

    private static final String TAG = "WordsDictionaryCreator";

    private final WordsDataBaseHelper wordsDataBaseHelper;


    /**
     * Количество невыученных слов в текущем словаре
     */
    private int numberOfUnlearnedWords;

    public int getNumberOfUnlearnedWords() {
        return numberOfUnlearnedWords;
    }


    /**
     * Текущий словарь
     */
    private ArrayList<WordCard> allOfWordsOfDictionary;


    /**
     * Количество слов которые изучаются в настоящее время
     */
    private int countOfCurrentLearnWords;

    private boolean wordsEnd;


    public WordsDictionaryCreator(WordsDataBaseHelper wordsDataBaseHelper) {
        this.wordsDataBaseHelper = wordsDataBaseHelper;
    }

    /**
     * Загрузка главного словаря
     *
     * @return возвращает словарь состоящий из всех слов, содержащихся в базе данных
     */
    public ArrayList<WordCard> loadCurrentWordsDictionaryFromDatabase(SQLiteDatabase wordsDatabase) {
        ArrayList<WordCard> allWords = new ArrayList<>();
        WordCard tempWordCard;
        numberOfUnlearnedWords = 0;

        Cursor wordCursor = wordsDatabase.query(ProcessOfLearning.currentTableName, null, null, null, null, null, "ENGLISH_WORD");

        while (wordCursor.moveToNext()) {
            if (wordCursor.getInt(6) == 0) numberOfUnlearnedWords++;
            tempWordCard = new WordCard(wordCursor.getInt(0), wordCursor.getString(1).trim(), wordCursor.getString(2).trim(), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6));
            if (!allWords.contains(tempWordCard)) {
                allWords.add(tempWordCard);
            }
        }
        wordCursor.close();
        allOfWordsOfDictionary = allWords;
        return allWords;
    }


    public List<WordCard> createLearnList() {

        //Создаем временный список для хранения карточек
        //В конце метода возвращаем его как результат работы метода
        List<WordCard> tempLearnList = getListOfCurrentLearningWords();
        //Случайная карточка для добавления новой карточки в список
        WordCard randomCard;


        //Если временный словарь меньше требуемого размера
        //Получаем случайную карточку
        //Сравниваем количество невыученных слов с необходимым количеством слов для изучения
        while (tempLearnList.size() < countOfCurrentLearnWords) {

            randomCard = getRandomWordCardFromMainDictionary();

            if (isWordsEnough()) {

                wordsEnd = false;

                //  Сейчас не учится                  не содержится в текущем словаре      не выучено
                if (randomCard.nowLearning() == 0 && !tempLearnList.contains(randomCard) && randomCard.isLearned() == 0) {
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    tempLearnList.add(randomCard);
                    //  numberOfUnlearnedWords--;
                }

            } else {

                wordsEnd = true;

                if (!tempLearnList.contains(randomCard)) {
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    tempLearnList.add(randomCard);
                    //  numberOfUnlearnedWords--;
                }
            }
        }

        return tempLearnList;
    }


    /**
     * @return список слов из общего текущего словаря
     * которые отмеченны как изучаемые в настоящее время.
     */
    private List<WordCard> getListOfCurrentLearningWords() {
        List<WordCard> tempLearnList = new ArrayList<>();
        //Перебираем общий словарь
        //Добавляем во временный словарь слова которые отмечены как сейчас изучаемые
        for (WordCard wordCard : allOfWordsOfDictionary) {
            if (wordCard.nowLearning() > 0)
                tempLearnList.add(wordCard);
        }
        return tempLearnList;
    }


    /**
     * @return случайное слово из общего текущего словаря
     * без каких либо условий
     */
    private WordCard getRandomWordCardFromMainDictionary() {
        int random;
        random = (int) (Math.random() * allOfWordsOfDictionary.size());
        return allOfWordsOfDictionary.get(random);
    }

    private boolean isWordsEnough() {
        return numberOfUnlearnedWords > countOfCurrentLearnWords;
    }

}
