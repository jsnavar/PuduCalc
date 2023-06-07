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
  val argsDef    = NonTerminal[IdSeq]("argsDef")
  val idSeqTail  = NonTerminal[IdSeq]("idSeqTail")
  val fundef     = NonTerminal[FunDef]("fdef")

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
  (stmt ::= fundef) { identity }

  /* Expr seq (non empty by design) */
  (exprSeq ::= expr) { x => ExprSeq(Seq(x)) }
  (exprSeq ::= (expr, comma, exprSeq)) { (head,_,tail) => ExprSeq(head +: tail.seq) }

  /* Assignment */
  (assignment ::= (id, assign, expr)) { (id, _, expr) => Assignment(id.name, expr) }

  /* Function defintion */
  (fundef ::= (id, argsDef, assign, expr)) { (id,args,_,exp) => FunDef(id.name, args.seq, exp) }

  (argsDef ::= (lpar, idSeqTail)) { _._2 }

  (idSeqTail ::= (id, rpar)) { (id,_) => IdSeq(Seq(id.name)) }
  (idSeqTail ::= (id, comma, idSeqTail)) { (id, _, tail) => IdSeq(id.name +: tail.seq) }

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
