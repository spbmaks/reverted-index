package ru.infos.dcn.java.Lab1;

/* This program illustrates recursive descent parsing using a
   pure procedural approach.

   The grammar:

   БЫЛО
   statement = { expression  ";" } "."
   expression = term { ( "+" | "-" ) term }
   term      = factor { ( "*" | "/" ) factor }
   factor    = number | "(" expression ")"

   СТАЛО
   statement = { expression  ";" } "."
   expression = term { "|" term }
   term      = factor { "&" factor }
   factor    = word | "!" factor | "(" expression ")"

*/

import java.util.ArrayList;

public class Parser {

    private Scanner2 scanner;
    private Finder finder = null;

    public Parser(/*Scanner2 scanner*/String request,Finder finder) {
        this.scanner = new Scanner2(request);
        this.finder = finder;
    } // Parser


    public ArrayList<String> run() {
        scanner.getToken();
        return statement();
    } // run


    private ArrayList<String> statement() {
        //   statement = { expression  ";" } "."
        ArrayList<String> value = null;
        while (scanner.token != /*Token.period*/Token.semicolon) {
            value = expression();
        } // while
        return value;
    } // statement


    private ArrayList<String> expression() {
        ArrayList<String> left = term();
        while (scanner.token == Token.orop) {
            scanner.getToken();
            left = finder.orOperation(left,term());
        } // while
        return left;
    } // expression


    private ArrayList<String> term() {
        ArrayList<String> left = factor();
        while (scanner.token == Token.andop) {
            scanner.getToken();
            left = finder.andOperation(left,factor());
        } // while
        return left;
    } // term


    private ArrayList<String> factor() {
        //    factor    = number | "(" expression ")"
        ArrayList<String> value = null;
        switch (scanner.token) {
            case Token.not:
                scanner.getToken();
                value =  finder.fileListWhereWordNotIn(factor());
               // scanner.getToken();// flush word
                break;
            case Token.word:
                value =  finder.fileListWhereWord(scanner.word());
                scanner.getToken();// flush word
                break;
            case Token.lparen:
                scanner.getToken();
                value = expression();
                if (scanner.token != Token.rparen)
                    scanner.error("Missing ')'");
                scanner.getToken();  // flush ")"
                break;
            default:
                scanner.error("Expecting word or (");
                break;
        } // switch
        return value;
    } // factor


} // class Parser