package ru.infos.dcn.java.Lab1;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class RevInd implements Serializable{
    private String defaultDirectory;
    private HashMap<String,WordsPlaceList> revIndList;
    private String fileList[] = null;

    public RevInd() {
        this.defaultDirectory = "./invIndDirectory";
        if(revIndList == null)revIndList = new HashMap<String,WordsPlaceList>();
        createORupdateIndex();
    }
    public RevInd(String filePath) {
        this.defaultDirectory = filePath;
        if(revIndList == null)revIndList = new HashMap<String,WordsPlaceList>();
        createORupdateIndex();
    }

    public RevInd(HashMap<String,WordsPlaceList> revIndList) {
        this.revIndList = revIndList;
    }

    public void createORupdateIndex() {
        Scanner scanner;
        Integer placeInDocument = 0;
        String newFileList[] = new File(defaultDirectory).list();

        for (int i = 0; i < newFileList.length; i++) {
            if(!isFileExistInPrevIndex(newFileList[i])){//если файл в индексе, то ничего не добавляем; если пред списка нет(null), то считается, что каждого файла нет в индексе
                try {
                    scanner = new Scanner(new File(defaultDirectory + "\\" + newFileList[i])).useDelimiter("[.,:;()?!\"\\s]+");
                    while (scanner.hasNext()) {
                        String str = scanner.next().toLowerCase();// поиск будет без учета регистра
                        if(str.equals("Hong"))System.out.println("~~~~~~Hong~~~~~~");
                        addWord(str, placeInDocument, newFileList[i]);
                        System.out.println(str);
                    }
                    placeInDocument = 0;//для каждого нового документа
                } catch (FileNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }
        fileList = newFileList;//обновляем список проиндексированных файлов перед сохранением
    }

    private void addWord(String word, Integer placeInDocument, String filePath){
       //Integer listIndex = getWordPlaces(word);
        WordsPlaceList listIndex = getWordPlaces(word);
       if(listIndex == null)revIndList.put(word,new WordsPlaceList(word,placeInDocument,filePath));//revIndList.add(new WordsPlaceList(word,placeInDocument,filePath));//если такого слова нет в словаре
       else{
           listIndex.addPlace(filePath, placeInDocument);
       }
    }

    private boolean isWordExist(String word){
        return revIndList.containsKey(word);//если есть такой ключ, то возвращаем true
    }

    public WordsPlaceList getWordPlaces(String word){ //если слова нет, то возвращаем -1, если есть - возвращаем порядковый номер в списке слов
        if(revIndList == null)return null;
        return revIndList.get(word);//   если слова не будет, то вернем null
    }

    private boolean isFileExistInPrevIndex(String prevFilename){
        //if(prevFilename.equals("123.txt"))System.out.println("1");
        if(fileList!=null){
            for(int i=0; i< fileList.length; i++){
                if(fileList[i].equals(prevFilename))return true;
            }
        }
        return false;
    }

    public String[] getFileList() {
        return fileList;
    }
     /*
    public static void main(String[] args){
        RevInd tmp = new RevInd();
        //System.out.println("str".hashCode());
        //System.out.println("string".hashCode());
        //System.out.println("str dgadg sdsdg".hashCode());
    }
    */
}