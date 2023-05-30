package puducalc

case class UndefinedIds(vars: Set[String], functions: Set[String]):
  def addVar(v: String) = UndefinedIds(vars + v, functions)
  def addFn(fn: String) = UndefinedIds(vars, functions + fn)
  def ++(that: UndefinedIds) = UndefinedIds(vars ++ that.vars, functions ++ that.functions)

object UndefinedIds:
  def empty = UndefinedIds(Set.empty, Set.empty)

class TreeAnalizer(ctx: scala.collection.Map[String, Double]):
  val functions = Set("sum", "min", "max", "avg")
  def analize: ExprTree => UndefinedIds =
    case ConstDouble(d) => UndefinedIds.empty
    case Var(id) => if ! ctx.contains(id) then UndefinedIds.empty.addVar(id) else UndefinedIds.empty
    case Addition(l, r) => analize(l) ++ analize(r)
    case Subtraction(l, r) => analize(l) ++ analize(r)
    case Multiplication(l, r) => analize(l) ++ analize(r)
    case Division(l, r) => analize(l) ++ analize(r)
    case Pow(l, r) => analize(l) ++ analize(r)
    case UMinus(expr) => analize(expr)
    case FuncCall(fn, args) => 
      val argsUndefIds = args.map(analize).reduce(_ ++ _)
      if !functions.contains(fn) then argsUndefIds.addFn(fn) else argsUndefIds
