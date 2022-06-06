package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.ProcessOfLearning;
import com.example.englishwordslearning.logik.Variables;
import com.example.englishwordslearning.logik.WriterDictionaryToTxt;

public class SettingActivity extends AppCompatActivity {
    private MainInterface mainInterface;

    LinearLayout setCountOfRepeats;
    LinearLayout setCountOfWords;
    LinearLayout setTypeOfLearningSpinner;
    Button setTableButton;
    Button saveDictionaryToTxt;
    SeekBar seekBarOfRepeat;
    SeekBar seekBarOfNumber;
    TextView textViewOfNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
        setCountOfWords = findViewById(R.id.setCountOfWords);
        setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
        setTableButton = findViewById(R.id.setTable_button);
      //  setTable = findViewById(R.id.setTable);

        setContentView(R.layout.activity_setting);
        mainInterface = MainInterface.getMainInterface(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Переназначаем действие кнопки вверх для получения анимации перехода
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        setSeekBar();
        setModeSpinner();
    // setTableSpinner();
        showWordsCount();
setButtonsSaveAndRead();
        startChoosing();

    }


    private void showWordsCount(){
        TextView wordsCountView = findViewById(R.id.count_of_all_words);
        mainInterface.updateWordsDictionary();
        int allWordsInt = mainInterface.getNumberOfAllWords();
        int learnedWords = allWordsInt - mainInterface.getNumberOfUnlearnedWords();
        wordsCountView.setText(learnedWords + " / "+ allWordsInt);
    }


    @Override
    protected void onResume() {
        super.onResume();
         setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
         setCountOfWords = findViewById(R.id.setCountOfWords);
         setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
         setTableButton = findViewById(R.id.setTable_button);

        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);

        setCountOfRepeats.startAnimation(rightToLeft);
        setCountOfWords.startAnimation(leftToRight);
        setTypeOfLearningSpinner.startAnimation(rightToLeft);
        setTableButton.startAnimation(leftToRight);
    }

    @Override
    protected void onPause() {
        super.onPause();
         setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
        setCountOfWords = findViewById(R.id.setCountOfWords);
         setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
        setTableButton = findViewById(R.id.setTable_button);

        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);
        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);

        setCountOfRepeats.startAnimation(hideToLeft);
        setCountOfWords.startAnimation(hideToRight);
        setTypeOfLearningSpinner.startAnimation(hideToLeft);
        setTableButton.startAnimation(hideToRight);


    }

    private void setSeekBar() {
        int countOfRepeatWord = Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN;
        int countOfCurrentLearnWords = mainInterface.getTheNumberOfWordsBeingStudied();

       // int countOfRepeatWord = 10;
       // int countOfCurrentLearnWords = 10;

        seekBarOfRepeat = findViewById(R.id.seek_bar_for_count_of_repeat_words);
        seekBarOfRepeat.setProgress(countOfRepeatWord);
        TextView textViewOfRepeat = findViewById(R.id.count_of_repeat_int);
        textViewOfRepeat.setText(String.valueOf(countOfRepeatWord));

        seekBarOfNumber = findViewById(R.id.seek_bar_count_of_current_learn_words);
        seekBarOfNumber.setProgress(countOfCurrentLearnWords);
        textViewOfNumber = findViewById(R.id.count_of_current_learn_words_int);
        textViewOfNumber.setText(String.valueOf(countOfCurrentLearnWords));


        seekBarOfRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int count, boolean b) {
                Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN = count + 1;
                if(count == 0){
                    Variables.HOW_MANY_TIMES_SHOW_HIGHLIGHT = 0;
                }else {
                    Variables.HOW_MANY_TIMES_SHOW_HIGHLIGHT = 1;
                }
              //  mainInterface.setCountOfRepeatWord(count + 1);
                textViewOfRepeat.setText(String.valueOf(count +1 ));
                saveSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarOfNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int count, boolean b) {
                if (mainInterface.getNumberOfAllWords() < count) {
                    count = mainInterface.getNumberOfAllWords();
                }
                mainInterface.setNumberOfCurrentLearnWords(count);
                textViewOfNumber.setText(String.valueOf(count));

                if(count == 50){
                    textViewOfNumber.setText("all words");
                    mainInterface.setNumberOfCurrentLearnWords(mainInterface.getNumberOfAllWords());
                }
                saveSettings();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     *
     *
     * Выбор типа слова
     */
    private void setModeSpinner() {
        String[] data = {"Русский - Английский", "Английский - Русский", "Случайный порядок"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_spinner, data);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop);
        Spinner spinner = findViewById(R.id.modeSpinner);
        spinner.setAdapter(adapter);

       // spinner.setSelection(mainInterface.isTypeOfLearn());
        spinner.setSelection(Variables.TYPE_OF_LEARN);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemPosition, long l) {
              // mainInterface.setTypeOfLearn(selectedItemPosition);
              //  mainInterface.updateButtons();
                Variables.TYPE_OF_LEARN = selectedItemPosition;
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setButtonsSaveAndRead(){
        saveDictionaryToTxt = findViewById(R.id.save_to_txt);

        saveDictionaryToTxt.setOnClickListener(e ->{
           WriterDictionaryToTxt.getWriter(this).writeToTxt();
        });
    }

/*
    private void setTableSpinner() {
        String[] data = WordsDataBaseHelper.getTableNamesList().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_spinner, data);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop);
        Spinner spinner = findViewById(R.id.tableSpinner);

        spinner.setAdapter(adapter);

        spinner.setSelection(ProcessOfLearning.getCurrentTableNum());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemPosition, long l) {
                ProcessOfLearning.setCurrentTableNum(selectedItemPosition);

                mainInterface.updateWordsDictionary();
             int dictionarySize =   mainInterface.getNumberOfAllWords();

                if (dictionarySize < Integer.parseInt(textViewOfNumber.getText().toString())) {
                    mainInterface.setNumberOfCurrentLearnWords(dictionarySize);
                    textViewOfNumber.setText(Integer.toString(dictionarySize));
                    seekBarOfNumber.setProgress(dictionarySize);
                }


            //    mainInterface.updateWordsDictionary();
                showWordsCount();
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
*/


    private void startChoosing(){
        Button button = findViewById(R.id.setTable_button);
        button.setOnClickListener(e->{
            Intent intent = new Intent(this, ChooseTableNameActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = MainActivity.getMySharedPreference().edit();
        //editor.putInt(MainActivity.COUNT_OF_REPEAT, mainInterface.getCountOfRepeatWord());
        editor.putInt(MainActivity.COUNT_OF_REPEAT, Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN);
        editor.putInt(MainActivity.COUNT_OF_NUMBER_CURRENT_WORDS, mainInterface.getTheNumberOfWordsBeingStudied());

       // editor.putInt(MainActivity.TYPE_OF_LEARN_WORDS, mainInterface.isTypeOfLearn());
        editor.putInt(MainActivity.TYPE_OF_LEARN_WORDS, Variables.TYPE_OF_LEARN);
        editor.putInt(MainActivity.CURRENT_TABLE_NAME, ProcessOfLearning.getCurrentTableNum());
        editor.putInt(MainActivity.HOW_MANY_TIMES_SHOW_HIGHLIGHT, Variables.HOW_MANY_TIMES_SHOW_HIGHLIGHT);
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
       // SettingActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}