package ru.infos.dcn.java.Lab1;

import java.io.Serializable;

public class WordsPlace implements Serializable {
    private String filePath;
    private Integer placeInDocument;

    public WordsPlace(String filePath, Integer placeInDocument) {
        this.filePath = filePath;
        this.placeInDocument = placeInDocument;
    }


    public String getFilePath() {
        return filePath;
    }

    public Integer getPlaceInDocument() { //осмысленный порядковый номер, не положение первого символа слова в файле
        return placeInDocument;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPlaceInDocument(Integer placeInDocument) {
        this.placeInDocument = placeInDocument;
    }
}