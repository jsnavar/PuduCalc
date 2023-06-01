package puducalc

sealed trait CalcAST

sealed trait ExprTree extends CalcAST
case class Assignment(id: String, expr: ExprTree) extends CalcAST
case class ExprSeq(seq: Seq[ExprTree]) extends CalcAST

case class ConstDouble(lit: Double) extends ExprTree
case class Var(id: String) extends ExprTree
case class Addition(left: ExprTree, right: ExprTree) extends ExprTree
case class Subtraction(left: ExprTree, right: ExprTree) extends ExprTree
case class Multiplication(left: ExprTree, right: ExprTree) extends ExprTree
case class Division(left: ExprTree, right: ExprTree) extends ExprTree
case class Pow(left: ExprTree, right: ExprTree) extends ExprTree
case class FuncCall(fn: String, args: Seq[ExprTree]) extends ExprTree
case class UMinus(expr: ExprTree) extends ExprTree


case class TypedExpr[T <: ExprTree](tree: T, tpe: TypeRepr)
