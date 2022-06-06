package com.example.englishwordslearning.logik;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriterDictionaryToTxt {

    private static WriterDictionaryToTxt mWriterDictionaryToTxt;
    private final String FOLDER_NAME = "English Learn";
    private String FILE_NAME = "dictionary.txt";
    private List<WordCard> dictionary;
    private ProcessOfLearning mProcessOfLearning;
    private final Context mContext;

    private WriterDictionaryToTxt(Context context) {
        mContext = context;
    }

    public static WriterDictionaryToTxt getWriter(Context context) {
        if (mWriterDictionaryToTxt == null) mWriterDictionaryToTxt = new WriterDictionaryToTxt(context);
        return mWriterDictionaryToTxt;
    }


    public void writeToTxt() {
        mProcessOfLearning = ProcessOfLearning.getProcessOfLearning(mContext);
        createFileName();
        getAndUpdateDictionary();
        writeDictionary();
    }

    private void writeDictionary() {
        File on = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), FOLDER_NAME);
        on.mkdirs();
        File txtDictFile = new File(on, FILE_NAME);
        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(txtDictFile));

            for (int i = 0 ; i < dictionary.size() ; i++) {
                if(i != dictionary.size() - 1) bufferedWriter.write(dictionary.get(i).getEnglishWord() + " - " + dictionary.get(i).getRussianWord()+ "\n");
                else bufferedWriter.write(dictionary.get(i).getEnglishWord() + " - " + dictionary.get(i).getRussianWord());
            }

            bufferedWriter.close();

            showToast("Success!!!");
        } catch (IOException e) {
            showToast("Fail!!!");
        }
    }

    private void createFileName() {
        FILE_NAME = mProcessOfLearning.getCurrentTableName() + ".txt";
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getAndUpdateDictionary() {
        dictionary = mProcessOfLearning.getAllOfWordsOfDictionary();
    }
}
