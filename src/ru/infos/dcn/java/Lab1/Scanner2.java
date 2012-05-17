package ru.infos.dcn.java.Lab1;


public class Scanner2 {
    private char ch = ' ';
    private String wordValue = "";
    private Buffer buffer;
    public int token;

    public Scanner2(String request) {
        buffer = new Buffer(request);
        token = Token.semicolon;
    } // Scanner


    public int getToken() {
        while (Character.isWhitespace(ch))
            ch = buffer.get();
        if (Character.isLetter(ch) || Character.isDigit(ch) || (ch == '-')) {     //слово состоит из букв, цифр и '-'
            wordValue = getWord();//Character.toLowerCase(ch);
            //ch = buffer.get();
            token = Token.word;
        } else {
            switch (ch) {
                case ';':
                    ch = buffer.get();
                    token = Token.semicolon;
                    break;

                case '.':
                    ch = buffer.get();
                    token = Token.period;
                    break;

                case '|':
                    ch = buffer.get();
                    token = Token.orop;
                    break;

                case '&':
                    ch = buffer.get();
                    token = Token.andop;
                    break;

                case '!':
                    ch = buffer.get();
                    token = Token.not;
                    break;

                case '(':
                    ch = buffer.get();
                    token = Token.lparen;
                    break;

                case ')':
                    ch = buffer.get();
                    token = Token.rparen;
                    break;

                default:
                    error("Illegal character " + ch);
                    break;
            } // switch
        } // if
        return token;
    } // getToken

    public void match(int which) {
        token = getToken();
        if (token != which) {
            error("Invalid token " + Token.toString(token) +
                    "-- expecting " + Token.toString(which));
            System.exit(1);
        } // if
    } // match


    public void error(String msg) {
        System.err.println(msg);
        System.exit(1);
    } // error

    public String word() {
            return wordValue;
        } // number

    private String getWord() {
            String rslt = "";
            do {
                rslt += ch;
                ch = buffer.get();
            } while (Character.isLetter(ch) || Character.isDigit(ch) || (ch == '-'));
            return rslt.toLowerCase(); //возвращаем слово в нижнем регистре
        }
}

class Buffer {
    private String line = "";
    private int column = 0;

    public Buffer(String line) {
        this.line = line + ";.";  // ";." - признац конца запроса
        column = 0;
        System.out.println("modified(filtered) request: " + line);
    } // Buffer

    public char get() {
        column++;
        if (column > this.line.length()) {
            System.err.println("Invalid read operation, column = "+column);
            System.exit(1);
        }
        if (this.line == null)
            System.exit(0);
        return this.line.charAt(column - 1);  // -1 так как line.length() возвращает точное число символов в строке, а line.charAt берет их со смещением на -1
    } // if column



} // class Buffer