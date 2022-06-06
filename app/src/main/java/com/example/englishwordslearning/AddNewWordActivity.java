package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.englishwordslearning.logik.MainInterface;

public class AddNewWordActivity extends AppCompatActivity {

    private MainInterface mainInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);
        mainInterface = MainInterface.getMainInterface(this);
        setButtons();



    }

    private void setButtons() {
        Button backButton = findViewById(R.id.button_for_back_from_add);
        backButton.setOnClickListener(e -> {
            goToBack();
        });

        Button addButton = findViewById(R.id.button_for_save_word);
        addButton.setOnClickListener(this::saveWord);
    }

    private void saveWord(View view) {
        //Получаем EditText для Английского слова
        EditText englishWord = (EditText) findViewById(R.id.edit_text_english_word);
        //Получаем String из поля
        String newEnglishWord = englishWord.getText().toString();
        //Тоже самое для русского слова
        EditText newRussianWord = (EditText) findViewById(R.id.edit_text_russian_word);
        String russian = newRussianWord.getText().toString();

        //Вызываем сохранение метода в базу данных
        mainInterface.addNewWord(newEnglishWord, russian, view);
        goToBack();

    }




    private void goToBack() {
        Intent intent = new Intent(this, NewCreateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToBack();
    }
}