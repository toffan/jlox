package org.toffan.lox;

import static org.toffan.lox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private boolean isAlpha(char c) {
        // clang-format off
        return ('a' <= c && c <= 'z')
            || ('A' <= c && c <= 'Z')
            || (c == '_');
        // clang-format on
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // At beginning of the next lexeme
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
        case '(':
            addToken(LEFT_PAREN);
            break;
        case ')':
            addToken(RIGHT_PAREN);
            break;
        case '{':
            addToken(LEFT_BRACE);
            break;
        case '}':
            addToken(RIGHT_BRACE);
            break;
        case ':':
            addToken(COLON);
            break;
        case ',':
            addToken(COMMA);
            break;
        case '.':
            addToken(DOT);
            break;
        case '-':
            addToken(MINUS);
            break;
        case '+':
            addToken(PLUS);
            break;
        case '?':
            addToken(QUESTIONMARK);
            break;
        case ';':
            addToken(SEMICOLON);
            break;
        case '*':
            addToken(STAR);
            break;
        case '!':
            addToken(match('=') ? BANG_EQUAL : BANG);
            break;
        case '=':
            addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            break;
        case '<':
            addToken(match('=') ? LESS_EQUAL : LESS);
            break;
        case '>':
            addToken(match('=') ? GREATER_EQUAL : GREATER);
            break;
        case '/':
            if (match('/')) {
                // ignore comment until end of line
                while (peek() != '\n' && !isAtEnd()) {
                    advance();
                }
            } else {
                addToken(SLASH);
            }
            break;
        case ' ':
        case '\t':
        case '\r':
            // ignore whitespaces
            break;
        case '\n':
            line++;
            break;
        case '"':
            string();
            break;
        default:
            if (isDigit(c)) {
                number();
            } else if (isAlpha(c)) {
                identifier();
            } else {
                Lox.error(line, "Unexpected character '" + c + "'.");
            }
            break;
        }
    }

    private char advance() {
        char c = source.charAt(current);
        current++;
        return c;
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        advance(); // closing '"'

        String value =
            source.substring(start + 1, current - 1); // remove surrounding '"'
        addToken(STRING, value);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
        }

        while (isDigit(peek())) {
            advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String word = source.substring(start, current);
        TokenType type = keywords.get(word);
        if (type == null) {
            type = IDENTIFIER;
        }

        addToken(type);
    }
}
