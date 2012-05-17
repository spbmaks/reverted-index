package ru.infos.dcn.java.Lab1;

import java.io.Serializable;
import java.util.ArrayList;

public class WordsPlaceList implements Serializable {
    private String word;
    private ArrayList<WordsPlace> wordList;

    public WordsPlaceList() {
        wordList = new ArrayList<WordsPlace>();
    }

    public WordsPlaceList(String word, Integer placeInDocument, String filePath) {
        wordList = new ArrayList<WordsPlace>();
        wordList.add(new WordsPlace(filePath,placeInDocument));
        this.word = word;
    }

    public WordsPlaceList(String word, ArrayList<WordsPlace> wordList) {
        this.word = word;
        this.wordList = wordList;
    }
    public ArrayList<WordsPlace> getPlacesList(){
        return wordList;
    }
    public void addPlace(String filePath, Integer placeInDocument){
        wordList.add(new WordsPlace(filePath,placeInDocument));
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public ArrayList<String> getWordPlaces(){  //cписок уникальных имен файлов, где упоминается данное слово
        ArrayList<String> places = new ArrayList<String>();
        boolean flagPlaceExist = false;
        for(int i=0;i< wordList.size();i++){
            for(int j=0;j<places.size();j++){
               if(wordList.get(i).getFilePath().equals(places.get(j))){
                   flagPlaceExist = true;
                   break;//если уже есть в списке документов, то пропускаем шаг
               }
            }
            if(flagPlaceExist == false)places.add(wordList.get(i).getFilePath());//если в списке такого файла еще нет - добавляем
            else flagPlaceExist = false;// для след итерации снова зануляем
        }
        return places;
    }
}
