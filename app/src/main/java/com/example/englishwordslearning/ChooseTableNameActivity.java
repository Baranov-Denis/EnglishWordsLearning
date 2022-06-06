package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.ProcessOfLearning;
import com.example.englishwordslearning.logik.Variables;
import com.example.englishwordslearning.logik.WordCard;
import com.example.englishwordslearning.recycler.RecyclerAdapter;
import com.example.englishwordslearning.recycler_for_table_names.RecyclerAdapterForTableNames;

public class ChooseTableNameActivity extends AppCompatActivity {

    private MainInterface mainInterface;
    private String[] tableNames;
    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_table_name);
        mainInterface = MainInterface.getMainInterface(this);
        selectedItem = ProcessOfLearning.getCurrentTableNum();
        setRecyclerView();
        setButton();
    }


    private void setRecyclerView() {
        RecyclerView wordsRecycler = findViewById(R.id.table_names_recycler);
        tableNames = WordsDataBaseHelper.getTableNamesList().toArray(new String[0]);



        RecyclerAdapterForTableNames adapter = new RecyclerAdapterForTableNames(tableNames);

        adapter.setListener(new RecyclerAdapter.Listener() {
            @Override
            public void onClick(int position) {
                selectedItem = position;
            }
        });

        wordsRecycler.setAdapter(adapter);
        wordsRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setButton() {
        Button okButton = findViewById(R.id.button_for_choose_table);
        okButton.setOnClickListener(view -> {
            ProcessOfLearning.setCurrentTableNum(selectedItem);

            mainInterface.updateWordsDictionary();

            int dictionarySize = mainInterface.getNumberOfAllWords();

            int countOfCurrentLearnWords = mainInterface.getTheNumberOfWordsBeingStudied();

            if (dictionarySize < countOfCurrentLearnWords || countOfCurrentLearnWords < 10) {
                mainInterface.setNumberOfCurrentLearnWords(10);
                Toast toast = Toast.makeText(getApplicationContext(), "Слишком мало слов в словаре!!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
            saveSettings();
            goToBack();
        });

        Button backButton = findViewById(R.id.button_for_back_from_choosing_table);
        backButton.setOnClickListener(view -> {
            goToBack();
        });
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = MainActivity.getMySharedPreference().edit();
       // editor.putInt(MainActivity.COUNT_OF_REPEAT, mainInterface.getCountOfRepeatWord());
        editor.putInt(MainActivity.COUNT_OF_REPEAT, Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN);
        editor.putInt(MainActivity.COUNT_OF_NUMBER_CURRENT_WORDS, mainInterface.getTheNumberOfWordsBeingStudied());
       // editor.putInt(MainActivity.TYPE_OF_LEARN_WORDS, mainInterface.isTypeOfLearn());
        editor.putInt(MainActivity.TYPE_OF_LEARN_WORDS, Variables.TYPE_OF_LEARN);
        editor.putInt(MainActivity.CURRENT_TABLE_NAME, ProcessOfLearning.getCurrentTableNum());
        editor.apply();
    }

    private void goToBack() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}