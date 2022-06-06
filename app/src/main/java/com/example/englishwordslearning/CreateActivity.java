package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;

public class CreateActivity extends AppCompatActivity {



    private MainInterface mainInterface;
    //Идентификационный номер слова из общего списка слов после нажатия на него в списке
    private long selectedWord;

    Button deleteButton;

    public long getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(long selectedWord) {
        this.selectedWord = selectedWord;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //wordsDataBase = new WordsDataBase(this);
        mainInterface = MainInterface.getMainInterface(this);
        // database = mainInterface.getSQLiteDatabase();
        createWordsList();
        setOnClickForButtons();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    private void setOnClickForButtons() {
        Button saveButton = (Button) findViewById(R.id.button_for_save_word);
        Button deleteButton = (Button) findViewById(R.id.button_for_delete_word);
        Button resetButton = findViewById(R.id.button_for_reset_all_progress);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWord(view);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord(view);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resetAllProgress();
            }
        });
    }


    /**
     * Запускаем сохранение слова
     *
     * @param view
     */
    private void saveWord(View view) {
        //Получаем EditText для Английского слова
        EditText englishWord = (EditText) findViewById(R.id.edit_text_english_word);
        //Получаем String из поля
        String newEnglishWord = englishWord.getText().toString();
        //Тоже самое для русского слова
        EditText newRussianWord = (EditText) findViewById(R.id.edit_text_russian_word);
        String russian = newRussianWord.getText().toString();

        //Вызываем сохранение метода в базу данных
        mainInterface.addNewWord(newEnglishWord, russian,view);

        // Этот код обновляет текущую activity без анимации
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void deleteWord(View view) {
        mainInterface.deleteCurrentWord(getSelectedWord());
        // Этот код обновляет текущую activity без анимации
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void resetAllProgress(){
        mainInterface.resetAllProgress();
        createWordsList();
    }


    /**
     * // Метод для создания списка слов
     */
    private void createWordsList() {
        //Получаем ListView для размещения в нем списка
        ListView wordsList = findViewById(R.id.list_of_words);
        //Получаем базу данных
        SQLiteDatabase database = mainInterface.getSQLiteDatabase();
        //Создаём курсор который содержит _id слова, английское слово, русское слово и количество правильных ответов соответствующих этому слову
        Cursor wordCursor = database.query(/*WordsDataBaseHelper.getTableName()*/  mainInterface.getCurrentTableName(), new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT","WRONG_ANSWER_STAT","NOW_LEARNING" , "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");



        //Создаём адаптер для того чтобы вывести данные из курсора на экран
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list, wordCursor,
                new String[]{"ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT","WRONG_ANSWER_STAT","NOW_LEARNING","IS_LEARNED"}, new int[]{R.id.text1, R.id.text2, R.id.text3,R.id.text4, R.id.text5, R.id.text6}, 0);




        //Подключаем адаптер
        wordsList.setAdapter(cursorAdapter);
        wordsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedWord(id);
                deleteButton = findViewById(R.id.button_for_delete_word);
                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }

                view.setBackgroundColor(getResources().getColor(R.color.yellow_dark));
                deleteButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
        };

        wordsList.setOnItemClickListener(listener);

    }


}