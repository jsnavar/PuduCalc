package puducalc

import pudu.parser.generator._
import pudu.grammar._

object ParserSpec extends LanguageSpec[CalcAST, Token]:
  val lpar = Terminal[Token.LPar]("(")
  val rpar = Terminal[Token.RPar](")")

  val literal = Terminal[Token.Literal]

  val plus  = Terminal[Token.Plus]("+")
  val minus = Terminal[Token.Minus]("-")
  val times = Terminal[Token.Asterisk]("*")
  val div   = Terminal[Token.Slash]("/")
  val caret = Terminal[Token.Caret]("^")

  /* Unary plus and minus use the same tokens */
  val uminus = Terminal[Token.Minus]("u-")
  val uplus  = Terminal[Token.Plus]("u+")

  val assign = Terminal[Token.Assign](":=")

  val id     = Terminal[Token.Id]
  val comma  = Terminal[Token.Comma](",")

  /* Non terminals */
  val stmt       = NonTerminal[CalcAST]("stmt")
  val assignment = NonTerminal[Assignment]("assgn")
  val expr       = NonTerminal[ExprTree]("expr")
  val exprSeq    = NonTerminal[ExprSeq]("exprSeq")

  override val eof = Terminal[Token.EOF]
  override val error = Terminal[Token.ERROR]
  override val start = stmt
  override val precedence = Precedence()
    .left(plus, minus)
    .left(times, div)
    .nonassoc(uminus, uplus)
    .right(caret)

  /* Statements */
  (stmt ::= expr) { identity }
  (stmt ::= assignment) { identity }

  /* Expr seq (non empty by design) */
  (exprSeq ::= expr) { x => ExprSeq(Seq(x)) }
  (exprSeq ::= (expr, comma, exprSeq)) { case (x,_,ExprSeq(seq)) => ExprSeq(x +: seq) }

  /* Assignment */
  (assignment ::= (id, assign, expr)) { (id, _, expr) => Assignment(id.name, expr) }

  /* Literal and var */
  (expr ::= literal) { l => ConstDouble(l.value) }
  (expr ::= id) { v => Var(v.name) }

  /* Unary operators */
  (expr ::= (uplus, expr)) { (_, x) => x }
  (expr ::= (uminus, expr)) { (_, x) => UMinus(x) }

  /* parens */
  (expr ::= (lpar, expr, rpar)) { _._2 }

  /* Binary operators */
  (expr ::= (expr, plus, expr)) { (l,_,r) => Addition(l,r) }
  (expr ::= (expr, minus, expr)) { (l,_,r) => Subtraction(l,r) }
  (expr ::= (expr, times, expr)) { (l,_,r) => Multiplication(l,r) }
  (expr ::= (expr, div, expr)) { (l,_,r) => Division(l,r) }
  (expr ::= (expr, caret, expr)) { (l,_,r) => Pow(l,r) }

  /* function call */
  (expr ::= (id, lpar, exprSeq, rpar)) { (fn,_,args,_) => FuncCall(fn.name, args.seq) }
