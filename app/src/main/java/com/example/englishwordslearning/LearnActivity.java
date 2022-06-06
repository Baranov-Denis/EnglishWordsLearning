package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishwordslearning.logik.Colors;
import com.example.englishwordslearning.logik.ProcessOfLearning;
import com.example.englishwordslearning.logik.Variables;
import com.example.englishwordslearning.logik.WordCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LearnActivity extends AppCompatActivity {

    /**
     * ВСЕ СЛОВА КОТОРЫЕ СЕЙЧАС ИЗУЧАЮЮТСЯ!!!!
     * ArrayList в котором содержатся слова, которые изучаются в настоящее время
     * количество слов в этом списке определяется переменной countOfCurrentLearningWords
     * из этого списка выбираются слова для кнопок
     */
    private List<WordCard> currentLearningWords;


    /**
     * СЛОВА КОТОРЫЕ СЕЙЧАС НА КНОПКАХ!!!!
     */
    private List<WordCard> currentButtonsWords;

    /**
     * Слово которое нужно перевести и будет показано в верхнем поле
     * содержит wordCard для возможности переключения языка
     */
    private WordCard wordThatNeedsToBeTranslated;


    private boolean answeredTrue = true;


    /**
     * Переменная для изменения типа обучения
     * 0 = Русское слово - аглийские кнопки
     * 1 = Английское слово - русские кнопки
     * 2 = Случайный выбор
     */
    public static int typeOfLearnWord = 0;


    private ProcessOfLearning processOfLearning;

    //private MainInterface mainInterface;
    private TextView textView;
    private LinearLayout textViewCount;
    private AppCompatImageButton backButton;

    private ArrayList<Button> buttonsList;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_learn);

        processOfLearning = ProcessOfLearning.getProcessOfLearning(this);

        //processOfLearning.updateWordsDictionary();

        setTypeOfLearnWord();

        getCurrentLearningWords();

        getRandomListForCreateButtons();

        getWordThatNeedsToBeTranslated();

        getRandomListForCreateButtons();

        setUpButtons();

        setUpLearningWord();

        showCounter();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setOnBackButton();

    }


    private void setTypeOfLearnWord() {
        if (Variables.TYPE_OF_LEARN == 0) {
            typeOfLearnWord = 0;
        } else if (Variables.TYPE_OF_LEARN == 1) {
            typeOfLearnWord = 1;
        } else {
            typeOfLearnWord = (int) (Math.random() * 2);
        }
    }

    /**
     * Загружает весь словарь из базы данных
     * Обновляет словарь изучаемых слов
     */
    private void getCurrentLearningWords() {
        processOfLearning.loadGlobalWordDictionary();
        processOfLearning.updateLearningList();
        currentLearningWords = processOfLearning.currentLearningWords;
    }


    private void setUpButtons() {

        getButtons();


        int counter = 0;

        final Animation  wrongAnim = AnimationUtils.loadAnimation(this, R.anim.wrong_answer_animation_button);


        for (Button button : buttonsList) {

            if (answeredTrue) {
                setBackgroundColorOnButton(button , Colors.YELLOW_BUTTON);
                if (typeOfLearnWord == 0)
                    button.setText(currentButtonsWords.get(counter).getEnglishWord());
                else if (typeOfLearnWord == 1)
                    button.setText(currentButtonsWords.get(counter).getRussianWord());

                if (wordThatNeedsToBeTranslated.getEnglishWord().equals(currentButtonsWords.get(counter).getEnglishWord()) && wordThatNeedsToBeTranslated.getRightAnswerCount() < Variables.HOW_MANY_TIMES_SHOW_HIGHLIGHT) {
                    setBackgroundColorOnButton(button,Colors.GREEN_BUTTON);
                }


                setTextColorOnButton(button);
                setAdaptiveTextSizeOnButton(button);
                counter++;

            } else {
                setBackgroundColorOnButton(button, Colors.RED_BUTTON);
                if (button.getText().equals(wordThatNeedsToBeTranslated.getEnglishWord()) | button.getText().equals(wordThatNeedsToBeTranslated.getRussianWord())) {
                    button.startAnimation(wrongAnim);
                    setBackgroundColorOnButton(button, Colors.GREEN_BUTTON);
                }
            }

        }

        addOnclickListener();
    }


    private void setAdaptiveTextSizeOnButton(Button button) {
        if (button.getText().length() < 10) {
            button.setTextSize(45);
        } else if (button.getText().length() < 15) {
            button.setTextSize(20);
        } else {
            button.setTextSize(20);
        }
    }

    private void setTextColorOnButton(Button button) {
        button.setTextColor(getResources().getColor(Colors.YELLOW_FOR_TEXT));
    }

    private void setBackgroundColorOnButton(Button button, int color) {
        button.setBackground(ResourcesCompat.getDrawable(getResources(), color, null));
    }

    private void setUpLearningWord() {
        if (answeredTrue) showWord();
    }


    /**
     * Данный метод определяет
     * какое слово будет показано для
     * отгадывания
     */
    private void getWordThatNeedsToBeTranslated() {
        boolean done = false;
        if (processOfLearning.getNumberOfUnlearnedWords() > 1) {
            while (!done) {
                wordThatNeedsToBeTranslated = currentButtonsWords.get((int) (Math.random() * 10));
                if (wordThatNeedsToBeTranslated.getRightAnswerCount() < Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN) {
                    done = true;
                }
            }
        } else {
            wordThatNeedsToBeTranslated = currentButtonsWords.get((int) (Math.random() * 10));
            showToast("Слова заканчиваются осталось : " + processOfLearning.getNumberOfUnlearnedWords());
        }
    }


    public void showWord() {

        TextView targetWord = findViewById(R.id.target_word);

        String word = "";


        if (typeOfLearnWord == 0) {
            word = wordThatNeedsToBeTranslated.getRussianWord().toUpperCase(Locale.ROOT);
        } else if (typeOfLearnWord == 1) {
            word = wordThatNeedsToBeTranslated.getEnglishWord().toUpperCase(Locale.ROOT);
        }

        targetWord.setText(word);

        if (word.length() < 9) {
            targetWord.setTextSize(35);
        } else if (word.length() < 11) {
            targetWord.setTextSize(30);
        } else {
            targetWord.setTextSize(25);
        }

    }


    /**
     * Показывает счётчик
     * Количество правильных ответов / Сколько раз нужно угадать слово
     */
    private void showCounter() {
        TextView targetWordCount = findViewById(R.id.count_of_target_word);
        String counterText;
        if (wordThatNeedsToBeTranslated.getRightAnswerCount() >= 0) {
            counterText = wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN;
        } else {
            counterText = "0/" + Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN;
        }
        targetWordCount.setText(counterText);
    }


    /**
     * Создаём список
     * случайных слов
     * для кнопок
     * из ранее созданного списка
     * для изучения слов
     */
    private void getRandomListForCreateButtons() {

        currentButtonsWords = new ArrayList<>();

        WordCard wordCard;
        int random;

        while (currentButtonsWords.size() < 10) {

            random = (int) (Math.random() * Variables.HOW_MANY_WORDS_NEED_FOR_LEARNING);
            wordCard = currentLearningWords.get(random);


            if (!currentButtonsWords.contains(wordCard)) {
                currentButtonsWords.add(wordCard);
            }

        }


    }

    private void addOnclickListener() {
        for (Button button : buttonsList) {
            button.setOnClickListener(e -> pressButton(button));
        }
    }

    private void pressButton(Button button) {
        String text = (String) button.getText();
        if (text.equals(wordThatNeedsToBeTranslated.getRussianWord()) || text.equals(wordThatNeedsToBeTranslated.getEnglishWord())) {
            rightAnswerReaction(button);
        } else {
            wrongAnswerReaction();
        }
    }

    private void rightAnswerReaction(Button button) {
        answeredTrue = true;
        reactionToTheRightAnswer(wordThatNeedsToBeTranslated);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_answer_animation_button);




        setBackgroundColorOnButton(button,Colors.GREEN_BUTTON);
        button.startAnimation(animation);


        new Handler().postDelayed(() -> {
            setTypeOfLearnWord();
            getCurrentLearningWords();
            getRandomListForCreateButtons();
            getWordThatNeedsToBeTranslated();
            setUpButtons();
            setUpLearningWord();
            showCounter();
        }, 300);

    }

    public void reactionToTheRightAnswer(WordCard wordCard) {
        wordCard.setRightAnswerCount(wordCard.getRightAnswerCount() + 1);
        if (wordCard.getRightAnswerCount() < Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN) {
            processOfLearning.changeExistingWordInDataBase(wordCard);
        } else {
            wordCard.setNowLearning(0);
            wordCard.setIsLearned(1);
            currentLearningWords.remove(wordCard);
        }
        reWriteWordCardInCurrentLearnList(wordCard);
        processOfLearning.changeExistingWordInDataBase(wordCard);
    }

    private void reWriteWordCardInCurrentLearnList(WordCard wordCard) {
        currentLearningWords.remove(wordCard);
        currentLearningWords.add(wordCard);
    }


    private void wrongAnswerReaction() {
        answeredTrue = false;
        reactionToTheWrongAnswer(wordThatNeedsToBeTranslated);
        setUpButtons();
        setUpLearningWord();
        showCounter();
    }

    public void reactionToTheWrongAnswer(WordCard wordCard) {
        wordCard.setRightAnswerCount(-1);
        wordCard.setWrongAnswerCount(wordCard.getWrongAnswerCount() + 1);
        processOfLearning.changeExistingWordInDataBase(wordCard);
        reWriteWordCardInCurrentLearnList(wordCard);
    }


    private void getButtons() {
        buttonsList = new ArrayList<>();
        buttonsList.add(button0 = findViewById(R.id.button_1));
        buttonsList.add(button1 = findViewById(R.id.button_2));
        buttonsList.add(button2 = findViewById(R.id.button_3));
        buttonsList.add(button3 = findViewById(R.id.button_4));
        buttonsList.add(button4 = findViewById(R.id.button_5));
        buttonsList.add(button5 = findViewById(R.id.button_6));
        buttonsList.add(button6 = findViewById(R.id.button_7));
        buttonsList.add(button7 = findViewById(R.id.button_8));
        buttonsList.add(button8 = findViewById(R.id.button_9));
        buttonsList.add(button9 = findViewById(R.id.button_10));
    }


    @Override
    protected void onResume() {
        super.onResume();
        textView = findViewById(R.id.target_word);
        textViewCount = findViewById(R.id.count_of_target_word_layout);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        button0 = findViewById(R.id.button_10);

        final Animation fromUp = AnimationUtils.loadAnimation(this, R.anim.from_up_to_down);
        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);

        textView.startAnimation(fromUp);

        textViewCount.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_left_to_right));
        backButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_right_to_left));

        button1.startAnimation(rightToLeft);
        button2.startAnimation(leftToRight);
        button3.startAnimation(rightToLeft);
        button4.startAnimation(leftToRight);
        button5.startAnimation(rightToLeft);
        button6.startAnimation(leftToRight);
        button7.startAnimation(rightToLeft);
        button8.startAnimation(leftToRight);
        button9.startAnimation(rightToLeft);
        button0.startAnimation(leftToRight);
    }

    @Override
    protected void onPause() {
        super.onPause();

        final Animation hideToUp = AnimationUtils.loadAnimation(this, R.anim.hide_to_up);
        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);
        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);

        textView.startAnimation(hideToUp);

        textViewCount.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_right));
        backButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_left));

        button0.startAnimation(hideToRight);
        button1.startAnimation(hideToLeft);
        button2.startAnimation(hideToRight);
        button3.startAnimation(hideToLeft);
        button4.startAnimation(hideToRight);
        button5.startAnimation(hideToLeft);
        button6.startAnimation(hideToRight);
        button7.startAnimation(hideToLeft);
        button8.startAnimation(hideToRight);
        button9.startAnimation(hideToLeft);
    }


    private void setOnBackButton() {
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(e -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LearnActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}