package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.WordsDataBaseHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private WordsDataBaseHelper wordsDataBaseHelper;
   // private WordsDictionaryCreator wordsDictionaryCreator;


    private SQLiteDatabase wordsDatabase;


    /**
     * Переменная отслеживающая окончание слов в общем словаре
     */
    public boolean wordsEnd = false;

    private static final String TAG = " ->> learning";

  /*  /**
     * Переменная для
     */
   // private int typeOfLearnFinal = 0;


    /**
     * Переменная для выбора имени таблицы currentTableName из массива
     */
    private static int currentTableNum;


    /**
     * Переменная для отключения подсветки правильного ответа на кнопке
     */
    public static boolean withHighLight;

    public static int getCurrentTableNum() {
        return currentTableNum;
    }

    public static void setCurrentTableNum(int currentTableNum) {

        ProcessOfLearning.currentTableNum = currentTableNum;
        currentTableName = WordsDataBaseHelper.getTableNamesList().get(currentTableNum);
    }

    /**
     * Текущее имя словаря
     */
    public static String currentTableName;

    public String getCurrentTableName() {
        return currentTableName;
    }

    /**
     * typeOfLearn создан для изменения
     */
 //   private int typeOfLearn = 0;

  /*  public int getTypeOfLearn() {
        return typeOfLearn;
    }
*/
    /*
    private int setTypeOfLearnFinal() {
        if (typeOfLearn == 0) return 0;
        if (typeOfLearn == 1) return 1;
        return (int) (Math.random() * 2);
    }
*/
    /*
    public void setTypeOfLearn(int typeOfLearn) {
        this.typeOfLearn = typeOfLearn;
    }
*/
    private Context mainContext;

    /**
     * ArrayList содержащий все слова из библиотеки
     */
    public ArrayList<WordCard> allOfWordsOfDictionary;


    public void loadGlobalWordDictionary(){
        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();
    }

    public ArrayList<WordCard> getAllOfWordsOfDictionary() {
        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();
        return allOfWordsOfDictionary;
    }

    public int getAllOfWordsOfDictionarySize() {
        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();
        return allOfWordsOfDictionary.size();
    }


    /**
     * Количество невыученных слов
     */
    private int numberOfUnlearnedWords;

    public int getNumberOfUnlearnedWords() {
        return numberOfUnlearnedWords;
    }

    /**
     * ArrayList в котором содержатся слова, которые изучаются в настоящее время
     * количество слов в этом списке определяется переменной countOfCurrentLearningWords
     * из этого списка выбираются слова для кнопок
     */
    public List<WordCard> currentLearningWords;

    /**
     * HashSet содержащий изучаемые слова выбранные для кнопок
     */
    private ArrayList<WordCard> learningWordsForButtons;


    private ArrayList<Button> buttons;


    /**
     * Переменная для отслеживания правильного ответа
     * используется при создании кнопок
     * в случае когда дан неправильный ответ answeredTrue
     * будет присвоено значение false после чего
     * при создании кнопок не будут обновлены надписи на них
     * а вместо этого кнопки изменят цвет
     */
    private boolean answeredTrue = true;

    private boolean done = true;


    /**
     * Количество слов в списке изучаемых слов
     */
    private int countOfCurrentLearnWords = 10;

    public int getCountOfCurrentLearnWords() {
        return countOfCurrentLearnWords;
    }

    public void setCountOfCurrentLearnWords(int countOfCurrentLearnWords) {
        this.countOfCurrentLearnWords = countOfCurrentLearnWords;
    }

    /**
     * количество необходимых правильных ответов для того чтобы слово считалось выученным
     */
    /*
    private int countOfRepeatWord = 10;
*/

    /**
     * Слово которое показывается вверху страницы которое нужно перевести
     */
   // private WordCard wordThatNeedsToBeTranslated;


    /**
     * количество кнопок с вариантами слов
     */
  //  int countOfButtons = 10;

/*
    public void setCountOfRepeatWord(int countOfRepeatWord) {
        if (countOfRepeatWord > this.countOfRepeatWord) {
            reWriteWordDataBaseAfterChangingRepeat(countOfRepeatWord);
        }
        this.countOfRepeatWord = countOfRepeatWord;

        if (countOfRepeatWord == 1) {
            withHighLight = false;
        } else {
            withHighLight = true;
        }
    }*/


    /**
     * Данный метод служит для того, чтобы при увеличении количества повторений слова,
     * все выученые слова, которые были повторены меньше нового значения будут перезаписаны как невыученые
     * @param newCount новое значение количества повторений слова
     */
    private void reWriteWordDataBaseAfterChangingRepeat(int newCount) {

        for (WordCard wordCard : allOfWordsOfDictionary) {
            if (wordCard.getRightAnswerCount() < newCount) {
                if (wordCard.isLearned() > 0) {
                    wordCard.setIsLearned(0);
                    wordsDataBaseHelper.changeExistsWord(wordCard);
                }
            }
        }
    }
/*
    public int getCountOfRepeatWord() {
        return countOfRepeatWord;
    }
*/
  /*  public WordCard getWordThatNeedsToBeTranslated() {
        return wordThatNeedsToBeTranslated;
    }
*/

    public static ProcessOfLearning getProcessOfLearning(Context context) {

        if (processOfLearning == null) {
            processOfLearning = new ProcessOfLearning(context);
        }

        return processOfLearning;
    }


    /**
     * -------------------------------------------------------------------   Конструктор!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private ProcessOfLearning(Context context) {
        this.mainContext = context;
        wordsDataBaseHelper = WordsDataBaseHelper.getWordsDataBaseHelper(context);
        wordsDatabase = wordsDataBaseHelper.getReadableDatabase();
        currentTableName = WordsDataBaseHelper.getTableNamesList().get(currentTableNum);
      //TODO
       // wordsDictionaryCreator = new WordsDictionaryCreator(wordsDataBaseHelper);
      /*  if (countOfRepeatWord == 1) {
            withHighLight = false;
        } else {
            withHighLight = true;
        }*/
        //    allOfWordsOfDictionary = loadWordsDictionary();


    }




    public void updateWordsDictionary() {
        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();
    }


    /**
     * Загрузка главного словаря
     *
     * @return возвращает словарь состоящий из всех слов, содержащихся в базе данных
     */
    public ArrayList<WordCard> loadCurrentWordsDictionaryFromDatabase() {
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
    /*private ArrayList<WordCard> loadWordsDictionary() {
        ArrayList<WordCard> allWords = new ArrayList<>();
        WordCard tempWordCard;
        numberOfUnlearnedWords = 0;


        Log.i(TAG, currentTableName + "  ");


        Cursor wordCursor = wordsDatabase.query(currentTableName, null, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            if (wordCursor.getInt(6) == 0) numberOfUnlearnedWords++;
            tempWordCard = new WordCard(wordCursor.getInt(0), wordCursor.getString(1).trim(), wordCursor.getString(2).trim(), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6));

            if (!allWords.contains(tempWordCard)) {
                allWords.add(tempWordCard);
            } else {
                // Toast toast = Toast.makeText(mainContext , "Word already exist "  + tempWordCard.getEnglishWord(), Toast.LENGTH_SHORT);
                //   toast.show();
            }

        }
        return allWords;
    }
*/
   /* public void setAllOfWordsOfDictionary(ArrayList<WordCard> allOfWordsOfDictionary) {
        this.allOfWordsOfDictionary = allOfWordsOfDictionary;
    }*/


    /**
     * ----------------------------- Create Activity -----------------------------------------------
     */


    /**
     * Метод добавляет карточку в базу данных
     * с нулевыми показателями всех полей
     * и перезагружает ArrayList содержащий все слова библиотеки
     * <p>
     * Используется для начальной загрузки слов
     *
     * @param EnglishWord английское слово
     * @param russianWord русское слово
     */
    public void addNewWord(String EnglishWord, String russianWord, View view) {
        wordsDataBaseHelper.addNewWord(currentTableName, EnglishWord, russianWord, view);
        updateWordsDictionary();
        // allOfWordsOfDictionary = loadWordsDictionary();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {

        wordsDataBaseHelper.deleteCurrentWord(currentTableName, targetWord);
        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();
    }


    /**
     * Метод сбрасывает весь прогресс переписывает количество правильных и неправильных ответов
     * устанавливает все поля равными нулю
     * <p>
     * Присваивает словарю изучаемых слов currentLearningWords значение null
     */
    public void cleanAllProgress() {
        for (WordCard wordCard : allOfWordsOfDictionary) {
            wordCard.setRightAnswerCount(0);
            wordCard.setWrongAnswerCount(0);
            wordCard.setNowLearning(0);
            wordCard.setIsLearned(0);
            wordsDataBaseHelper.changeExistsWord(wordCard);
        }

        allOfWordsOfDictionary = loadCurrentWordsDictionaryFromDatabase();

        //Очищает словарь изучаемых слов. Иначе при изучении будут загружены не сброшенные данные.
        currentLearningWords = null;
     //   wordThatNeedsToBeTranslated = null;
        answeredTrue = true;
        done = true;
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * ----------------------------- Learn Activity ------------------------------------------------
     * ---------------------------------------------------------------------------------------------
     */


    public void updateLearningList(){
        currentLearningWords = createLearnList();
    }

    private List<WordCard> createLearnList() {

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

    private boolean isWordsEnough() {
        return numberOfUnlearnedWords > countOfCurrentLearnWords;
    }


/*
    private ArrayList<WordCard> createLearnList() {

        //Создаем временный список для хранения карточек
        //В конце метода возвращаем его как результат работы метода
        ArrayList<WordCard> tempLearnList = new ArrayList<>();
        //Случайная карточка для добавления новой карточки в список
        WordCard randomCard;


        //Перебираем общий словарь
        //Добавляем во временный словарь слова которые отмечены как сейчас изучаемые
        for (WordCard wordCard : allOfWordsOfDictionary) {
            if (wordCard.nowLearning() > 0 /*& wordCard.isLearned() == 0*//*)*/
       /*         tempLearnList.add(wordCard);
        }
*/
        //Если временный словарь меньше требуемого размера
        //Получаем случайную карточку
        //Сравниваем количество невыученных слов с необходимым количеством слов для изучения
     /*   while (tempLearnList.size() < countOfCurrentLearnWords) {

            randomCard = getRandomWordCardFromMainDictionary();

            if (numberOfUnlearnedWords > countOfCurrentLearnWords) {
                endingWords = false;
                if (randomCard.nowLearning() == 0 && !tempLearnList.contains(randomCard) && randomCard.isLearned() == 0) {
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    tempLearnList.add(randomCard);
                    //  numberOfUnlearnedWords--;
                }

            } else {

                if (!tempLearnList.contains(randomCard)) {
                    endingWords = true;
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    tempLearnList.add(randomCard);
                    //  numberOfUnlearnedWords--;
                }
            }
        }


        if (endingWords) {
            String text = "";

            if (numberOfUnlearnedWords > 0)
                text = "Слова на исходе осталось неизученно : " + numberOfUnlearnedWords;
            else text = "Вы выучили все слова.";

            Toast toast = Toast.makeText(mainContext, text, Toast.LENGTH_SHORT);
            toast.show();
        }

        return tempLearnList;
    }
*/

    private WordCard getRandomWordCardFromMainDictionary() {
        int random;
        random = (int) (Math.random() * allOfWordsOfDictionary.size());
        return allOfWordsOfDictionary.get(random);
    }


    /**
     * @param - слово которое нужно перевести
     */
  /*  public void showWord(View view) {

        TextView targetWord = view.findViewById(R.id.target_word);
        TextView targetWordCount = view.findViewById(R.id.count_of_target_word);
        String word = "";


        if (typeOfLearnFinal == 0) {
            word = wordThatNeedsToBeTranslated.getRussianWord().toUpperCase(Locale.ROOT);
        } else if (typeOfLearnFinal == 1) {
            word = wordThatNeedsToBeTranslated.getEnglishWord().toUpperCase(Locale.ROOT);
        }

        targetWord.setText(word);

        if(word.length() < 9){
            targetWord.setTextSize(35);
            Log.i("12345","set 35");
        }else if(word.length() < 11){
            targetWord.setTextSize(30);
            Log.i("12345","set 30");
        }else {
            targetWord.setTextSize(25);
            Log.i("12345","set 25");
        }

       /* if (typeOfLearnFinal == 0) {
            targetWord.setText(wordThatNeedsToBeTranslated.getRussianWord().toUpperCase(Locale.ROOT));
        } else if (typeOfLearnFinal == 1) {
            targetWord.setText(wordThatNeedsToBeTranslated.getEnglishWord().toUpperCase(Locale.ROOT));
        }*/
/*
        if (wordThatNeedsToBeTranslated.getRightAnswerCount() >= 0)
            targetWordCount.setText(wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + countOfRepeatWord );
        else targetWordCount.setText("0/" + countOfRepeatWord );

    }
*/

    private ArrayList<Button> getButtons(View view) {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(view.findViewById(R.id.button_1));
        buttons.add(view.findViewById(R.id.button_2));
        buttons.add(view.findViewById(R.id.button_3));
        buttons.add(view.findViewById(R.id.button_4));
        buttons.add(view.findViewById(R.id.button_5));
        buttons.add(view.findViewById(R.id.button_6));
        buttons.add(view.findViewById(R.id.button_7));
        buttons.add(view.findViewById(R.id.button_8));
        buttons.add(view.findViewById(R.id.button_9));
        buttons.add(view.findViewById(R.id.button_10));
        return buttons;
    }

/*
    public void createButtons(View view, Context context) {

        buttons = getButtons(view);
        final Animation wrongAnim = AnimationUtils.loadAnimation(mainContext, R.anim.wrong_answer_animation_button);
      //  final Animation alfa = AnimationUtils.loadAnimation(mainContext, R.anim.faf);

        //allOfWordsOfDictionary = loadWordsDictionary();
      //  currentLearningWords = createLearnList();
        currentLearningWords = createLearnList();


        if (answeredTrue && done) {

            done = false;
            typeOfLearnFinal = setTypeOfLearnFinal();
            learningWordsForButtons = getRandomListForCreateButtons(currentLearningWords, countOfButtons);

            wordThatNeedsToBeTranslated = getWordForLearn(learningWordsForButtons);

        }

        showWord(view);

        Iterator<WordCard> iterator = learningWordsForButtons.iterator();

        for (int i = 0; i < countOfButtons; i++) {


            Button myButton = buttons.get(i);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myButton.setTextColor(context.getColor(R.color.yellow_text_dark));
            }

            WordCard tempWordCardForButton = iterator.next();


            if (typeOfLearnFinal == 0) {
                myButton.setText(tempWordCardForButton.getEnglishWord().toLowerCase(Locale.ROOT)  );
            } else if (typeOfLearnFinal == 1) {
                myButton.setText(tempWordCardForButton.getRussianWord());
            }


            if (myButton.getText().length() < 10) {
                myButton.setTextSize(30);
            } else if (myButton.getText().length() < 15) {
                myButton.setTextSize(27);
            } else {
                myButton.setTextSize(23);
            }
            myButton.setBackground(context.getResources().getDrawable(R.drawable.button_for_learn_background));


            if (!answeredTrue) {
                myButton.setBackground(context.getResources().getDrawable(R.drawable.wrong_button_background));
            }

            if (tempWordCardForButton.getEnglishWord().equals(wordThatNeedsToBeTranslated.getEnglishWord()) && tempWordCardForButton.getRightAnswerCount() < 1) {


                if (withHighLight) {
                    myButton.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
                    myButton.startAnimation(wrongAnim);
                }

                if (!withHighLight) {
                    myButton.setBackground(context.getResources().getDrawable(R.drawable.button_for_learn_background));
                }

                if (!answeredTrue) {
                    //TODO После неправильного ответа включаетс чигание и не отключается потом!!!!!!!!!!
                     //После
                    myButton.startAnimation(wrongAnim);
                    myButton.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
                }

            }


            myButton.setOnClickListener(view2 -> {

                onClickButton(tempWordCardForButton, view, context, view2);


            });


        }


    }
*/

    /**
     * @param wordCards      ArrayList с изучаемыми в настоящее время карточками
     * @param countOfButtons количество кнопок
     * @return
     */
    private ArrayList<WordCard> getRandomListForCreateButtons(List<WordCard> wordCards, int countOfButtons) {

        ArrayList<WordCard> learningWordsForButtons = new ArrayList<>();
        WordCard wordCard;
        int random = 0;

        while (learningWordsForButtons.size() < countOfButtons) {

            random = (int) (Math.random() * wordCards.size());
            wordCard = wordCards.get(random);

          //  if (isWordsEnough()) {
                if (!learningWordsForButtons.contains(wordCard)) {
                    learningWordsForButtons.add(wordCard);
                }
          //  }else{
                //o
         //   }

        }

        return learningWordsForButtons;
    }


    /**
     * @param wordCard получает WordsCard с кнопки для проверки ответа
     *                 Тут будет проверка ответа
     */
    /*
    public void onClickButton(WordCard wordCard, View view, Context context, View button) {
        final Animation animAlpha = AnimationUtils.loadAnimation(mainContext, R.anim.right_answer_animation_button);
       // final Animation animA = AnimationUtils.loadAnimation(mainContext, R.anim.eeee);

        if (wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())) {

            if (answeredTrue) {

                reactionToTheRightAnswer(wordCard);

                button.startAnimation(animAlpha);
                button.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
                //button.setBackground(context.getResources().getDrawable(android.R.color.holo_blue_dark));
                //TODO
                updateWordsDictionary();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createButtons(view, context);
                    }
                }, 300);

            } else {
               // button.startAnimation(animA);
                reactionToTheRightAnswer(wordCard);
                createButtons(view, context);
            }

        } else {
            reactionToTheWrongAnswer(wordThatNeedsToBeTranslated, view, context);
            showWord(view);
            //  showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1), view);
        }
    }
*/
    /**
     * NEW Version <<<----------------------------------
     *//*

    public void reactionToTheRightAnswer(WordCard rightWordCard) {
        rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
        if (rightWordCard.getRightAnswerCount() < Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN ) {
            //Log.i(TAG,countOfRepeatWord + "  " + rightWordCard.getRightAnswerCount());
        //    rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
            wordsDataBaseHelper.changeExistsWord(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        } else {
            //rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
            rightWordCard.setNowLearning(0);
            rightWordCard.setIsLearned(1);
            wordsDataBaseHelper.changeExistsWord(rightWordCard);
            currentLearningWords.remove(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        }



      //  answeredTrue = true;
     //   done = true;
    }
*/


    public void changeExistingWordInDataBase(WordCard wordCard){
        wordsDataBaseHelper.changeExistsWord(wordCard);
    }
/*

    public void reactionToTheWrongAnswer(WordCard wrongWordCard, View view, Context context) {
        wrongWordCard.setRightAnswerCount(-1);
        wrongWordCard.setWrongAnswerCount(wrongWordCard.getWrongAnswerCount() + 1);
        wordsDataBaseHelper.changeExistsWord(wrongWordCard);
        reWriteWordCardInArrayList(allOfWordsOfDictionary, wrongWordCard);
        answeredTrue = false;
        createButtons(view, context);
    }
*/

    /**
     * NEW Version <<<----------------------------------
     */
    /*
    public void reactionToTheWrongAnswer(WordCard wrongWordCard) {
        wrongWordCard.setRightAnswerCount(-1);
        wrongWordCard.setWrongAnswerCount(wrongWordCard.getWrongAnswerCount() + 1);
        wordsDataBaseHelper.changeExistsWord(wrongWordCard);
        reWriteWordCardInArrayList(allOfWordsOfDictionary, wrongWordCard);
        answeredTrue = false;
       // createButtons(view, context);
    }
*/
/*
    private void reWriteWordCardInArrayList(ArrayList<WordCard> wordCards, WordCard wordCard) {
        wordCards.remove(wordCard);
        wordCards.add(wordCard);
    }

*/

    /*
    public WordCard getWordForLearn(ArrayList<WordCard> learningWordsForButtons) {
        boolean run = true;
        int random = (int) (Math.random() * learningWordsForButtons.size());
        WordCard tempWordCard = learningWordsForButtons.get(random);
        if (isWordsEnough()) {
            return tempWordCard;
        } else {
            while (run) {

                if (tempWordCard.getRightAnswerCount() < countOfRepeatWord  || getNumberOfUnlearnedWords() <= 2) {

                    Log.i(TAG , tempWordCard.getRightAnswerCount() + " " + countOfRepeatWord + " " + getNumberOfUnlearnedWords());
                    return tempWordCard;
                }

                random = (int) (Math.random() * learningWordsForButtons.size());
                tempWordCard = learningWordsForButtons.get(random);
            }
        }


        return learningWordsForButtons.get(random);
    }
*/

    /**
     * Метод нужен для того, чтобы после изменения модели изучения между англ - русск, русск - англ
     * и случайным выбором, ставилась метка о том, что
     * 1 - ответ правильный
     * 2 - ответ закончен
     * Это позволяет обновить кнопки и угадываемое слово иначе после изменения модели
     * первый ответ будет старым.
     */
    /*
    public void updateButtons() {
        answeredTrue = true;
        done = true;
    }
*/

}
