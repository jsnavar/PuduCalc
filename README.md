## PuduCalc: Simple interactive calculator using Pudu

This project is just an example of how to use [Pudu](https://github.com/jsnavar/Pudu).

### Grammar

PuduCalc is a line oriented calculator operating with `Double`s, where each line is either an expression to be evaluated, a variable assignment, or a function definition:

```
      stmt ::= assignment | expr | defun
assignment ::= Id := expr
     defun ::= Id [ idSeq ] := expr
      expr ::= Literal | Id
      expr ::= ( expr )
      expr ::= expr + expr | expr - expr | expr * expr | expr / expr | expr ^ expr
      expr ::= + expr | - expr | Id ( exprSeq )
   exprSeq ::= expr | expr , exprSeq
     idSeq ::= id | id , idSeq
```

Operator precedence is given by:
```scala
Precedence()
    .left(plus, minus)
    .left(times, div)
    .nonassoc(uminus, uplus)
    .right(caret)
```

### Predefined functions and constants
PuduCalc includes the functions: `sum`, `min`, `max`, `avg`, `log2`, `exp` and `sqrt`, and constants `Pi` and `E`.

### Error reporting
It reports syntax errors, unrecognized characters (lexical error), the usage of undefined functions or variables, calling functions with a wrong number of arguments, and attempting to redefine predefined functions or constants.

### Example session
```
sbt:PuduCalc> run
[info] running main
2 * 4
Result(1) is 8.0
Pi := 4
Can not redefine predefined id: Pi
x := 7
Assigned 'x' := 7.0
rms[x, y] := sqrt(x^2 + y^2)
Defined function rms as (x, y) => FuncCall(sqrt,List(Addition(Pow(Var(x),ConstDouble(2.0)),Pow(Var(y),ConstDouble(2.0)))))
rms(x, Pi) + 2
Result(2) is 9.672653022331282
rms(2)
Too few arguments for function rms. Expected 2 but found 1
log2(x,z)
Undefined Variable: z
Too many arguments for function log2. Expected 1 but found 2
.quit
```
