package puducalc

/** Sets of undefined variables and functions */
case class UndefinedIds(vars: Set[String], functions: Set[String]):
  def addVar(v: String) = UndefinedIds(vars + v, functions)
  def addFn(fn: String) = UndefinedIds(vars, functions + fn)

  def ++(that: UndefinedIds) = UndefinedIds(vars ++ that.vars, functions ++ that.functions)
  def isEmpty = vars.isEmpty && functions.isEmpty

  override def toString =
    def setToString(name: String, set: Set[String]) =
      require(!set.isEmpty)
      val setStr = set.mkString(",")
      s"$name: <$setStr>"
    def varsStr = setToString("variables", vars)
    def funcStr = setToString("functions", functions)
    if isEmpty then ""
    else if vars.isEmpty then funcStr
    else if functions.isEmpty then varsStr
    else s"$varsStr - $funcStr"

object UndefinedIds:
  def empty = UndefinedIds(Set.empty, Set.empty)

/** Collects all undefined variables and functions */
class TreeAnalizer(ctx: scala.collection.Map[String, Double]):
  val functions = Set("sum", "min", "max", "avg")
  def analize: ExprTree => UndefinedIds =
    case ConstDouble(d) => UndefinedIds.empty
    case Var(id) => if ! ctx.contains(id) then UndefinedIds(Set(id), Set.empty)
                    else UndefinedIds.empty
    case Addition(l, r) => analize(l) ++ analize(r)
    case Subtraction(l, r) => analize(l) ++ analize(r)
    case Multiplication(l, r) => analize(l) ++ analize(r)
    case Division(l, r) => analize(l) ++ analize(r)
    case Pow(l, r) => analize(l) ++ analize(r)
    case UMinus(expr) => analize(expr)
    case FuncCall(fn, args) =>
      val argsUndefIds = args.map(analize).reduce(_ ++ _)
      if !functions.contains(fn) then argsUndefIds.addFn(fn) else argsUndefIds
