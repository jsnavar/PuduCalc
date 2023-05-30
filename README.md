## PuduCalc: Simple interactive calculator using Pudu

This project is just an example of how to use [Pudu](https://github.com/jsnavar/Pudu).

### Grammar

PuduCalc is a line oriented calculator, operating with `Double`s, where each line is either a variable assignment or an expression to be evaluated:

```
      stmt ::= assignment | expr
assignment ::= Id := expr
      expr ::= Literal | Id
      expr ::= ( expr )
      expr ::= expr + expr | expr - expr | expr * expr | expr / expr | expr ^ expr
      expr ::= + expr | - expr | Id ( exprSeq )
   exprSeq ::= expr | expr , exprSeq
```

Operator precedence is given by:
```scala
Precedence()
    .left(plus, minus)
    .left(times, div)
    .nonassoc(uminus, uplus)
    .right(caret)
```

### Predefined functions
PuduCalc supports four functions: `sum`, `min`, `max` and `avg`.

### Error reporting
It reports syntax errors, unrecognized characters (lexical error), and the usage of undefined functions or variables.
