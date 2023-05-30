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

class TreeEvaluator(ctx: scala.collection.Map[String, Double]):
  private def evalFuncCall(fn: String, args: Seq[Double]): Double =
    fn match
      case "sum" => args.sum
      case "max" => args.max
      case "min" => args.min
      case "avg" => args.sum / args.size
      case s => throw FunctionNotFoundException(s)

  def eval: ExprTree => Double =
    case ConstDouble(d) => d
    case Var(id) =>
      ctx.getOrElse(id, throw VarNotFoundException(id))
    case Addition(l, r) => eval(l) + eval(r)
    case Subtraction(l, r) => eval(l) - eval(r)
    case Multiplication(l, r) => eval(l) * eval(r)
    case Division(l, r) => eval(l) / eval(r)
    case Pow(l, r) => math.pow(eval(l), eval(r))
    case FuncCall(fn, args) => evalFuncCall(fn, args.map(eval))
    case UMinus(expr) => -eval(expr)
