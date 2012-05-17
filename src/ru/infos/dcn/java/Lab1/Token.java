package ru.infos.dcn.java.Lab1;

public class Token {

    public static final int semicolon = 0;
    public static final int period = 1;
    public static final int orop = 2;
    public static final int andop = 3;
    public static final int not = 4;
    public static final int lparen = 5;
    public static final int rparen = 6;
    public static final int word = 7;


    private static String[] spelling = {
            ";", ".", "|", "&", "!", "(", ")",
            "word"};

    public static String toString(int i) {
        if (i < 0 || i > word)
            return "";
        return spelling[i];
    } // toString

} // Token