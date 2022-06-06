package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.Variables;
import com.example.englishwordslearning.logik.WordCard;
import com.example.englishwordslearning.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewCreateActivity extends AppCompatActivity {

    private MainInterface mainInterface;
    private ArrayList<WordCard> allWordsFromCurrentDictionary;

    private TextView englishSort;
    private TextView russianSort;
    private long selectedItem;
    private String sortType = "eng";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_create);
        englishSort = findViewById(R.id.engl_sort);
        russianSort = findViewById(R.id.rus_sort);
        mainInterface = MainInterface.getMainInterface(this);
        mainInterface.updateWordsDictionary();
        allWordsFromCurrentDictionary = mainInterface.getAllWordsFromCurrentDictionary();

        sortRecyclerView();
        setOnClickForButtons();
        setSortButtons();


    }


    private void setRecyclerView() {
        RecyclerView wordsRecycler = findViewById(R.id.words_recycler);
      //  allWordsFromCurrentDictionary = mainInterface.getAllWordsFromCurrentDictionary();


        String[] engWords = new String[allWordsFromCurrentDictionary.size()];
        String[] rusWords = new String[allWordsFromCurrentDictionary.size()];
        String[] stat = new String[allWordsFromCurrentDictionary.size()];


        for (int i = 0; i < engWords.length; i++) {
            engWords[i] = allWordsFromCurrentDictionary.get(i).getEnglishWord();
            rusWords[i] = allWordsFromCurrentDictionary.get(i).getRussianWord();
            if(allWordsFromCurrentDictionary.get(i).getRightAnswerCount()>0) {
                stat[i] = allWordsFromCurrentDictionary.get(i).getRightAnswerCount() + "/" + (Variables.HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN);
            }else {
               // stat[i] = "✔";
                stat[i] = "-";
            }
        }


        RecyclerAdapter adapter = new RecyclerAdapter(engWords, rusWords, stat);
        adapter.setListener(new RecyclerAdapter.Listener() {
            @Override
            public void onClick(int position) {

                WordCard wordCard = allWordsFromCurrentDictionary.get(position);
                selectedItem = wordCard.getId();
                Toast toast = Toast.makeText(getApplicationContext(),wordCard.getEnglishWord(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        wordsRecycler.setAdapter(adapter);
        wordsRecycler.setLayoutManager(new LinearLayoutManager(this));



    }

    private void setSortButtons() {

        englishSort.setOnClickListener(e -> {
            sortType = "eng";
            sortEnglishWords();

        });


        russianSort.setOnClickListener(e -> {
            sortType = "rus";
     sortRussianWords();

        });
    }


    private void setOnClickForButtons() {
        Button addButton = findViewById(R.id.button_for_save_word);
        Button deleteButton = findViewById(R.id.button_for_delete_word);
        Button resetButton = findViewById(R.id.button_for_reset_all_progress);
        Button backButton = findViewById(R.id.button_for_back_from_creating);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWord();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord(view);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllProgress();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addWord(){
        Intent intent = new Intent(this, AddNewWordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void deleteWord(View view) {
        mainInterface.deleteCurrentWord(selectedItem);
        allWordsFromCurrentDictionary = mainInterface.getAllWordsFromCurrentDictionary();
        sortRecyclerView();
    }



    private void sortRecyclerView(){
        if(sortType.equals("eng")) sortEnglishWords();
        else if (sortType.equals("rus")) sortRussianWords();
    }



    private void sortRussianWords() {

        Collections.sort(allWordsFromCurrentDictionary, new Comparator<WordCard>() {
            @Override
            public int compare(WordCard lhs, WordCard rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getRussianWord().compareTo(rhs.getRussianWord());
            }
        });
        englishSort.setText("English");
        russianSort.setText("Russian ▼");
        setRecyclerView();
    }

    private void sortEnglishWords() {
        Collections.sort(allWordsFromCurrentDictionary, new Comparator<WordCard>() {
            @Override
            public int compare(WordCard lhs, WordCard rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getEnglishWord().compareTo(rhs.getEnglishWord());
            }
        });
        englishSort.setText("English ▼");
        russianSort.setText("Russian");
        setRecyclerView();
    }



    private void resetAllProgress() {
        mainInterface.resetAllProgress();
        setRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}