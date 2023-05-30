package puducalc

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
