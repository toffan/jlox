package org.toffan.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [FILE]");
            System.exit(2);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            System.out.println("Usage: jlox [FILE]");
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));
        run(new String(content, Charset.defaultCharset()));

        if (hadError) {
            System.exit(1);
        }
    }

    public static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    public static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token: tokens) {
            System.out.println(token);
        }
    }

    public static void error(int line, String msg) {
        report(line, "", msg);
    }

    private static void report(int line, String where, String msg) {
        System.err.println("[line " + line + "] Error " + where + ": " + msg);
        hadError = true;
    }
}
