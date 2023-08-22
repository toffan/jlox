# jlox

Interpreter for the lox language. Java implementation of *[Crafting Interpreters]* by
Robert Nystrom.

[Crafting Interpreters]: https://craftinginterpreters.com

## Build & run

```console
$ (cd src/org/toffan/tool; ../../../../gradlew generate)
$ ./gradlew build 
$ ./gradlew run --args examples/hello.lox
```

## jlox interpreter

`jlox` is a tree-walk interpreter. The AST classes are generated from
`src/org/toffan/tool/GenerateAst.java`.

## Lox language

[Lox] is a toy language designed by *Robert Nystrom* for its *Crafting
Interpreters* book. It is high level, dynamically typed, automatic memory
managed, object oriented.

[Lox]: https://craftinginterpreters.com/the-lox-language.html

### Hello World
```
// First example
print "Hello World";
```

### Dynamic typing
```
var name = "Bob";
var greetings = "Hi";

print greetings + " " + name;
```

### Functions & control flow
```
fun factorial(n) {
    var res = 1;
    for (var i = 2; i <= n; i = i+1) {
        res = res * i;
    }
    return res;
}

fun factorial_rec(n) {
    if (n < 2) {
        return 1;
    } else {
        return factorial_rec(n-1) * n;
    }
}

print factorial(6);
print factorial_rec(7);
```

### Closures
```
fun log(prefix) {
    fun pprint(message) {
        print "[" + prefix + "] " + message;
    }
    return pprint;
}

var debug = log("+");
debug("Start exec");  // [+] Start exec
```


### Classes and Inheritance
```
class Rect {
    init(x, y) {
        this.x = x;
        this.y = y;
    }
    area() { return this.x * this.y; }
    str() { return "Rectangle"; }
}

class ColoredRect < Rect {
    init(x, y, color) {
        super.init(x, y);
        this.color = color;
    }
    str() { return super.str() + " in " + this.color; }
}

var r = ColoredRect(3, 4, "yellow");
print r.area();  // 12
print r.str();  // Rectangle in yellow
```
