## PuduCalc: Simple interactive calculator using Pudu

This project is just an example of how to use [Pudu](https://github.com/jsnavar/Pudu).

### Grammar

PuduCalc is a line oriented calculator operating with Doubles, where each line is an expression to be evaluated, a variable assignment, or a function definition:

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
PuduCalc includes the constants `Pi == math.Pi` and `E == math.E`, varargs functions:
 - `max`
 - `min`
 - `avg`
 - `sum`

and three functions with a single parameter:
 - `log2`
 - `sqrt`
 - `exp`

### Error reporting
It reports:
- Syntax errors: unexpected end of line, unexpected tokens
- Lexical errors: unrecognized characters
- Naming errors: repeated arguments in function definitions, attempting to redefine predefined functions or constants
- Type errors: wrong number of parameters in function calls
- Not found errors: trying to use undefined functions or variables.

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
