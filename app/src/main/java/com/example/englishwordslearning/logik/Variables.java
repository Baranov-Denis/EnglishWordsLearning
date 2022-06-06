package com.example.englishwordslearning.logik;

public class Variables {

    /**
     * Количество сколько раз
     * показывается подсказка
     * при отгадывании
     */
    public static int HOW_MANY_TIMES_SHOW_HIGHLIGHT = 1;


    /**
     * Переменная для изменения типа обучения
     * 0 = Русское слово - аглийские кнопки
     * 1 = Английское слово - русские кнопки
     * 2 = Случайный выбор
     */
    public static int TYPE_OF_LEARN = 0;


    /**
     * Количество необходимых правильных ответов для того чтобы слово считалось выученным
     */
    public static int HOW_MANY_TIMES_NEED_REPEAT_EVERY_WORD_FOR_IS_LEARN = 10;


    /**
     * Количество слов изучаемых одновременно
     * Нужно для создания словаря
     */
    public static int HOW_MANY_WORDS_NEED_FOR_LEARNING = 10;
}
