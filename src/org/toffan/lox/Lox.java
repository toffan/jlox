package org.toffan.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

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
        if (hadRuntimeError) {
            System.exit(70);
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
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // Stop if the was a syntax error.
        if (hadError) {
            return;
        }

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        // Stop if there was a resolution error.
        if (hadError) {
            return;
        }

        interpreter.interpret(statements);
    }

    public static void error(int line, String msg) {
        report(line, "", msg);
    }

    private static void report(int line, String where, String msg) {
        System.err.println("[line " + line + "] Error " + where + ": " + msg);
        hadError = true;
    }

    public static void error(Token token, String msg) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", msg);
        } else {
            report(token.line, " at '" + token.lexeme + "'", msg);
        }
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line +
                           "]");
        hadRuntimeError = true;
    }
}
