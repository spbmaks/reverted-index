package ru.infos.dcn.java.Lab1;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Finder {
    private RevInd revertedIndex = null;
    private String defaultSaveIndexDirectory = "./prevIndex";
    private String savedIndexFileName = "prevIndex";

    public Finder() {
        loadIndexBaseFromFile();// если файла нет, то будет создана новая база
    //    renewIndex();
        searchRequest();
    }

    public Finder(RevInd revertedIndex) {
        this.revertedIndex = revertedIndex;
    }


    public void setRevertedIndex(RevInd revertedIndex) {
        this.revertedIndex = revertedIndex;
    }

    public RevInd getRevertedIndex() {
        return revertedIndex;
    }
    public void renewIndex(){
        revertedIndex.createORupdateIndex();
        saveIndexBaseToFile();//сохраняем базу после обновления
    }
    private boolean isServiceCharacter(char currentChar){
        switch(currentChar){
            case '|':
            case '&':
            case '!':
            case '(':
            case ')':
                return true;
            default:
                return false;
        }
    }
   private String filterInputString(String input){  //приводим строку к общему виду для последующего разбора
        String filteredStr = "",tmpStr="";
        char currentChar = ' ',prevChar,prevSequenceChar;
        //убираем пробелы из начала строки
        for(int i = 0; i < input.length(); i++){
            if(!Character.isWhitespace(input.charAt(i))){//доходим до первого значимого символа
                input = input.substring(i);
                break;
            }
        }
        for(int i = 0; i < input.length(); i++){
            prevChar = currentChar;
            currentChar = input.charAt(i);
            if(Character.isWhitespace(currentChar)){
                for(; (currentChar == ' ') && (i + 1 < input.length()); ){
                    currentChar = input.charAt(++i);//считываем следующий символ, при этом счетчик i не должен повышаться после каждой итерации, только во время нее
                }
                //рассматриваем первый символ после пробелов
                if(i < input.length()){// если не достигли конца строки, то ...  ; иначе ничего не делаем
                    if(Character.isLetter(currentChar)||Character.isDigit(currentChar)){//например: "car black" --> "car&black"
                        if(isServiceCharacter(prevChar)&&(prevChar!=')')&&(prevChar!='(')){//если предыдущий символ служебный, но не скобки, то просто добавляем текущий символ ; пример: "(spice&worm) sandworm" --> "(spice&worm)&sandworm"
                            filteredStr += currentChar;
                        }
                        else filteredStr +="&" + currentChar;
                        /*
                        if(!isServiceCharacter(prevChar))filteredStr +="&" + currentChar;
                        else filteredStr += currentChar;
                         */
                    }
                    if(isServiceCharacter(currentChar)){//например: "car   !black" --> "car!black"
                        if((currentChar == '(')||(currentChar == '!'))filteredStr +="&" + currentChar;//например при    "!(the|memory) (leaks | do)"  --> !(the|memory)&(leaks|do)
                        else  filteredStr += currentChar;
                    }
                }
            }
            else filteredStr += currentChar;
        }

        return filteredStr;
    }
    private String getRequestFromCommandLine(BufferedReader input){
        String str = null;
        try {
            str = input.readLine().toLowerCase();  // убираем влияние регистра букв
   //         input.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return  null;
        }
        return str;
    }
    private int getChoosedAction(BufferedReader input){
        for (; ; ) {
            String str = null;
            int action = 0;
            try {
                str = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return 0;
            }
            try {
                action = Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
                System.out.println("You should choose a number, try again");
            }
            if (action != 0) {   //если не значение по умолчанию
                if ((action >= 1) && (action <= 3)) {
                    //          try{input.close();} catch(IOException ioe){}
                    return action;
                } else {
                    System.out.println("Incorrect input value(there is no existing action), try again");
                    //return 0;
                }
            }
        }
    }
    private void searchRequest(){
        int action = 0;
        boolean flagNoResult = true;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                System.out.println("Please Choose an action: ");
                System.out.println("1 : new search; 2 : update index; 3 : exit");
                action = getChoosedAction(input);
                if (action == 1) {
                    System.out.println("Please enter your request for new search: ");
                    String str = null, str2 = "";
                    ArrayList<String> results;
                    str = getRequestFromCommandLine(input);
                    //убираем повсюду &
                    Scanner s = new Scanner(str).useDelimiter("&");
                    while (s.hasNext()) {
                        str2 += s.next();
                        if (s.hasNext()) str2 += " ";
                    }
                    //     System.out.println(""+str2);
                    results = search(str2);
                    System.out.println("Result: ");
                    for (int i = 0, count = 1; i < results.size(); i++) {
                        System.out.println(count++ + ": " + results.get(i));
                        flagNoResult = false;
                    }
                    if(flagNoResult) System.out.println("No result");
                    flagNoResult = true;// т.к. в цикле, то надо поставить значение по умолчанию
                    System.out.println("End of search");
                    System.out.println("");
                }
                if(action == 2){
                    renewIndex();
                    System.out.println("Index updated");
                }
                if(action == 3){
                    input.close();
                    System.exit(0);
                }
            }
        }
        catch(IOException ioe){

        }
      //  System.out.println(str);
    }
    public ArrayList<String> fileListWhereWord(String word){
        WordsPlaceList tmp = revertedIndex.getWordPlaces(word);
        if (tmp != null) {    //может не быть слова в словаре
            return tmp.getWordPlaces();
        } else return new ArrayList<String>();
    }
    public ArrayList<String> fileListWhereWordNot(String word){
        ArrayList<String> whereWordShouldntBe = fileListWhereWord(word);
        return fileListWhereWordNotIn(whereWordShouldntBe);
    }
    public ArrayList<String> fileListWhereWordNotIn(ArrayList<String> whereWordShouldntBe){
            boolean flagIsFnameExist = false;
            ArrayList<String> tmpReverseWordPlace = new ArrayList<String>();
            ArrayList<String> tmpCurWordPlaceList;
            //tmpCurWordPlaceList = revertedIndex.getWordPlaces(word.substring(1));//убираем !
            for (int i = 0; i < revertedIndex.getFileList().length; i++) {
                for (int j = 0; j < whereWordShouldntBe.size(); j++) {
                    if (revertedIndex.getFileList()[i].equals(whereWordShouldntBe.get(j)))
                        flagIsFnameExist = true;
                }
                if (!flagIsFnameExist)
                    tmpReverseWordPlace.add(revertedIndex.getFileList()[i]);//если совпадений в списке файлов, в кот есть исключающее слово, нет, то добавляем к возможному списку файлов для ответа
                else flagIsFnameExist = false;
            }
            return tmpReverseWordPlace;
    }
    public ArrayList<String> andOperation(ArrayList<String> list1, ArrayList<String> list2){//проходим по двум спискам и выделяем в отдельный совпадающие элементы
        ArrayList<String> result = new ArrayList<String>();
        boolean flagPlaceExist = false;
        for(int i=0; i < list1.size();i++){
            for(int j=0; j < list2.size();j++){
                if(list1.get(i).equals(list2.get(j))){
                    for(int k = 0; k < result.size();k++){
                        if(list1.get(i).equals(result.get(k))){
                            flagPlaceExist = true;
                            break;//если уже есть в списке документов, то пропускаем шаг
                        }
                    }
                    if(!flagPlaceExist)result.add(list1.get(i));//если в списке такого файла еще нет - добавляем
                    else flagPlaceExist = false;// для след итерации снова зануляем
                }
            }
        }
        return result;
    }
    public ArrayList<String> orOperation(ArrayList<String> list1, ArrayList<String> list2){//проходим по двум спискам и выделяем в отдельный совпадающие элементы
            ArrayList<String> result = new ArrayList<String>();
            boolean flagPlaceExist = false;
            for(int i=0; i < list1.size();i++){
                result.add(list1.get(i));//добавляем все значения из первого списка
            }
            for(int i=0; i < list2.size();i++){       //добавляем значения из второго списка, которых еще нет
                for(int k = 0; k < result.size(); k++) {
                    if(list2.get(i).equals(result.get(k))) {
                        flagPlaceExist = true;
                        break;//если уже есть в списке документов, то пропускаем шаг
                    }
                }
                if (!flagPlaceExist) result.add(list2.get(i));//если в списке такого файла еще нет - добавляем
                else flagPlaceExist = false;// для след итерации снова зануляем
            }
            return result;
    }

    public ArrayList<String> search(String request){
        Parser parser = new Parser(filterInputString(request),this);
        return parser.run();
    }

    private void loadIndexBaseFromFile(){
            try{
                FileInputStream fis = new FileInputStream(defaultSaveIndexDirectory+"\\"+savedIndexFileName);//если файла не существует - будет брошен FileNotFoundException
                ObjectInputStream ois = new ObjectInputStream(fis);
                revertedIndex = (RevInd)ois.readObject();
                ois.close();
                System.out.println("~~~~~~Index loaded~~~~~~");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                revertedIndex = new RevInd();//если нет файла для загрузки, создаем новую базу - индекс сразу обновится
                saveIndexBaseToFile();// после обновления сохраняем изменения
              //  System.out.println("\n no existing file to load\n");
              //  e.printStackTrace();
            } catch (StreamCorruptedException e){//если файл испорчен
                revertedIndex = new RevInd();
                saveIndexBaseToFile();
               // e.printStackTrace();
            } catch(InvalidClassException ice){
                System.out.println("index base file is old or corrupted, creating a new index");
                revertedIndex = new RevInd();
                saveIndexBaseToFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    private void saveIndexBaseToFile(){
            try{
                File file = new File(defaultSaveIndexDirectory+"\\"+savedIndexFileName);
                if( file.exists() ){
                    file.delete(); //всегда заново пишем объект полностью, а не добавляем что-то
                }
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(defaultSaveIndexDirectory+"\\"+savedIndexFileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(revertedIndex);
                oos.flush();
                oos.close();
                System.out.println("~~~~~~Index saved~~~~~~");
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    public static void main(String[] args){
            Finder tmp = new Finder();
            //System.out.println("str".hashCode());
            //System.out.println("string".hashCode());
            //System.out.println("str dgadg sdsdg".hashCode());
        /*
            System.out.println(tmp.filterInputString("  (spice worm) | sandworm"));
        System.out.println(tmp.filterInputString("spice worm"));
        System.out.println(tmp.filterInputString("  pool | cars&data |boot !windows"));

        */
    }
}
