package puducalc

class TreeEvaluator(ctx: Context):
  private def evalFuncCall(fn: String, args: Seq[Double]): Double =
    if Predefined.functions.contains(fn) then Predefined.evalFn(fn, args)
    else throw FunctionNotFoundException(fn)

  def eval: ExprTree => Double =
    case ConstDouble(d) => d
    case Var(id) =>
      if Predefined.constants.contains(id) then Predefined.constantValue(id)
      else ctx.vars.getOrElse(id, throw VarNotFoundException(id))
    case Addition(l, r) => eval(l) + eval(r)
    case Subtraction(l, r) => eval(l) - eval(r)
    case Multiplication(l, r) => eval(l) * eval(r)
    case Division(l, r) => eval(l) / eval(r)
    case Pow(l, r) => math.pow(eval(l), eval(r))
    case FuncCall(fn, args) => evalFuncCall(fn, args.map(eval))
    case UMinus(expr) => -eval(expr)
